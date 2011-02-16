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
package org.emftools.validation.builder.resourcedesc.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourcedescFactory;
import org.emftools.validation.builder.resourcedesc.ResourcedescPackage;
import org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ResourcedescFactoryImpl extends EFactoryImpl implements
		ResourcedescFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static ResourcedescFactory init() {
		try {
			ResourcedescFactory theResourcedescFactory = (ResourcedescFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://org.emftools.validation.builder.resourcedesc/1.0");
			if (theResourcedescFactory != null) {
				return theResourcedescFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ResourcedescFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourcedescFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ResourcedescPackage.WORKSPACE_DESCRIPTOR:
			return createWorkspaceDescriptor();
		case ResourcedescPackage.RESOURCE_DESCRIPTOR:
			return createResourceDescriptor();
		case ResourcedescPackage.PROJECT_DESCRIPTOR:
			return createProjectDescriptor();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public WorkspaceDescriptor createWorkspaceDescriptor() {
		WorkspaceDescriptorImpl workspaceDescriptor = new WorkspaceDescriptorImpl();
		return workspaceDescriptor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourceDescriptor createResourceDescriptor() {
		ResourceDescriptorImpl resourceDescriptor = new ResourceDescriptorImpl();
		return resourceDescriptor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ProjectDescriptor createProjectDescriptor() {
		ProjectDescriptorImpl projectDescriptor = new ProjectDescriptorImpl();
		return projectDescriptor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourcedescPackage getResourcedescPackage() {
		return (ResourcedescPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ResourcedescPackage getPackage() {
		return ResourcedescPackage.eINSTANCE;
	}

} // ResourcedescFactoryImpl
