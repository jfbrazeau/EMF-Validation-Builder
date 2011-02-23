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
package org.emftools.validation.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Helper class that offers validation utility methods.
 */
public class EMFConstraintsHelper {

	/** Helper instances map */
	private static HashMap<String, EMFConstraintsHelper> instances = new HashMap<String, EMFConstraintsHelper>();

	/** The source */
	private String source;

	/**
	 * Default constructor.
	 * 
	 * @param source
	 *            the source.
	 */
	private EMFConstraintsHelper(String source) {
		this.source = source;
	}

	/**
	 * Returns the helper instance associated to the source.
	 * 
	 * @param source
	 *            the source.
	 * @return the helper instance associated to the source.
	 */
	public static EMFConstraintsHelper getInstance(String source) {
		EMFConstraintsHelper instance = instances.get(source);
		if (instance == null) {
			instance = new EMFConstraintsHelper(source);
			instances.put(source, instance);
		}
		return instance;
	}

	/**
	 * Adds an error if the value is empty, which means null or empty string.
	 * 
	 * @param value
	 *            the value to check.
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 * @return a boolean indicating if the entry is valid (if the value is not
	 *         empty).
	 */
	public boolean addErrorIfEmpty(String value, DiagnosticChain diagnostic,
			EObject eObject, int code, String message, Object... msgParameters) {
		boolean valid = true;
		if (value == null || value.trim().equals("")) {
			addError(diagnostic, eObject, code, message, msgParameters);
			valid = false;
		}
		return valid;
	}

	/**
	 * Adds an error if the object is not unique, except if the object
	 * corresponds to the first occurrence of the duplicated objects.
	 * 
	 * @param eObjectsList
	 *            the eObjects list.
	 * @param identfyingFeature
	 *            the feature used to identify the eObjects.
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 * @return a boolean indicating if the entry is valid.
	 */
	public boolean addErrorIfNotUnique(List<?> eObjectsList,
			EStructuralFeature identfyingFeature, DiagnosticChain diagnostic,
			EObject eObject, int code, String message, Object... msgParameters) {
		boolean valid = true;
		Object identifier = eObject.eGet(identfyingFeature);
		Iterator<?> iterator = eObjectsList.iterator();
		while (iterator.hasNext() && valid) {
			EObject currentEObject = (EObject) iterator.next();
			if (currentEObject == eObject) {
				break;
			} else {
				Object currentIdentifier = currentEObject
						.eGet(identfyingFeature);
				if (equals(identifier, currentIdentifier)) {
					valid = false;
					addError(diagnostic, eObject, code, message, msgParameters);
				}
			}
		}
		return valid;
	}

	/**
	 * Adds an error if the specified objects are equals.
	 * 
	 * @param o1
	 *            the first object to compare.
	 * @param o2
	 *            the second object to compare.
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 * @return a boolean indicating if the entry is valid.
	 */
	public boolean addErrorIfNotEquals(Object o1, Object o2,
			DiagnosticChain diagnostic, EObject eObject, int code,
			String message, Object... msgParameters) {
		boolean equals = equals(o1, o2);
		if (!equals) {
			addError(diagnostic, eObject, code, message, msgParameters);
		}
		return !equals;
	}

	/**
	 * Compares the specified objects.
	 * 
	 * @param o1
	 *            the first object to compare.
	 * @param o2
	 *            the second object to compare.
	 * @return a boolean indicating if the objects are equals.
	 */
	private boolean equals(Object o1, Object o2) {
		boolean equals = false;
		if (o1 == null) {
			equals = (o2 == null);
		} else {
			equals = (o1.equals(o2));
		}
		return equals;
	}

	/**
	 * Adds an error.
	 * 
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 * @return a boolean indicating if the entry is valid.
	 */
	public void addError(DiagnosticChain diagnostic, EObject eObject, int code,
			String message, Object... msgParameters) {
		newDiagnostic(diagnostic, eObject, Diagnostic.ERROR, code, message,
				msgParameters);
	}

	/**
	 * Adds an warning.
	 * 
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 * @return a boolean indicating if the entry is valid.
	 */
	public void addWarning(DiagnosticChain diagnostic, EObject eObject,
			int code, String message, Object... msgParameters) {
		newDiagnostic(diagnostic, eObject, Diagnostic.WARNING, code, message,
				msgParameters);
	}

	/**
	 * Creates a diagnostic.
	 * 
	 * @param diagnostic
	 *            the diagnostic.
	 * @param eObject
	 *            the eObject on which is applied the check.
	 * @param severity
	 *            the severity.
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message.
	 * @param msgParameters
	 *            the message parameters.
	 */
	private void newDiagnostic(DiagnosticChain diagnostic, EObject eObject,
			int severity, int code, String message, Object... msgParameters) {
		if (diagnostic != null) {
			String formattedMessage = MessageFormat.format(message,
					msgParameters);
			diagnostic.add(new BasicDiagnostic(severity, source, code,
					formattedMessage, new Object[] { eObject }));
		}
	}

}
