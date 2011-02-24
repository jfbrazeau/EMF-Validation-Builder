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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;
import org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor;

/**
 * This class is responsible for listening to the model changes and refreshing
 * the tree viewer.
 * 
 * @author jbrazeau
 */
final class ResourceDescriptorsModelAdapter extends AdapterImpl {

	/** The tree viewer to refresh */
	private TreeViewer viewer;

	/**
	 * Default constructor.
	 * 
	 * @param viewer
	 *            the tree viewer to refresh.
	 */
	ResourceDescriptorsModelAdapter(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse
	 * .emf.common.notify.Notification)
	 */
	@Override
	public void notifyChanged(final Notification msg) {
		boolean refreshViewer = false;
		switch (msg.getEventType()) {
		case Notification.ADD:
			((Notifier) msg.getNewValue()).eAdapters().add(this);
			refreshViewer = true;
			break;
		case Notification.REMOVE:
			((Notifier) msg.getOldValue()).eAdapters().remove(this);
			refreshViewer = true;
			break;
		}
		if (refreshViewer) {
			Object notifier = msg.getNotifier();
			if (notifier instanceof WorkspaceDescriptor) {
				notifier = ResourceDescriptorRepository.getInstance();
			}
			final Object theNotifier = notifier;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					viewer.refresh(theNotifier);
				}
			});
		}
	}
}