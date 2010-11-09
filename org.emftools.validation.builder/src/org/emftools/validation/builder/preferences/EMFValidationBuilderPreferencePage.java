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
package org.emftools.validation.builder.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.emftools.validation.builder.Activator;

/**
 * EMF Validation Builder preference page.
 */

public class EMFValidationBuilderPreferencePage extends
		FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Default constructor.
	 */
	public EMFValidationBuilderPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("EMF Validaton Builder configuration page");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		List<String> predefinedFileExtensionsToProcess = Activator.getDefault()
				.getPredefinedFileExtensionsToProcess();
		final boolean hasPredefinedFileExtsToProcess = (predefinedFileExtensionsToProcess
				.size() > 0);
		if (hasPredefinedFileExtsToProcess) {
			// This field editor is added only if there are at least one plugin 
			// that declares a file extension to validate through the 
			// org.emftools.validation.builder.fileExtensions extension point.
			addField(new AbstractFileExtensionsFieldEditor(
					Activator.PREDEFINED_FILE_EXTENSIONS_TO_PROCESS_PREF,
					"Predefined file extensions to include in the validation build process :",
					predefinedFileExtensionsToProcess, getFieldEditorParent()) {
				protected boolean getTableItemsDefaultChecked() {
					// By default file validation is enabled for
					// predefined file extensions
					return true;
				}
				protected int getTableHeightHint() {
					return 100;
				}
				protected boolean getTableGrabsVerticalSpace() {
					return false;
				}
			});
		}
		// This editor allows to enable the EMF Validation build process
		// for custom file extensions (these file extensions are retrieved
		// from the eclipse IDE editors)
		addField(new AbstractFileExtensionsFieldEditor(
				Activator.CUSTOM_FILE_EXTENSIONS_TO_PROCESS_PREF,
				"Custom file extensions to include in the validation build process :",
				getIDEDefaultFileExtensions(), getFieldEditorParent()) {
			protected boolean getTableItemsDefaultChecked() {
				// By default file validation is disabled for
				// custom file extensions
				return false;
			}
			protected int getTableHeightHint() {
				return hasPredefinedFileExtsToProcess ? 150 : 250;
			}
			protected boolean getTableGrabsVerticalSpace() {
				return true;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * When the user click on OK, the file extensions list to process
	 * is updated.
	 */
	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		Activator.getDefault().updateFileExtensionsToProcess();
		return result;
	}

	/**
	 * Returns the file extensions list retrieved from the eclipse IDE
	 * editor list.
	 * @return the file extensions.
	 */
	private List<String> getIDEDefaultFileExtensions() {
		List<String> predefinedFileExtension = Activator.getDefault()
				.getPredefinedFileExtensionsToProcess();
		List<String> result = new ArrayList<String>();
		IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint("org.eclipse.ui.editors").getExtensions();
		for (IExtension iExtension : extensions) {
			for (IConfigurationElement iConfigurationElement : iExtension
					.getConfigurationElements()) {
				if ("editor".equals(iConfigurationElement.getName())) {
					String editorFileExtensions = iConfigurationElement
							.getAttribute("extensions");
					if (editorFileExtensions != null) {
						String[] editorFileExtensionsArray = editorFileExtensions
								.split(",");
						List<String> editorFileExtensionsList = new ArrayList<String>();
						for (int k = 0; k < editorFileExtensionsArray.length; k++) {
							String string = editorFileExtensionsArray[k];
							if (string != null) {
								editorFileExtensionsList.add(string.trim());
							}
						}
						for (String editorFileExtension : editorFileExtensionsList) {
							if (!result.contains(editorFileExtension)
									&& !predefinedFileExtension
											.contains(editorFileExtension)) {
								result.add(editorFileExtension);
							}
						}
					}
				}
			}
		}
		// The result is sorted
		Collections.sort(result);
		return result;
	}

}