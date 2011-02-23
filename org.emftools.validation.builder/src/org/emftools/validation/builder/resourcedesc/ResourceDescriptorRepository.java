/**
 * Copyright (c) 2010-2011, Jean-Francois Brazeau. All rights reserved.
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
package org.emftools.validation.builder.resourcedesc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * Resource descriptors repository.
 * 
 * @author jbrazeau
 */
public class ResourceDescriptorRepository extends AdapterImpl {

	/** Workspace descriptor instance */
	private WorkspaceDescriptor workspace;

	/** Project descriptors cache */
	private Map<String, ProjectDescriptor> projectsMapCache = new HashMap<String, ProjectDescriptor>();

	/** Resource descriptors cache */
	private Map<String, ResourceDescriptor> resourcesMapCache = new HashMap<String, ResourceDescriptor>();

	/** Workspace descriptor resource files location */
	private IPath stateLocation;

	/** Boolean indicating if the workspace descriptor must be flushed on disk */
	private boolean dirty = false;

	/** Singleton instance */
	private static ResourceDescriptorRepository instance = new ResourceDescriptorRepository();

	/**
	 * Default constructor.
	 */
	private ResourceDescriptorRepository() {
	}

	/**
	 * @return the singleton instance.
	 */
	public static ResourceDescriptorRepository getInstance() {
		return instance;
	}

	/**
	 * Initializes the repository.
	 * 
	 * @param stateLocation
	 *            the workspace descriptor resource files location to use.
	 * @throws IOException
	 *             thrown if an I/O exception occurs while loading the workspace
	 *             descriptors.
	 */
	public void init(IPath stateLocation) throws IOException {
		this.stateLocation = stateLocation;
		// Workspace description file load
		if (getWorkspaceFile().exists()) {

			// File load
			ResourceSet resourceSet = new ResourceSetImpl();
			ResourcedescPackage.eINSTANCE.eClass();
			Resource workspaceResource = resourceSet.getResource(
					getWorkspaceFileURI(), true);
			workspace = (WorkspaceDescriptor) workspaceResource.getContents()
					.get(0);

			// Proxy resolution
			EcoreUtil.resolveAll(workspace);

			// Resource resourcesMapCache indexation
			for (ProjectDescriptor project : workspace.getProjects()) {
				registerInCache(project);
				for (ResourceDescriptor resource : project.getResources()) {
					registerInCache(resource);
				}
			}

			// Listener registration
			registerNotificationListener(workspace);
		} else {
			clean();
		}
	}

	/**
	 * @return the project descriptors.
	 */
	public List<ProjectDescriptor> getProjectDescriptors() {
		return workspace.getProjects();
	}

	/**
	 * Checks if the repository has been initialized.
	 */
	private void checkState() {
		if (stateLocation == null) {
			throw new IllegalStateException(
					"Resource descriptor repository has not been initialized");
		}
	}

