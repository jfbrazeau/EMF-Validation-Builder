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
package org.emftools.validation.builder.resourcedesc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;

public class ResourceDescriptorRepository extends AdapterImpl {

	private WorkspaceDescriptor workspace;
	private Map<String, ProjectDescriptor> projectsMap = new HashMap<String, ProjectDescriptor>();
	private Map<String, ResourceDescriptor> resourcesMap = new HashMap<String, ResourceDescriptor>();
	private IPath stateLocation;
	private boolean dirty = false;
	
	private static ResourceDescriptorRepository instance = new ResourceDescriptorRepository();
	
	public static ResourceDescriptorRepository getInstance() {
		return instance;
	}
	
	public void init(IPath stateLocation) throws IOException {
		this.stateLocation = stateLocation;
		// Workspace description file load
		if (getWorkspaceFile().exists()) {

			// File load
			ResourceSet resourceSet = new ResourceSetImpl();
			ResourcedescPackage.eINSTANCE.eClass();
			Resource workspaceResource = resourceSet.getResource(getWorkspaceFileURI(), true);
			workspace = (WorkspaceDescriptor) workspaceResource.getContents().get(0);

			// Proxy resolution
			EcoreUtil.resolveAll(workspace);
			
			// Resource resourcesMap indexation
			for (ProjectDescriptor project : workspace.getProjects()) {
				register(project);
				for (ResourceDescriptor resource : project.getResources()) {
					register(resource);
				}
			}
			
			// Listener registration
			registerNotificationListener(workspace);
		}
		else {
			clean();
		}
	}
	
	private void checkState() {
		if (stateLocation == null) {
			throw new IllegalStateException("Resource descriptor repository has not been initialized");
		}
	}

