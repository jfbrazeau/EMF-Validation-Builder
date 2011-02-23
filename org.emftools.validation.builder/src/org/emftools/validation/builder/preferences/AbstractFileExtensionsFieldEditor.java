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
package org.emftools.validation.builder.preferences;

import java.io.StringWriter;
import java.util.List;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.emftools.validation.builder.Activator;

/**
 * Abstract field editor allowing to select several file extensions within a
 * list.
 * 
 * The filed editor shows the default editor icon associated to each file
 * extension.
 */
public abstract class AbstractFileExtensionsFieldEditor extends FieldEditor {

	/** The file extension list to show */
	private List<String> fileExtensions;

	/** The checkbox table viewer */
	private CheckboxTableViewer tableViewer;

	/** The resource manager that is used for the icons */
	private ResourceManager resourceManager;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            the field editor's name.
	 * @param text
	 *            the text to show above the table.
	 * @param fileExtensions
	 *            the file extension list to show.
	 * @param parent
	 *            the parent composite.
	 */
	public AbstractFileExtensionsFieldEditor(String name, String text,
			List<String> fileExtensions, Composite parent) {
		init(name, text);
		this.fileExtensions = fileExtensions;
		resourceManager = new LocalResourceManager(
				JFaceResources.getResources(parent.getDisplay()));
		createControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt
	 * .widgets.Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(getLabelText());
		label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false,
				numColumns, 1));
		Table table = new Table(parent, SWT.CHECK | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true,
				getTableGrabsVerticalSpace(), numColumns - 1, 2);
		gd.heightHint = getTableHeightHint();
		table.setLayoutData(gd);
		tableViewer = new CheckboxTableViewer(table);
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}
		});
		tableViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String fileExtension = (String) element;
				StringWriter buf = new StringWriter();
				buf.append("*.");
				buf.append(fileExtension);
				try {
					IEditorDescriptor descriptor = IDE
							.getEditorDescriptor("file." + fileExtension);
					if (descriptor != null) {
						buf.append(" (");
						buf.append(descriptor.getLabel());
						buf.append(")");
					}
				} catch (PartInitException e) {
					// If an error occurs, we only log it
					// If the editor's icon is not displayable,
					// it is not so critical
					Activator.getDefault().logError(
							"Unexpected error while loading the editor label",
							e);
				}
				return buf.toString();
			}

			public Image getImage(Object element) {
				String fileExtension = (String) element;
				Image icon = null;
				try {
					IEditorDescriptor descriptor = IDE
							.getEditorDescriptor("file." + fileExtension);
					icon = descriptor != null ? resourceManager
							.createImage(descriptor.getImageDescriptor())
							: null;
				} catch (PartInitException e) {
					// If an error occurs, we only log it
					// If the editor's icon is not displayable,
					// it is not so critical
					Activator
							.getDefault()
							.logError(
									"Unexpected error while loading the editor icon",
									e);
				}
				return icon;
			}
		});
		tableViewer.setInput(fileExtensions);

		Button selectAllButton = new Button(parent, SWT.NONE);
		selectAllButton.setText("Select all");
		selectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				false, 1, 1));
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(true);
			}
		});
		Button deselectAllButton = new Button(parent, SWT.NONE);
		deselectAllButton.setText("Deselect all");
		deselectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,
				false, 1, 1));
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setAllChecked(false);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		String prefValue = getPreferenceStore().getString(getPreferenceName());
		if (prefValue == null || "".equals(prefValue)) {
			doLoadDefault();
		} else {
			for (String fileExtension : fileExtensions) {
				tableViewer.setChecked(
						fileExtension,
						prefValue.contains(Activator.PREF_SEPARATOR
								+ fileExtension + Activator.PREF_SEPARATOR));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		tableViewer.setAllChecked(getTableItemsDefaultChecked());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	@Override
	protected void doStore() {
		StringWriter buf = new StringWriter();
		buf.append(Activator.PREF_SEPARATOR);
		for (String fileExtension : fileExtensions) {
			if (tableViewer.getChecked(fileExtension)) {
				buf.append(fileExtension);
				buf.append(Activator.PREF_SEPARATOR);
			}
		}
		getPreferenceStore().setValue(getPreferenceName(), buf.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * @return the table height hint.
	 */
	protected abstract int getTableHeightHint();

	/**
	 * @return a boolean indicating if the table is supposed to grab the
	 *         vertical space.
	 */
	protected abstract boolean getTableGrabsVerticalSpace();

	/**
	 * @return a boolean indicating if the table item are checked by default.
	 */
	protected abstract boolean getTableItemsDefaultChecked();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#dispose()
	 */
	@Override
	public void dispose() {
		resourceManager.dispose();
		super.dispose();
	}

}
