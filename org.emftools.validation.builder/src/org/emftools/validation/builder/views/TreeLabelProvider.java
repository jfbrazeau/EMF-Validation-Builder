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

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.emftools.validation.builder.Activator;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;

/**
 * The tree label provider of the validation view.
 * 
 * @author jbrazeau
 */
final class TreeLabelProvider extends LabelProvider {

	/** The resource manager that is used for the icons */
	private ResourceManager resourceManager;

	/**
	 * Default constructor.
	 * 
	 * @param resourceManager
	 *            the resource manager.
	 */
	public TreeLabelProvider(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		String text = null;
		if (element instanceof ProjectDescriptor) {
			text = ((ProjectDescriptor) element).getName();
		} else if (element instanceof ResourceDescriptor) {
			IFile res = ((ResourceDescriptor) element).getFile();
			text = res.getProjectRelativePath().toPortableString();
		} else if (element instanceof ResourceReference) {
			ResourceDescriptor resource = ((ResourceReference) element)
					.getReferenced();
			IFile res = resource.getFile();
			text = res.getFullPath().toPortableString();
		} else {
			text = super.getText(element);
		}
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof ProjectDescriptor) {
			URL url = Platform.getBundle("org.eclipse.ui.ide").getResource(
					"icons/full/obj16/prj_obj.gif");
			image = resourceManager.createImage(ImageDescriptor
					.createFromURL(url));
		} else if (element instanceof ResourceDescriptor) {
			image = getResourceIcon((ResourceDescriptor) element);
		} else if (element instanceof ResourceReference) {
			image = getResourceIcon(((ResourceReference) element)
					.getReferenced());
			URL overlayUrl = Platform.getBundle("org.eclipse.ui.ide")
					.getResource("icons/full/ovr16/filterapplied_ovr.gif");
			ImageDescriptor overlayDesc = ImageDescriptor
					.createFromURL(overlayUrl);
			DecorationOverlayIcon imageDesc = new DecorationOverlayIcon(image,
					new ImageDescriptor[] { null, null, overlayDesc, null });
			image = resourceManager.createImage(imageDesc);
		}
		return image;
	}

	/**
	 * @param resource
	 * @return
	 */
	private Image getResourceIcon(ResourceDescriptor resource) {
		try {
			IEditorDescriptor descriptor = IDE.getEditorDescriptor(resource
					.getUri());
			return descriptor != null ? resourceManager.createImage(descriptor
					.getImageDescriptor()) : null;
		} catch (PartInitException e) {
			// If an error occurs, we only log it
			// If the editor's icon is not displayable,
			// it is not critical
			Activator.getDefault().logError(
					"Unexpected error while loading the editor icon", e);
			// We return a default icon
			URL url = Platform.getBundle("org.eclipse.ui.ide").getResource(
					"icons/full/obj16/fileType_filter.gif");
			return resourceManager.createImage(ImageDescriptor
					.createFromURL(url));
		}
	}

}