	/**
	 * Resets the workspace descriptor.
	 * 
	 * @throws IOException
	 *             thrown if an I/O exception occurs while saving the workspace
	 *             descriptors.
	 */
	public void clean() throws IOException {
		checkState();
		// State files deletion
		File stateLocationDir = new File(stateLocation.toOSString());
		File[] filesToDelete = stateLocationDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		});
		for (File file : filesToDelete) {
			if (!file.delete()) {
				throw new IOException("Unable to clean directory '"
						+ stateLocationDir.getAbsolutePath()
						+ "' (file deletion failed : '" + file.getName() + "'");
			}
		}

		// Maps clearing
		projectsMapCache.clear();
		resourcesMapCache.clear();

		// Workspace initialization
		if (workspace != null) {
			List<ProjectDescriptor> projects = new ArrayList<ProjectDescriptor>();
			projects.addAll(workspace.getProjects());
			for (ProjectDescriptor project : projects) {
				List<ResourceDescriptor> resources = new ArrayList<ResourceDescriptor>();
				resources.addAll(project.getResources());
				for (ResourceDescriptor resource : resources) {
					resource.getReferencedResources().clear();
					resource.getReferrerResources().clear();
					resource.eAdapters().clear();
					project.getResources().remove(resource);
				}
				project.eAdapters().clear();
				workspace.getProjects().remove(project);
			}

			// Remove all the resources except the workspace resource
			Resource workspaceResource = workspace.eResource();
			ResourceSet resourceSet = workspaceResource.getResourceSet();
			resourceSet.getResources().clear();
			resourceSet.getResources().add(workspaceResource);

		} else {
			// Workspace file creation
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource workspaceResource = resourceSet
					.createResource(getWorkspaceFileURI());
			workspace = ResourcedescFactory.eINSTANCE
					.createWorkspaceDescriptor();
			workspaceResource.getContents().add(workspace);

			// Listener registration
			registerNotificationListener(workspace);
		}

		// Save
		save();
	}

	/**
	 * Saves the workspace descriptors.
	 * 
	 * @throws IOException
	 *             thrown if an I/O exception occurs while saving the workspace
	 *             descriptors.
	 */
	public void save() throws IOException {
		checkState();
		if (dirty) {
			Map<Object, Object> options = new HashMap<Object, Object>();
			options.put(XMLResource.OPTION_ENCODING, "UTF-8");
			options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
					Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			for (Resource res : workspace.eResource().getResourceSet()
					.getResources()) {
				res.save(options);
			}
		}
	}

	/**
	 * Resets the descriptors for a given project.
	 * 
	 * @param projectName
	 *            the name of the project to clean.
	 */
	public void clean(String projectName) {
		checkState();
		ProjectDescriptor project = projectsMapCache.get(projectName);
		if (project != null) {
			// Resources dependencies removal
			for (ResourceDescriptor resource : project.getResources()) {
				resource.getReferencedResources().clear();
			}

			// Only resources with no external referrer left must be removed
			List<ResourceDescriptor> resourcesToRemove = new ArrayList<ResourceDescriptor>();
			for (ResourceDescriptor resource : project.getResources()) {
				// Descriptor is removed only if it has no external referrer
				// left
				EList<ResourceDescriptor> referrers = resource
						.getReferrerResources();
				boolean hasExternalReferrer = false;
				for (int i = 0; i < referrers.size() && !hasExternalReferrer; i++) {
					ResourceDescriptor referrer = referrers.get(i);
					hasExternalReferrer |= !referrer.getProject().equals(
							project);
				}
				if (!hasExternalReferrer) {
					resourcesToRemove.add(resource);
				}
			}
			// Resource descriptor removal
			for (ResourceDescriptor resourceDescriptor : resourcesToRemove) {
				removeResourceDescriptor(resourceDescriptor);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse
	 * .emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		dirty = true;
	}

	/**
	 * Returns the existing resource descriptor associated to the specified uri
	 * or creates it.
	 * 
	 * @param resourceUri
	 *            the resource uri.
	 * @return the resource descriptor.
	 */
	public ResourceDescriptor getOrCreateResourceDescriptor(URI resourceUri) {
		checkState();
		String resourcePath = workspace.eResource().getResourceSet()
				.getURIConverter().normalize(resourceUri)
				.toPlatformString(true);
		IFile resourceFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(resourcePath));
		String projectName = resourceFile.getProject().getName();
		ProjectDescriptor project = projectsMapCache.get(projectName);
		if (project == null) {
			project = ResourcedescFactory.eINSTANCE.createProjectDescriptor();
			project.setName(projectName);

			// EMF resource path building
			IPath projectResourcesFilePath = stateLocation
					.append("resourcesDescriptors-" + projectName + ".xmi");
			File projectResourcesFile = new File(
					projectResourcesFilePath.toOSString());
			URI projectResourcesFileURI = URI
					.createFileURI(projectResourcesFile.getAbsolutePath());
			findOrCreateResource(projectResourcesFileURI).getContents().add(
					project);

			// Resource descriptor model update
			workspace.getProjects().add(project);
			registerInCache(project);
			registerNotificationListener(project);
		}
		String resourceUriStr = resourceUri.toString();
		ResourceDescriptor resource = getCachedResourceDescriptor(resourceUri);
		if (resource == null) {
			resource = ResourcedescFactory.eINSTANCE.createResourceDescriptor();
			resource.setUri(resourceUriStr);
			project.getResources().add(resource);
			registerInCache(resource);
			registerNotificationListener(resource);
		}
		return resource;
	}

	/**
	 * Removes a resource descriptor form the cache and from the repository.
	 * 
	 * This method also unregisters the listeners.
	 * 
	 * @param resource
	 *            the resource to remove.
	 */
	public void removeResourceDescriptor(ResourceDescriptor resource) {
		checkState();
		// Resource deletion
		resource.getReferencedResources().clear();
		resource.getReferrerResources().clear();
		ProjectDescriptor project = resource.getProject();
		project.getResources().remove(resource);
		unregisterFromCache(resource);
		unregisterNotificationListener(resource);
		// Project deletion
		if (project.getResources().size() == 0) {
			workspace.getProjects().remove(project);
			unregisterFromCache(project);
			unregisterNotificationListener(project);
			project.eResource().getContents().remove(project);
		}
	}

	/**
	 * Registers the current repository in the EObjects EAdapters.
	 * 
	 * @param eObject
	 *            the EObject.
	 */
	private void registerNotificationListener(EObject eObject) {
		if (!eObject.eAdapters().contains(this)) {
			eObject.eAdapters().add(this);
		}
	}

	/**
	 * Unregisters the current repository from the EObjects EAdapters.
	 * 
	 * @param eObject
	 *            the EObject.
	 */
	private void unregisterNotificationListener(EObject eObject) {
		eObject.eAdapters().remove(this);
	}

	/**
	 * Registers a new adapter in the workspace.
	 * 
	 * @param adapter
	 *            the adapter to register.
	 */
	public void registerWorkspaceListener(Adapter adapter) {
		if (workspace != null) {
			if (!workspace.eAdapters().contains(adapter)) {
				workspace.eAdapters().add(adapter);
				for (ProjectDescriptor project : workspace.getProjects()) {
					project.eAdapters().add(adapter);
					for (ResourceDescriptor resource : project.getResources()) {
						resource.eAdapters().add(adapter);
					}
				}
			}
		}
	}

	/**
	 * Unregisters a new adapter from the workspace.
	 * 
	 * @param adapter
	 *            the adapter to unregister.
	 */
	public void unregisterWorkspaceListener(Adapter adapter) {
		if (workspace != null) {
			if (!workspace.eAdapters().contains(adapter)) {
				workspace.eAdapters().remove(adapter);
				for (ProjectDescriptor project : workspace.getProjects()) {
					project.eAdapters().remove(adapter);
					for (ResourceDescriptor resource : project.getResources()) {
						resource.eAdapters().remove(adapter);
					}
				}
			}
		}
	}

	/**
	 * Returns the project descriptor associated to the specified project name
	 * in the cache.
	 * 
	 * @param projectName
	 *            the project name.
	 * @return the project descriptor.
	 */
	public ProjectDescriptor getCachedProjectDescriptor(String projectName) {
		checkState();
		return projectsMapCache.get(projectName);
	}

	/**
	 * Registers a project descriptor in the cache.
	 * 
	 * @param project
	 *            the project descriptor.
	 */
	private void registerInCache(ProjectDescriptor project) {
		String name = project.getName();
		if (projectsMapCache.get(name) != null) {
			throw new IllegalArgumentException("Duplicate project descriptor '"
					+ name + "'");
		}
		projectsMapCache.put(name, project);
	}

	/**
	 * Unregisters a project descriptor from the cache.
	 * 
	 * @param project
	 *            the project descriptor.
	 */
	private void unregisterFromCache(ProjectDescriptor project) {
		projectsMapCache.remove(project.getName());
	}

	/**
	 * Returns the resource descriptor associated to the specified uri in the
	 * cache.
	 * 
	 * @param resourceUri
	 *            the resource uri.
	 * @return the resource descriptor.
	 */
	public ResourceDescriptor getCachedResourceDescriptor(URI resourceUri) {
		checkState();
		return resourcesMapCache.get(resourceUri.toString());
	}

	/**
	 * Registers a resource descriptor in the cache.
	 * 
	 * @param resource
	 *            the resource descriptor.
	 */
	private void registerInCache(ResourceDescriptor resource) {
		String resourceUri = resource.getUri();
		if (resourcesMapCache.get(resourceUri) != null) {
			throw new IllegalArgumentException(
					"Duplicate resource descriptor '" + resource.getUri() + "'");
		}
		resourcesMapCache.put(resourceUri, resource);
	}

	/**
	 * Unregisters a resource descriptor from the cache.
	 * 
	 * @param resource
	 *            the resource descriptor.
	 */
	private void unregisterFromCache(ResourceDescriptor resource) {
		resourcesMapCache.remove(resource.getUri());
	}

	/**
	 * @return the workspace file.
	 */
	private File getWorkspaceFile() {
		IPath workspaceFilePath = stateLocation.append("workspace.xmi");
		return new File(workspaceFilePath.toOSString());
	}

	/**
	 * @return the workspace file URI.
	 */
	private URI getWorkspaceFileURI() {
		return URI.createFileURI(getWorkspaceFile().getAbsolutePath());
	}

	/**
	 * Returns the existing resource in the resource set or creates it.
	 * 
	 * @param uri
	 *            the resource uri.
	 * @return the resource.
	 */
	private Resource findOrCreateResource(URI uri) {
		// If this resource already exists, we reuse it
		ResourceSet rs = workspace.eResource().getResourceSet();
		Resource resource = null;
		EList<Resource> resources = rs.getResources();
		for (int i = 0; (i < resources.size()) && (resource == null); i++) {
			Resource currentResource = resources.get(i);
			if (uri.equals(currentResource.getURI())) {
				resource = currentResource;
			}
		}
		// If the resource doesn't already exist, we create it
		if (resource == null) {
			resource = rs.createResource(uri);
		}
		return resource;
	}

}