	public URIConverter getURIConverter() {
		return workspace.eResource().getResourceSet().getURIConverter();
	}

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
								+ "' (file deletion failed : '"
								+ file.getName() + "'");
			}
		}
		
		// Notification listener deletion
		if (workspace != null) {
			unregisterNotificationListener(workspace);
		}
		
		// Workspace file creation
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource workspaceResource = resourceSet.createResource(getWorkspaceFileURI());
		workspace = ResourcedescFactory.eINSTANCE.createWorkspaceDescriptor();
		workspaceResource.getContents().add(workspace);

		// Listener registration
		registerNotificationListener(workspace);
	}
	
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

	public void clean(String projectName) {
		checkState();
		ProjectDescriptor project = projectsMap.get(projectName);
		if (project != null) {
			// Resources dependencies removal
			for (ResourceDescriptor resource : project.getResources()) {
				resource.getReferencedResources().clear();
			}		
			
			// Only resources with no external referrer left must be removed
			List<ResourceDescriptor> resourcesToRemove = new ArrayList<ResourceDescriptor>();
			for (ResourceDescriptor resource : project.getResources()) {
				// Descriptor is removed only if it has no external referrer left
				EList<ResourceDescriptor> referrers = resource
						.getReferrerResources();
				boolean hasExternalReferrer = false;
				for (int i = 0; i < referrers.size() && !hasExternalReferrer; i++) {
					ResourceDescriptor referrer = referrers.get(i);
					hasExternalReferrer |= !referrer.getProject().equals(project);
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

	public void notifyChanged(Notification notification) {
		dirty = true;
	}

	public ProjectDescriptor getProjectDescriptor(String projectName) {
		checkState();
		return projectsMap.get(projectName);
	}
	
	public ResourceDescriptor getResourceDescriptor(URI resourceUri) {
		checkState();
		return resourcesMap.get(resourceUri.toString());
	}
	
	public ResourceDescriptor getOrCreateResourceDescriptor(URI resourceUri) {
		checkState();
		String resourcePath = workspace.eResource().getResourceSet().getURIConverter().normalize(resourceUri).toPlatformString(true);
		IFile resourceFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(resourcePath));
		String projectName = resourceFile.getProject().getName();
		ProjectDescriptor project = projectsMap.get(projectName);
		if (project == null) {
			project = ResourcedescFactory.eINSTANCE.createProjectDescriptor();
			project.setName(projectName);

			// EMF resource path building
			IPath projectResourcesFilePath = stateLocation.append("resourcesDescriptors-" + projectName + ".xmi");
			File projectResourcesFile = new File(projectResourcesFilePath.toOSString());
			URI projectResourcesFileURI = URI.createFileURI(projectResourcesFile.getAbsolutePath());
			findOrCreateResource(projectResourcesFileURI).getContents().add(project);
			
			// Resource descriptor model update
			workspace.getProjects().add(project);
			register(project);
			registerNotificationListener(project);
		}
		String resourceUriStr = resourceUri.toString();
		ResourceDescriptor resource = getResourceDescriptor(resourceUri);
		if (resource == null) {
			resource = ResourcedescFactory.eINSTANCE.createResourceDescriptor();
			resource.setUri(resourceUriStr);
			project.getResources().add(resource);
			register(resource);
			registerNotificationListener(resource);
		}
		return resource;
	}

	public void removeResourceDescriptor(ResourceDescriptor resource) {
		checkState();
		// Resource deletion
		resource.getReferencedResources().clear();
		resource.getReferrerResources().clear();
		ProjectDescriptor project = resource.getProject();
		project.getResources().remove(resource);
		unregister(resource);
		unregisterNotificationListener(resource);
		// Project deletion
		if (project.getResources().size() == 0) {
			workspace.getProjects().remove(project);
			unregister(project);
			unregisterNotificationListener(project);
			project.eResource().getContents().remove(project);
		}
	}

	private void registerNotificationListener(EObject eObject) {
		if (!eObject.eAdapters().contains(this)) {
			eObject.eAdapters().add(this);
		}
	}
	
	private void unregisterNotificationListener(EObject eObject) {
		eObject.eAdapters().remove(this);
	}
	
	private void register(ProjectDescriptor project) {
		String name = project.getName();
		if (projectsMap.get(name) != null) {
			throw new IllegalArgumentException("Duplicate project descriptor '" + name + "'");
		}
		projectsMap.put(name, project);
	}

	private void unregister(ProjectDescriptor project) {
		projectsMap.remove(project.getName());
	}

	private void register(ResourceDescriptor resource) {
		String resourceUri = resource.getUri();
		if (resourcesMap.get(resourceUri) != null) {
			throw new IllegalArgumentException("Duplicate resource descriptor '" + resource.getUri() + "'");
		}
		resourcesMap.put(resourceUri, resource);
	}

	private void unregister(ResourceDescriptor resource) {
		resourcesMap.remove(resource.getUri());
	}

	private File getWorkspaceFile() {
		IPath workspaceFilePath = stateLocation.append("workspace.xmi");
		return new File(workspaceFilePath.toOSString());
	}
	
	private URI getWorkspaceFileURI() {
		return URI.createFileURI(getWorkspaceFile().getAbsolutePath());
	}

	private Resource findOrCreateResource(URI uri) {
		// If this resource already exists, we reuse it
		ResourceSet rs = workspace.eResource().getResourceSet();
		Resource resource = null;
		EList<Resource> resources = rs.getResources();
		for (int i = 0; (i < resources.size()) && (resource==null); i++) {
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
	
	public static IFile getFile(ResourceDescriptor resDesc) {
		return getFile(resDesc.eResource().getResourceSet().getURIConverter(),
				URI.createURI(resDesc.getUri(), true));
	}

	public static URI getUri(IResource resource) {
		return URI.createPlatformResourceURI(resource.getFullPath().toString(),
				true);
	}

	public static IFile getFile(URIConverter uriConverter, URI uri) {
		String path = uriConverter.normalize(uri).toPlatformString(true);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
	}

}




