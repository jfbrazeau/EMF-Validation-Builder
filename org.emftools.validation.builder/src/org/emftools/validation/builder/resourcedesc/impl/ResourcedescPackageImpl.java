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
package org.emftools.validation.builder.resourcedesc.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourcedescFactory;
import org.emftools.validation.builder.resourcedesc.ResourcedescPackage;
import org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class ResourcedescPackageImpl extends EPackageImpl implements
		ResourcedescPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass workspaceDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass resourceDescriptorEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass projectDescriptorEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.emftools.validation.builder.resourcedesc.ResourcedescPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ResourcedescPackageImpl() {
		super(eNS_URI, ResourcedescFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends.
	 * 
	 * <p>
	 * This method is used to initialize {@link ResourcedescPackage#eINSTANCE}
	 * when that field is accessed. Clients should not invoke it directly.
	 * Instead, they should simply access that field to obtain the package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ResourcedescPackage init() {
		if (isInited)
			return (ResourcedescPackage) EPackage.Registry.INSTANCE
					.getEPackage(ResourcedescPackage.eNS_URI);

		// Obtain or create and register package
		ResourcedescPackageImpl theResourcedescPackage = (ResourcedescPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof ResourcedescPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI) : new ResourcedescPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theResourcedescPackage.createPackageContents();

		// Initialize created meta-data
		theResourcedescPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theResourcedescPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ResourcedescPackage.eNS_URI,
				theResourcedescPackage);
		return theResourcedescPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getWorkspaceDescriptor() {
		return workspaceDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getWorkspaceDescriptor_Projects() {
		return (EReference) workspaceDescriptorEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getResourceDescriptor() {
		return resourceDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getResourceDescriptor_Uri() {
		return (EAttribute) resourceDescriptorEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getResourceDescriptor_ReferencedResources() {
		return (EReference) resourceDescriptorEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getResourceDescriptor_ReferrerResources() {
		return (EReference) resourceDescriptorEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getResourceDescriptor_Project() {
		return (EReference) resourceDescriptorEClass.getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getProjectDescriptor() {
		return projectDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getProjectDescriptor_Resources() {
		return (EReference) projectDescriptorEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getProjectDescriptor_Workspace() {
		return (EReference) projectDescriptorEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getProjectDescriptor_Name() {
		return (EAttribute) projectDescriptorEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourcedescFactory getResourcedescFactory() {
		return (ResourcedescFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		workspaceDescriptorEClass = createEClass(WORKSPACE_DESCRIPTOR);
		createEReference(workspaceDescriptorEClass,
				WORKSPACE_DESCRIPTOR__PROJECTS);

		resourceDescriptorEClass = createEClass(RESOURCE_DESCRIPTOR);
		createEAttribute(resourceDescriptorEClass, RESOURCE_DESCRIPTOR__URI);
		createEReference(resourceDescriptorEClass,
				RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES);
		createEReference(resourceDescriptorEClass,
				RESOURCE_DESCRIPTOR__REFERRER_RESOURCES);
		createEReference(resourceDescriptorEClass, RESOURCE_DESCRIPTOR__PROJECT);

		projectDescriptorEClass = createEClass(PROJECT_DESCRIPTOR);
		createEReference(projectDescriptorEClass, PROJECT_DESCRIPTOR__RESOURCES);
		createEReference(projectDescriptorEClass, PROJECT_DESCRIPTOR__WORKSPACE);
		createEAttribute(projectDescriptorEClass, PROJECT_DESCRIPTOR__NAME);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(workspaceDescriptorEClass, WorkspaceDescriptor.class,
				"WorkspaceDescriptor", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWorkspaceDescriptor_Projects(),
				this.getProjectDescriptor(),
				this.getProjectDescriptor_Workspace(), "projects", null, 0, -1,
				WorkspaceDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceDescriptorEClass, ResourceDescriptor.class,
				"ResourceDescriptor", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResourceDescriptor_Uri(), ecorePackage.getEString(),
				"uri", null, 0, 1, ResourceDescriptor.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getResourceDescriptor_ReferencedResources(),
				this.getResourceDescriptor(),
				this.getResourceDescriptor_ReferrerResources(),
				"referencedResources", null, 0, -1, ResourceDescriptor.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getResourceDescriptor_ReferrerResources(),
				this.getResourceDescriptor(),
				this.getResourceDescriptor_ReferencedResources(),
				"referrerResources", null, 0, -1, ResourceDescriptor.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getResourceDescriptor_Project(),
				this.getProjectDescriptor(),
				this.getProjectDescriptor_Resources(), "project", null, 1, 1,
				ResourceDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(resourceDescriptorEClass, ecorePackage.getEBoolean(),
				"hasReferences", 1, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(resourceDescriptorEClass, ecorePackage.getEBoolean(),
				"hasReferrers", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(projectDescriptorEClass, ProjectDescriptor.class,
				"ProjectDescriptor", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProjectDescriptor_Resources(),
				this.getResourceDescriptor(),
				this.getResourceDescriptor_Project(), "resources", null, 0, -1,
				ProjectDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProjectDescriptor_Workspace(),
				this.getWorkspaceDescriptor(),
				this.getWorkspaceDescriptor_Projects(), "workspace", null, 1,
				1, ProjectDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProjectDescriptor_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, ProjectDescriptor.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} // ResourcedescPackageImpl
