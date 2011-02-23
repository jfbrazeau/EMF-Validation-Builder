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
package org.emftools.validation.builder.views;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;

/**
 * The tree content provider of the validation view.
 * 
 * @author jbrazeau
 */
final class TreeContentProvider implements ITreeContentProvider {

	/**
	 * Tree input.
	 */
	private Object input;

	/** Project comparator */
	private static Comparator<ProjectDescriptor> projectComparator = new Comparator<ProjectDescriptor>() {
		@Override
		public int compare(ProjectDescriptor p1, ProjectDescriptor p2) {
			return p1.getName().compareTo(p2.getName());
		}
	};

	/** Resource comparator */
	private static Comparator<ResourceDescriptor> resourceComparator = new Comparator<ResourceDescriptor>() {
		@Override
		public int compare(ResourceDescriptor r1, ResourceDescriptor r2) {
			return URI.decode(r1.getUri()).toLowerCase()
					.compareTo(URI.decode(r2.getUri()).toLowerCase());
		}
	};

	/** Reference comparator */
	private static Comparator<ResourceReference> referenceComparator = new Comparator<ResourceReference>() {
		public int compare(ResourceReference rr1, ResourceReference rr2) {
			return resourceComparator.compare(rr1.getReferenced(),
					rr2.getReferenced());
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		input = newInput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		Object[] childs = getChildren(element);
		return childs != null && childs.length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
	 * )
	 */
	@Override
	public Object getParent(Object element) {
		Object result = null;
		if (element instanceof ProjectDescriptor) {
			result = input;
		} else if (element instanceof ResourceDescriptor) {
			result = ((ResourceDescriptor) element).getProject();
		} else if (element instanceof ResourceReference) {
			result = ((ResourceReference) element).getReferrer();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] result = null;
		if (parentElement instanceof ResourceDescriptorRepository) {
			List<ProjectDescriptor> descriptors = ((ResourceDescriptorRepository) parentElement)
					.getProjectDescriptors();
			ProjectDescriptor[] descriptorsArray = descriptors
					.toArray(new ProjectDescriptor[descriptors.size()]);
			Arrays.sort(descriptorsArray, projectComparator);
			result = descriptorsArray;
		} else if (parentElement instanceof ProjectDescriptor) {
			List<ResourceDescriptor> descriptors = ((ProjectDescriptor) parentElement)
					.getResources();
			ResourceDescriptor[] descriptorsArray = descriptors
					.toArray(new ResourceDescriptor[descriptors.size()]);
			Arrays.sort(descriptorsArray, resourceComparator);
			result = descriptorsArray;

		} else if (parentElement instanceof ResourceDescriptor) {
			ResourceDescriptor referrer = (ResourceDescriptor) parentElement;
			List<ResourceDescriptor> descriptors = ((ResourceDescriptor) parentElement)
					.getReferencedResources();
			ResourceReference[] referencesArray = new ResourceReference[descriptors
					.size()];
			for (int i = 0; i < descriptors.size(); i++) {
				ResourceDescriptor resource = descriptors.get(i);
				ResourceReference reference = new ResourceReference(referrer,
						resource);
				referencesArray[i] = reference;
			}
			Arrays.sort(referencesArray, referenceComparator);
			result = referencesArray;
		}
		return result;
	}
}