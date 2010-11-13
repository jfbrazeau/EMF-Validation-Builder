/**
 * Copyright (c) 2010, Jean-Francois Brazeau. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 * 
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIEDWARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.emftools.validation.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.action.ValidateAction.EclipseResourcesUtil;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;
import org.xml.sax.SAXParseException;

/**
 * EMF Validation builder implementation.
 */
public class EMFValidationBuilder extends IncrementalProjectBuilder {

	/** Builder identifier constant */
	public static final String BUILDER_ID = "org.emftools.validation.builder.emfValidationBuilder";

	/**
	 * Qualified name used to save the last project name that is used to detect
	 * the project renames
	 */
	private static final QualifiedName LAST_PROJECT_NAME = new QualifiedName(
			BUILDER_ID, "lastProjectName");

	/** Resource descriptor repository used to manage the resource dependencies */
	private ResourceDescriptorRepository repository = ResourceDescriptorRepository
			.getInstance();

	/**
	 * Eclipse resource utility used to create the problem markers.
	 */
	private EclipseResourcesUtil eclipseResourcesUtil = new EclipseResourcesUtil();

	/**
	 * Delta visitor used for incremental builds.
	 */
	class DeltaVisitor implements IResourceDeltaVisitor {

		/** Progress monitor */
		private IProgressMonitor monitor;

		/**
		 * Resource cache used to re-validate not a resource that has already
		 * been validated (very useful for bi-directional dependencies)
		 */
		private List<IResource> validationCache = new ArrayList<IResource>();

		/**
		 * Default constructor.
		 * 
		 * @param monitor
		 *            progress monitor.
		 */
		public DeltaVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
			case IResourceDelta.CHANGED:
				// handle added/changed resource
				performResourceAndReferersValidation(validationCache, resource,
						monitor);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				URI uri = ResourceDescriptorRepository.getUri(resource);
				ResourceDescriptor resDesc = repository
						.getResourceDescriptor(uri);
				if (resDesc != null) {
					// Referrers clone build
					EList<ResourceDescriptor> referrersClone = new BasicEList<ResourceDescriptor>();
					referrersClone.addAll(resDesc.getReferrerResources());

					// We delete the resource descriptor from the repository
					repository.removeResourceDescriptor(resDesc);

					// Referrers validation (if those referrers still reference
					// the deleted resource descriptor, it we be readded
					performResourcesListAndReferersValidation(validationCache,
							referrersClone, monitor);
				}
				break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	/**
	 * Resource visitor used for full builds.
	 */
	class ResourceVisitor implements IResourceVisitor {

		/** Progress monitor */
		private IProgressMonitor monitor;

		/**
		 * Resource cache used to re-validate not a resource that has already
		 * been validated (very useful for bi-directional dependencies)
		 */
		private List<IResource> validationCache = new ArrayList<IResource>();

		/**
		 * Default constructor.
		 * 
		 * @param monitor
		 *            progress monitor.
		 */
		public ResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core
		 * .resources.IResource)
		 */
		public boolean visit(IResource resource) throws CoreException {
			performResourceAndReferersValidation(validationCache, resource,
					monitor);
			// return true to continue visiting children.
			return true;
		}
	}

