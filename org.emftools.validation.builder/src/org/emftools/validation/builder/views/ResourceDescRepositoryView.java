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
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.emftools.validation.builder.Activator;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;

/**
 * Resource descriptor repository view.
 * 
 * @author jbrazeau
 */
public class ResourceDescRepositoryView extends ViewPart {

	/**
	 * Link with editor and selection dialog settings key.
	 */
	private static final String LINK_WITH_EDITOR_AND_SELECTION = "linkWithEditorAndSelection";

	/** The tree viewer */
	private TreeViewer viewer;

	/** The resource manager that is used for the icons */
	private ResourceManager resourceManager;

	/** ResourceDescriptor model listener */
	private ResourceDescriptorsModelAdapter resourceDescriptorModelAdapter;

	/** Editor and selection listener */
	private EditorAndSelectionLinkListener editorAndSelectionLinkListener;

	/**
	 * Default constructor.
	 */
	public ResourceDescRepositoryView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// Resource manager creation
		resourceManager = new LocalResourceManager(
				JFaceResources.getResources(parent.getDisplay()));

		// Part control creation
		Tree tree = new Tree(parent, SWT.BORDER);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Viewer initialization
		viewer = new TreeViewer(tree);
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider(resourceManager));

		// Tree input initialization
		ResourceDescriptorRepository repository = ResourceDescriptorRepository
				.getInstance();
		viewer.setInput(repository);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClickEvent();
			}
		});

		// ResourceDescriptor model listener registration
		// (this component is responsible for listening to
		// each modification in the ResourceDescriptor
		// model)
		resourceDescriptorModelAdapter = new ResourceDescriptorsModelAdapter(
				viewer);
		repository.registerWorkspaceListener(resourceDescriptorModelAdapter);

		// Selection provider registration (this component is responsible
		// for giving access to the selected resources in the tree to the
		// eclipse workspace. ResourceDescriptors that are selected in the
		// tree are translated in IResource through the adapter factory
		// below)
		getSite().setSelectionProvider(viewer);
		// Eclipse adapter facroty registration
		IAdapterManager adapterManager = Platform.getAdapterManager();
		adapterManager.registerAdapters(new ResourceDescriptorAdapterFactory(),
				ResourceDescriptor.class);

		/**
		 * Test actions
		 */
		createRefreshAction();
		createCollapseAllAction();
		createLinkWithEditorAndSelectionAction();
	}

	/**
	 * Creates the refresh action.
	 */
	private void createRefreshAction() {
		Action refreshAction = new Action("Refresh", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				try {
					ResourceDescriptorRepository.getInstance().clean();
					Activator.getDefault().scheduleCleanJobs();
				} catch (Throwable t) {
					Activator
							.getDefault()
							.logError(
									"Unexpected error while cleaning the EMF Validation resource repository",
									t);
				}
			}
		};
		refreshAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/refresh.gif"));
		getViewSite().getActionBars().getToolBarManager().add(refreshAction);
	}

	/**
	 * Creates the "link with editor and selection" action.
	 */
	private void createLinkWithEditorAndSelectionAction() {
		editorAndSelectionLinkListener = new EditorAndSelectionLinkListener(
				viewer);
		Action linkWithEditorAction = new Action("Link with editor",
				Action.AS_CHECK_BOX) {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				Activator.getDefault().getDialogSettings()
						.put(LINK_WITH_EDITOR_AND_SELECTION, isChecked());
				if (page != null) {
					if (isChecked()) {
						page.addPartListener(editorAndSelectionLinkListener);
						page.addPostSelectionListener(editorAndSelectionLinkListener);
						// Selection event simulation
						editorAndSelectionLinkListener.selectionChanged(
								ResourceDescRepositoryView.this,
								viewer.getSelection());
					} else {
						page.removePartListener(editorAndSelectionLinkListener);
						page.removePostSelectionListener(editorAndSelectionLinkListener);
					}
				}
			}
		};
		linkWithEditorAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/synced.gif"));
		getViewSite().getActionBars().getToolBarManager()
				.add(linkWithEditorAction);
		boolean defaultChecked = Activator.getDefault().getDialogSettings()
				.getBoolean(LINK_WITH_EDITOR_AND_SELECTION);
		if (defaultChecked) {
			linkWithEditorAction.setChecked(true);
			linkWithEditorAction.run();
		}
	}

	/**
	 * Creates a "collapse all" action.
	 */
	private void createCollapseAllAction() {
		Action collapseAllAction = new Action("Collapse all",
				Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				viewer.collapseAll();
			}
		};
		collapseAllAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/collapseall.gif"));
		getViewSite().getActionBars().getToolBarManager()
				.add(collapseAllAction);
	}

	/**
	 * Handles a double click event.
	 */
	private void handleDoubleClickEvent() {
		TreeSelection treeSelection = (TreeSelection) viewer.getSelection();
		Object firstElement = treeSelection.getFirstElement();
		if (firstElement != null) {
			if (firstElement instanceof ResourceDescriptor) {
				ResourceDescriptor resource = (ResourceDescriptor) firstElement;
				final IFile iFile = resource.getFile();
				PlatformUI.getWorkbench().getDisplay()
						.asyncExec(new Runnable() {
							@Override
							public void run() {
								try {
									IWorkbenchPage page = PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage();
									IDE.openEditor(page, iFile);
								} catch (Throwable t) {
									Activator
											.getDefault()
											.logError(
													"Unexpected error while opening the editor for '"
															+ iFile.getFullPath()
															+ "'", t);
								}
							}
						});
			} else if (firstElement instanceof ResourceReference) {
				ResourceReference resRef = (ResourceReference) firstElement;
				viewer.setSelection(new StructuredSelection(resRef
						.getReferenced()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			page.removePartListener(editorAndSelectionLinkListener);
			page.removePostSelectionListener(editorAndSelectionLinkListener);
		}
		if (resourceManager != null) {
			resourceManager.dispose();
			resourceManager = null;
		}
		ResourceDescriptorRepository.getInstance().unregisterWorkspaceListener(
				resourceDescriptorModelAdapter);
		resourceDescriptorModelAdapter = null;
		super.dispose();
	}

}
