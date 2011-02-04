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
package org.emftools.validation.builder.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.emftools.validation.builder.Activator;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;

//TODO Javadoc
final class EditorAndSelectionLinkListener implements ISelectionListener,
		IPartListener2 {

	/** The tree viewer to refresh */
	private TreeViewer viewer;

	/**
	 * Default constructor.
	 * 
	 * @param viewer
	 *            the tree viewer to refresh.
	 */
	EditorAndSelectionLinkListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			Object firstElement = treeSelection.getFirstElement();
			if (firstElement != null) {
				if (firstElement instanceof IFile) {
					handleFileSelection((IFile) firstElement);
				} else if (firstElement instanceof ResourceDescriptor) {
					IFile iFile = (IFile) Platform.getAdapterManager()
							.getAdapter(firstElement, IResource.class);
					handleFileSelection(iFile);
				}
			}
		}
	}

	/**
	 * Handles a file selection event.
	 * 
	 * @param file
	 *            the selected file.
	 */
	private void handleFileSelection(IFile file) {
		try {
			// TODO Splitter la méthode
			ProjectDescriptor project = ResourceDescriptorRepository
					.getInstance().getCachedProjectDescriptor(
							file.getProject().getName());
			if (project != null) {
				for (ResourceDescriptor resource : project.getResources()) {
					IFile iFile = resource.getFile();
					if (file.equals(iFile)) {
						// Selection du fichier dans l'arbre de la vue
						boolean resourceIsAlreadySelected = false;
						TreeSelection treeSelection = (TreeSelection) viewer
								.getSelection();
						if (treeSelection.size() == 1) {
							Object selectedObject = treeSelection
									.getFirstElement();
							if (selectedObject == resource) {
								resourceIsAlreadySelected = true;
							}
						}
						if (!resourceIsAlreadySelected) {
							viewer.setSelection(new StructuredSelection(
									resource));
						}

						// Activation de l'éditeur
						IWorkbenchPage activePage = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage();
						IEditorReference[] editorReferences = activePage
								.getEditorReferences();
						for (int i = 0; i < editorReferences.length; i++) {
							IEditorReference editorReference = editorReferences[i];
							IEditorInput editorInput = editorReference
									.getEditorInput();
							if (editorInput != null
									&& editorInput instanceof IFileEditorInput) {
								IFile editorFile = ((IFileEditorInput) editorInput)
										.getFile();
								if (iFile.equals(editorFile)) {
									// If the editor is already activated, no
									// need to reactivate it
									IEditorPart editor = editorReference
											.getEditor(true);
									IEditorPart activeEditor = activePage
											.getActiveEditor();
									if (editor != null
											&& editor != activeEditor) {
										activePage.activate(editor);
									}
									break;
								}
							}
						}

						break;
					}
				}
			}
		} catch (PartInitException e) {
			// If an error occurs, we only log it
			Activator.getDefault().logError("Unexpected error while ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			IEditorReference editorReference = (IEditorReference) partRef;
			try {
				IEditorInput editorInput = editorReference.getEditorInput();
				if (editorInput != null
						&& editorInput instanceof IFileEditorInput) {
					IFile file = ((IFileEditorInput) editorInput).getFile();
					if (file != null) {
						handleFileSelection(file);
					}
				}
			} catch (PartInitException e) {
				// If an error occurs, we only log it
				Activator.getDefault().logError("Unexpected error while ", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

}