	/**
	 * Performs a validation on a resource and the resources that depends on it.
	 * 
	 * @param validationCache
	 *            the resource cache used to re-validate not a resource that has
	 *            already been validated.
	 * @param resource
	 *            the resource to validate.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	private void performResourceAndReferersValidation(
			List<IResource> validationCache, IResource resource,
			IProgressMonitor monitor) throws CoreException {
		if (!validationCache.contains(resource)
				&& resource instanceof IFile
				&& Activator.getDefault().getFileExtensionsToProcess()
						.contains(resource.getFileExtension())) {
			IFile file = (IFile) resource;
			URI uri = ResourceDescriptorRepository.getUri(file);

			// Remember that validation has been processed for this resource
			validationCache.add(file);

			// Markers deletion
			file.deleteMarkers(EValidator.MARKER, false, IResource.DEPTH_ZERO);

			try {
				// File loading
				ResourceSet rs = new ResourceSetImpl();
				Resource emfResource = rs.getResource(uri, true);
				performEMFResourceValidation(validationCache, emfResource,
						monitor);
			} catch (WrappedException e) {
				Activator.getDefault().logError(
						"Unexpected error while reading a resource", e);
				IMarker marker = file.createMarker(EValidator.MARKER);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				marker.setAttribute(IMarker.MESSAGE,
						"Resource file format error ("
								+ e.getCause().getClass().getName() + ", "
								+ e.getCause().getMessage() + ")");
				int lineNumber = 0;
				if (e.getCause() instanceof SAXParseException) {
					SAXParseException sax = (SAXParseException) e.getCause();
					lineNumber = sax.getLineNumber() - 1;
				}
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			}

			// And then we validate the resources that uses the current resource
			// (the referrers).
			EList<ResourceDescriptor> referrers = repository
					.getResourceDescriptor(
							ResourceDescriptorRepository.getUri(resource))
					.getReferrerResources();
			performResourcesListAndReferersValidation(validationCache,
					referrers, monitor);
		}
	}

	/**
	 * Performs a validation on the EMF resource.
	 * 
	 * @param validationCache
	 *            the resource cache used to re-validate not a resource that has
	 *            already been validated.
	 * @param emfResource
	 *            the EMF resource to validate.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	private void performEMFResourceValidation(List<IResource> validationCache,
			final Resource emfResource, IProgressMonitor monitor)
			throws CoreException {
		ResourceSet rs = emfResource.getResourceSet();
		EList<EObject> emfContent = emfResource.getContents();
		// The default Diagnostician is overrided in order to disallow him from
		// validating the eObjects that are not contained in the current EMF
		// resource (except if that resource is not an eclipse resource)
		Diagnostician diagnostician = new Diagnostician() {
			@Override
			public boolean validate(EClass eClass, EObject eObject,
					DiagnosticChain diagnostics, Map<Object, Object> context) {
				Resource currentResource = eObject.eResource();
				if (emfResource == currentResource
						|| !currentResource.getURI().isPlatform()) {
					return super
							.validate(eClass, eObject, diagnostics, context);
				} else {
					return true;
				}
			}
		};
		// Validation ....
		BasicDiagnostic diagnostic = new BasicDiagnostic(
				EObjectValidator.DIAGNOSTIC_SOURCE, 0,
				EMFEditUIPlugin.INSTANCE.getString(
						"_UI_DiagnosisOfNObjects_message",
						new String[] { Integer.toString(emfContent.size()) }),
				emfContent.toArray());
		Map<Object, Object> context = diagnostician.createDefaultContext();
		for (EObject eObject : emfContent) {
			monitor.setTaskName(EMFEditUIPlugin.INSTANCE.getString(
					"_UI_Validating_message",
					new Object[] { diagnostician.getObjectLabel(eObject) }));
			diagnostician.validate(eObject, diagnostic, context);
		}
		// Resource dependencies retrieval (we ignore the first item wich
		// corresponds to the resource we've just processed). Notice that
		// only platform resources are processed.
		EList<Resource> newResourceDependencies = rs.getResources();
		ResourceDescriptor resourceDesc = ResourceDescriptorRepository
				.getInstance().getOrCreateResourceDescriptor(
						emfResource.getURI());

		// Creation of a map containing the current known resource dependencies
		Map<String, ResourceDescriptor> oldDependenciesMap = new HashMap<String, ResourceDescriptor>();
		EList<ResourceDescriptor> referencedResources = resourceDesc
				.getReferencedResources();
		for (ResourceDescriptor referencedResource : referencedResources) {
			oldDependenciesMap.put(referencedResource.getUri(),
					referencedResource);
		}

		// Dependencies update (we start from 1 because the position 0
		// corresponds
		// to the current resource
		for (int i = 1; i < newResourceDependencies.size(); i++) {
			Resource newResourceDependency = newResourceDependencies.get(i);
			URI newResourceDependencyUri = newResourceDependency.getURI();
			if (newResourceDependencyUri.isPlatformResource()) {
				String newResourceDependencyUriStr = newResourceDependencyUri
						.toString(); // platform:/resource/...
				if (oldDependenciesMap.get(newResourceDependencyUriStr) != null) {
					oldDependenciesMap.remove(newResourceDependencyUriStr);
				} else {
					ResourceDescriptor newDepency = repository
							.getOrCreateResourceDescriptor(newResourceDependencyUri);
					resourceDesc.getReferencedResources().add(newDepency);
				}
			}
		}

		// At this point, the map contains only old dependencies that do not
		// exist any more
		// so we remove it from the referenced resource list
		for (ResourceDescriptor referencedResource : oldDependenciesMap
				.values()) {
			resourceDesc.getReferencedResources().remove(referencedResource);

			// After having removed an obsolete dependencies in the current
			// resource, we must
			// check these referenced resource to see if we have to delete it
			// which may happen
			// in two ways, with the condition that this resource has no
			// referrer left:
			// - maybe the referenced resource doesn't exist
			// - or maybe it belongs to a project which has not the EMF
			// validation builder nature
			if (referencedResource.getReferrerResources().size() == 0) {
				IFile referencedResourceFile = ResourceDescriptorRepository
						.getFile(referencedResource.eResource()
								.getResourceSet().getURIConverter(),
								URI.createURI(referencedResource.getUri()));
				if (!referencedResourceFile.exists()
						|| !referencedResourceFile.getProject().hasNature(
								EMFValidationNature.NATURE_ID)) {
					repository.removeResourceDescriptor(referencedResource);
				}
			}
		}

		// Markers creation
		for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
			eclipseResourcesUtil.createMarkers(emfResource, childDiagnostic);
		}

	}

	/**
	 * Performs a validation on a resource list and the resources that depends
	 * on them.
	 * 
	 * @param validationCache
	 *            the resource cache used to re-validate not a resource that has
	 *            already been validated.
	 * @param resourcesDescToValidate
	 *            the resource descriptor of the resources to validate.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	private void performResourcesListAndReferersValidation(
			List<IResource> validationCache,
			EList<ResourceDescriptor> resourcesDescToValidate,
			IProgressMonitor monitor) throws CoreException {
		URIConverter uriConverter = repository.getURIConverter();
		// Clone to avoid concurrent modification (when an EMF resource
		// is uncontrolled, the parent resource dependencies are updated
		// which causes a concurrent modification on the iterator
		// if we don't use a cloned list)
		List<ResourceDescriptor> resourcesDescToValidateClone = new ArrayList<ResourceDescriptor>();
		resourcesDescToValidateClone.addAll(resourcesDescToValidate);
		for (ResourceDescriptor resourceDescToValidate : resourcesDescToValidateClone) {
			URI referrerUri = URI.createURI(resourceDescToValidate.getUri());
			IFile referrerFile = ResourceDescriptorRepository.getFile(
					uriConverter, referrerUri);
			if (referrerFile.getProject() == getProject()) {
				performResourceAndReferersValidation(validationCache,
						referrerFile, monitor);
			} else {
				referrerFile.touch(monitor);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind,
			@SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor)
			throws CoreException {

		// If project name has changed, resources descriptors
		// must be cleaned
		manageProjectNameChanges(monitor);

		// Normal build process
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}

		// Save the resource descriptors if required
		try {
			repository.save();
		} catch (IOException e) {
			Activator
					.getDefault()
					.logError(
							"Unexpected erreor while saving the EMF Validation Builder state",
							e);
		}
		return null;
	}

	/**
	 * This method handles the projects renamings.
	 * 
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	private void manageProjectNameChanges(IProgressMonitor monitor)
			throws CoreException {
		// We check if the current project has been renamed
		String currentProjectName = getProject().getName();
		String savedProjectName = getProject().getPersistentProperty(
				LAST_PROJECT_NAME);
		if (savedProjectName != null) {
			boolean projectNameHasChanged = !savedProjectName
					.equals(currentProjectName);
			if (projectNameHasChanged) {
				// We delete the old resource descriptors
				repository.clean(savedProjectName);
				// External referrers need to be rebuilt
				ProjectDescriptor projectDesc = repository
						.getProjectDescriptor(savedProjectName);
				if (projectDesc != null) {
					for (ResourceDescriptor resource : projectDesc
							.getResources()) {
						for (ResourceDescriptor referrer : resource
								.getReferrerResources()) {
							if (!referrer.getProject().equals(projectDesc)) {
								ResourceDescriptorRepository.getFile(referrer)
										.touch(monitor);
							}
						}
					}
				}
			}
		}
		// Current project name save for the next build
		getProject().setPersistentProperty(LAST_PROJECT_NAME,
				currentProjectName);
	}

	/**
	 * Performs a full build.
	 * 
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		// Re-initialize the resource descriptors of the project
		repository.clean(getProject().getName());

		// And we delete the associated markers
		IMarker[] markers = getProject().findMarkers(EValidator.MARKER, true,
				IResource.DEPTH_INFINITE);
		for (IMarker iMarker : markers) {
			iMarker.delete();
		}

		// Build
		getProject().accept(new ResourceVisitor(monitor));
	}

	/**
	 * Performs an incremental build.
	 * 
	 * @param delta
	 *            the resource delta.
	 * @param monitor
	 *            the progress monitor.
	 * @throws CoreException
	 *             thrown if an unexpected error occurs.
	 */
	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new DeltaVisitor(monitor));
	}

}
