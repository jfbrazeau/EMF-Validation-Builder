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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.emftools.validation.builder.preferences.EMFValidationBuilderPreferencePage;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptorRepository;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.emftools.validation.builder"; //$NON-NLS-1$

	// Preferences constants
	public static final String PREDEFINED_FILE_EXTENSIONS_TO_PROCESS_PREF = "predefinedFileExtensionsToProcess";
	public static final String CUSTOM_FILE_EXTENSIONS_TO_PROCESS_PREF = "customFileExtensionsToProcess";
	public static final char PREF_SEPARATOR = '/';
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * Model containing extra information used in the build process (static as
	 * it is shared among the builders)
	 */
	private ResourceDescriptorRepository resourceDescriptorsRepository = ResourceDescriptorRepository.getInstance();

	/** Predefined file extensions to include in the validation build process */
	private List<String> predefinedFileExtensionsToProcess = new ArrayList<String>();
	
	/** File extensions to include in the validation build process */
	private List<String> fileExtensionsToProcess = new ArrayList<String>();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		resourceDescriptorsRepository.init(getStateLocation());
		loadPredefinedFileExtensions();
		updateFileExtensionsToProcess();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		predefinedFileExtensionsToProcess.clear();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Returns the predefined file extensions to process.
	 * @return the predefined file extensions to process.
	 */
	public List<String> getPredefinedFileExtensionsToProcess() {
		return predefinedFileExtensionsToProcess;
	}

	/**
	 * Loads the predefined files extensions to process.
	 * 
	 * Predefined file extensions are configured in the plugins through 
	 * the <code>org.emftools.validation.builder.fileExtensions</code>
	 * extension point.
	 */
	private void loadPredefinedFileExtensions() {
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(PLUGIN_ID, "fileExtensions").getExtensions();
		for (IExtension ext : extensions) {
			for (IConfigurationElement cfgElem : ext.getConfigurationElements()) {
				if ("fileExtension".equals(cfgElem.getName())) {
					registerPredefinedFileExtension(cfgElem.getAttribute("value"));
				}
				else if ("fileExtensionsFromEditor".equals(cfgElem.getName())) {
					String editorId = cfgElem.getAttribute("editorId");
					if (editorId != null) {
						editorId = editorId.trim();
						if (!"".equals(editorId)) {
							IExtension[] editorExtensions = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.ui.editors").getExtensions();
							for (IExtension editorExt : editorExtensions) {
								for (IConfigurationElement editorCfgElem : editorExt.getConfigurationElements()) {
									String id = editorCfgElem.getAttribute("id");
									if (editorId.equals(id)) {
										String fileExtensionsStr = editorCfgElem.getAttribute("extensions");
										if (fileExtensionsStr != null) {
											String[] fileExtensionsStrArray = fileExtensionsStr.split(",");
											for (String fileExtension : fileExtensionsStrArray) {
												registerPredefinedFileExtension(fileExtension);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Registers in a file extension in the predefined file extensions list. 
	 * @param fileExtension the file extension to register.
	 */
	private void registerPredefinedFileExtension(String fileExtension) {
		if (fileExtension != null) {
			fileExtension = fileExtension.trim();
			if (!predefinedFileExtensionsToProcess.contains(fileExtension)) {
				predefinedFileExtensionsToProcess.add(fileExtension);
			}
		}
	}
	
	/**
	 * Update the file extensions list to be processed.
	 * 
	 * This method is invoked when the preference page is validated.
	 * @see EMFValidationBuilderPreferencePage#performOk()
	 */
	public void updateFileExtensionsToProcess() {
		debug("updateFileExtensionsToProcess()");
		fileExtensionsToProcess.clear();
		// Predefined file extensions
		String prefValue = getPreferenceStore().getString(PREDEFINED_FILE_EXTENSIONS_TO_PROCESS_PREF);
		for (String predefinedFileExtension : predefinedFileExtensionsToProcess) {
			boolean isEnabled = (prefValue == null) || (prefValue.contains(Activator.PREF_SEPARATOR + predefinedFileExtension + Activator.PREF_SEPARATOR));
			if (isEnabled) {
				registerFileExtensionToProcess(predefinedFileExtension);
			}
		}
		// Custom file extensions
		prefValue = getPreferenceStore().getString(CUSTOM_FILE_EXTENSIONS_TO_PROCESS_PREF);
		if (prefValue != null) {
			if (prefValue.startsWith(String.valueOf(PREF_SEPARATOR)))
				prefValue = prefValue.substring(1);
			String[] customFileExtensions = prefValue.split(String.valueOf(PREF_SEPARATOR));
			for (String customFileExtension : customFileExtensions) {
				registerFileExtensionToProcess(customFileExtension);
			}
		}
		// Full rebuild
		SafeRunner.run(new ISafeRunnable() {
			public void run() throws Exception {
				EMFValidationBuilderHelper.scheduleCleanJobs();
			}
			public void handleException(Throwable exception) {
		        Activator.getDefault().logError("Unhandled Exception", exception);
			}
		});
	}

	/**
	 * Registers in a file extension in the file extensions list that will
	 * be processed. 
	 * @param fileExtension the file extension to register.
	 */
	private void registerFileExtensionToProcess(String fileExtension) {
		debug("  registerFileExtensionToProcess(" + fileExtension + ")");
		if (fileExtension != null) {
			fileExtension = fileExtension.trim();
			if (!fileExtensionsToProcess.contains(fileExtension)) {
				fileExtensionsToProcess.add(fileExtension);
			}
		}
	}
	
	/**
	 * Returns the file extensions list to be processed.
	 * @return the file extensions list to be processed.
	 */
	public List<String> getFileExtensionsToProcess() {
		return fileExtensionsToProcess;
	}

	/**
	 * Logs an error.
	 * @param error the error message.
	 * @param throwable the throwable to log.
	 */
	public void logError(String error, Throwable throwable) {
		if (error == null && throwable != null) {
			error = throwable.getMessage();
		}
		getLog().log(
				new Status(IStatus.ERROR,
						PLUGIN_ID, IStatus.OK,
						error, throwable));
		debug(error, throwable);
	}

	/**
	 * Logs an error.
	 * @param message the message.
	 * @param throwable the throwable to log.
	 */
	private void debug(String message, Throwable throwable) {
		if (!isDebugging()) {
			return;
		}
		if (message != null) {
			System.err.println(message);
		}
		if (throwable != null) {
			throwable.printStackTrace();
		}
	}

	/**
	 * Logs a debug message.
	 * @param message the message.
	 */
	private void debug(String message) {
		debug(message, null);
	}

}
