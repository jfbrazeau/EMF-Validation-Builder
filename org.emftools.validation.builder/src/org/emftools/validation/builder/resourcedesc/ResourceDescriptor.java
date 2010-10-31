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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getUri <em>Uri</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferencedResources <em>Referenced Resources</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferrerResources <em>Referrer Resources</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject <em>Project</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getResourceDescriptor()
 * @model
 * @generated
 */
public interface ResourceDescriptor extends EObject {
	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getResourceDescriptor_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

	/**
	 * Returns the value of the '<em><b>Referenced Resources</b></em>' reference list.
	 * The list contents are of type {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor}.
	 * It is bidirectional and its opposite is '{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferrerResources <em>Referrer Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Resources</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Resources</em>' reference list.
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getResourceDescriptor_ReferencedResources()
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferrerResources
	 * @model opposite="referrerResources"
	 * @generated
	 */
	EList<ResourceDescriptor> getReferencedResources();

	/**
	 * Returns the value of the '<em><b>Referrer Resources</b></em>' reference list.
	 * The list contents are of type {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor}.
	 * It is bidirectional and its opposite is '{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferencedResources <em>Referenced Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referrer Resources</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referrer Resources</em>' reference list.
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getResourceDescriptor_ReferrerResources()
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferencedResources
	 * @model opposite="referencedResources"
	 * @generated
	 */
	EList<ResourceDescriptor> getReferrerResources();

	/**
	 * Returns the value of the '<em><b>Project</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getResources <em>Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Project</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project</em>' container reference.
	 * @see #setProject(ProjectDescriptor)
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getResourceDescriptor_Project()
	 * @see org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getResources
	 * @model opposite="resources" required="true" transient="false"
	 * @generated
	 */
	ProjectDescriptor getProject();

	/**
	 * Sets the value of the '{@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject <em>Project</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project</em>' container reference.
	 * @see #getProject()
	 * @generated
	 */
	void setProject(ProjectDescriptor value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	boolean hasReferences();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	boolean hasReferrers();

} // ResourceDescriptor
