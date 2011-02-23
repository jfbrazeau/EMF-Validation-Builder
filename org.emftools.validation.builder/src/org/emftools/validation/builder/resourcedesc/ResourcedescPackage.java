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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.emftools.validation.builder.resourcedesc.ResourcedescFactory
 * @model kind="package"
 * @generated
 */
public interface ResourcedescPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "resourcedesc";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://org.emftools.validation.builder.resourcedesc/1.0";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "resourcedesc";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	ResourcedescPackage eINSTANCE = org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.emftools.validation.builder.resourcedesc.impl.WorkspaceDescriptorImpl
	 * <em>Workspace Descriptor</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.emftools.validation.builder.resourcedesc.impl.WorkspaceDescriptorImpl
	 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getWorkspaceDescriptor()
	 * @generated
	 */
	int WORKSPACE_DESCRIPTOR = 0;

	/**
	 * The feature id for the '<em><b>Projects</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int WORKSPACE_DESCRIPTOR__PROJECTS = 0;

	/**
	 * The number of structural features of the '<em>Workspace Descriptor</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int WORKSPACE_DESCRIPTOR_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '
	 * {@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl
	 * <em>Resource Descriptor</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl
	 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getResourceDescriptor()
	 * @generated
	 */
	int RESOURCE_DESCRIPTOR = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DESCRIPTOR__URI = 0;

	/**
	 * The feature id for the '<em><b>Referenced Resources</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES = 1;

	/**
	 * The feature id for the '<em><b>Referrer Resources</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DESCRIPTOR__REFERRER_RESOURCES = 2;

	/**
	 * The feature id for the '<em><b>Project</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DESCRIPTOR__PROJECT = 3;

	/**
	 * The number of structural features of the '<em>Resource Descriptor</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RESOURCE_DESCRIPTOR_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '
	 * {@link org.emftools.validation.builder.resourcedesc.impl.ProjectDescriptorImpl
	 * <em>Project Descriptor</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.emftools.validation.builder.resourcedesc.impl.ProjectDescriptorImpl
	 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getProjectDescriptor()
	 * @generated
	 */
	int PROJECT_DESCRIPTOR = 2;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTOR__RESOURCES = 0;

	/**
	 * The feature id for the '<em><b>Workspace</b></em>' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTOR__WORKSPACE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTOR__NAME = 2;

	/**
	 * The number of structural features of the '<em>Project Descriptor</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PROJECT_DESCRIPTOR_FEATURE_COUNT = 3;

	/**
	 * Returns the meta object for class '
	 * {@link org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor
	 * <em>Workspace Descriptor</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Workspace Descriptor</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor
	 * @generated
	 */
	EClass getWorkspaceDescriptor();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor#getProjects
	 * <em>Projects</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Projects</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.WorkspaceDescriptor#getProjects()
	 * @see #getWorkspaceDescriptor()
	 * @generated
	 */
	EReference getWorkspaceDescriptor_Projects();

	/**
	 * Returns the meta object for class '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor
	 * <em>Resource Descriptor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Resource Descriptor</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor
	 * @generated
	 */
	EClass getResourceDescriptor();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getUri
	 * <em>Uri</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getUri()
	 * @see #getResourceDescriptor()
	 * @generated
	 */
	EAttribute getResourceDescriptor_Uri();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferencedResources
	 * <em>Referenced Resources</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Referenced Resources</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferencedResources()
	 * @see #getResourceDescriptor()
	 * @generated
	 */
	EReference getResourceDescriptor_ReferencedResources();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferrerResources
	 * <em>Referrer Resources</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the reference list '
	 *         <em>Referrer Resources</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getReferrerResources()
	 * @see #getResourceDescriptor()
	 * @generated
	 */
	EReference getResourceDescriptor_ReferrerResources();

	/**
	 * Returns the meta object for the container reference '
	 * {@link org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject
	 * <em>Project</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Project</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ResourceDescriptor#getProject()
	 * @see #getResourceDescriptor()
	 * @generated
	 */
	EReference getResourceDescriptor_Project();

	/**
	 * Returns the meta object for class '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor
	 * <em>Project Descriptor</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for class '<em>Project Descriptor</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ProjectDescriptor
	 * @generated
	 */
	EClass getProjectDescriptor();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getResources
	 * <em>Resources</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Resources</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getResources()
	 * @see #getProjectDescriptor()
	 * @generated
	 */
	EReference getProjectDescriptor_Resources();

	/**
	 * Returns the meta object for the container reference '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getWorkspace
	 * <em>Workspace</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '<em>Workspace</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getWorkspace()
	 * @see #getProjectDescriptor()
	 * @generated
	 */
	EReference getProjectDescriptor_Workspace();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.emftools.validation.builder.resourcedesc.ProjectDescriptor#getName()
	 * @see #getProjectDescriptor()
	 * @generated
	 */
	EAttribute getProjectDescriptor_Name();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ResourcedescFactory getResourcedescFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.emftools.validation.builder.resourcedesc.impl.WorkspaceDescriptorImpl
		 * <em>Workspace Descriptor</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.emftools.validation.builder.resourcedesc.impl.WorkspaceDescriptorImpl
		 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getWorkspaceDescriptor()
		 * @generated
		 */
		EClass WORKSPACE_DESCRIPTOR = eINSTANCE.getWorkspaceDescriptor();

		/**
		 * The meta object literal for the '<em><b>Projects</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference WORKSPACE_DESCRIPTOR__PROJECTS = eINSTANCE
				.getWorkspaceDescriptor_Projects();

		/**
		 * The meta object literal for the '
		 * {@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl
		 * <em>Resource Descriptor</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl
		 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getResourceDescriptor()
		 * @generated
		 */
		EClass RESOURCE_DESCRIPTOR = eINSTANCE.getResourceDescriptor();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RESOURCE_DESCRIPTOR__URI = eINSTANCE
				.getResourceDescriptor_Uri();

		/**
		 * The meta object literal for the '<em><b>Referenced Resources</b></em>
		 * ' reference list feature. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @generated
		 */
		EReference RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES = eINSTANCE
				.getResourceDescriptor_ReferencedResources();

		/**
		 * The meta object literal for the '<em><b>Referrer Resources</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RESOURCE_DESCRIPTOR__REFERRER_RESOURCES = eINSTANCE
				.getResourceDescriptor_ReferrerResources();

		/**
		 * The meta object literal for the '<em><b>Project</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RESOURCE_DESCRIPTOR__PROJECT = eINSTANCE
				.getResourceDescriptor_Project();

		/**
		 * The meta object literal for the '
		 * {@link org.emftools.validation.builder.resourcedesc.impl.ProjectDescriptorImpl
		 * <em>Project Descriptor</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.emftools.validation.builder.resourcedesc.impl.ProjectDescriptorImpl
		 * @see org.emftools.validation.builder.resourcedesc.impl.ResourcedescPackageImpl#getProjectDescriptor()
		 * @generated
		 */
		EClass PROJECT_DESCRIPTOR = eINSTANCE.getProjectDescriptor();

		/**
		 * The meta object literal for the '<em><b>Resources</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PROJECT_DESCRIPTOR__RESOURCES = eINSTANCE
				.getProjectDescriptor_Resources();

		/**
		 * The meta object literal for the '<em><b>Workspace</b></em>' container
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference PROJECT_DESCRIPTOR__WORKSPACE = eINSTANCE
				.getProjectDescriptor_Workspace();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute PROJECT_DESCRIPTOR__NAME = eINSTANCE
				.getProjectDescriptor_Name();

	}

} // ResourcedescPackage
