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
package org.emftools.validation.builder.resourcedesc;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Project Descriptor</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getResources
 * <em>Resources</em>}</li>
 * <li>
 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getWorkspace
 * <em>Workspace</em>}</li>
 * <li>
 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getName
 * <em>Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getProjectDescriptor()
 * @model
 * @generated
 */
public interface ProjectDescriptor extends EObject {
	/**
	 * Returns the value of the '<em><b>Resources</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor}.
	 * It is bidirectional and its opposite is '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject
	 * <em>Project</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resources</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Resources</em>' containment reference list.
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getProjectDescriptor_Resources()
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject
	 * @model opposite="project" containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ResourceDescriptor> getResources();

	/**
	 * Returns the value of the '<em><b>Workspace</b></em>' container reference.
	 * It is bidirectional and its opposite is '
	 * {@link org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor#getProjects
	 * <em>Projects</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Workspace</em>' container reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Workspace</em>' container reference.
	 * @see #setWorkspace(WorkspaceDescriptor)
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getProjectDescriptor_Workspace()
	 * @see org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor#getProjects
	 * @model opposite="projects" required="true" transient="false"
	 * @generated
	 */
	WorkspaceDescriptor getWorkspace();

	/**
	 * Sets the value of the '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getWorkspace
	 * <em>Workspace</em>}' container reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Workspace</em>' container reference.
	 * @see #getWorkspace()
	 * @generated
	 */
	void setWorkspace(WorkspaceDescriptor value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#getProjectDescriptor_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ProjectDescriptor
