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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emftools.validation.builder.resourcedesc.ProjectDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourceDescriptor;
import org.emftools.validation.builder.resourcedesc.ResourcedescPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl#getReferencedResources <em>Referenced Resources</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl#getReferrerResources <em>Referrer Resources</em>}</li>
 *   <li>{@link org.emftools.validation.builder.resourcedesc.impl.ResourceDescriptorImpl#getProject <em>Project</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceDescriptorImpl extends EObjectImpl implements ResourceDescriptor {
	/**
	 * The default value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected String uri = URI_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferencedResources() <em>Referenced Resources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedResources()
	 * @generated
	 * @ordered
	 */
	protected EList<ResourceDescriptor> referencedResources;

	/**
	 * The cached value of the '{@link #getReferrerResources() <em>Referrer Resources</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferrerResources()
	 * @generated
	 * @ordered
	 */
	protected EList<ResourceDescriptor> referrerResources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResourceDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ResourcedescPackage.Literals.RESOURCE_DESCRIPTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setUri(String newUri) {
		if (uri != null) {
			throw new IllegalArgumentException("Resource URI can not be set more than one time");
		}
		else if (newUri == null || newUri.trim().equals("")) {
			throw new IllegalArgumentException("Resource URI can not be empty");
		}
		String oldUri = uri;
		uri = newUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourcedescPackage.RESOURCE_DESCRIPTOR__URI, oldUri, uri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResourceDescriptor> getReferencedResources() {
		if (referencedResources == null) {
			referencedResources = new EObjectWithInverseResolvingEList.ManyInverse<ResourceDescriptor>(ResourceDescriptor.class, this, ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES, ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES);
		}
		return referencedResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ResourceDescriptor> getReferrerResources() {
		if (referrerResources == null) {
			referrerResources = new EObjectWithInverseResolvingEList.ManyInverse<ResourceDescriptor>(ResourceDescriptor.class, this, ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES, ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES);
		}
		return referrerResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectDescriptor getProject() {
		if (eContainerFeatureID() != ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT) return null;
		return (ProjectDescriptor)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectDescriptor basicGetProject() {
		if (eContainerFeatureID() != ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT) return null;
		return (ProjectDescriptor)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProject(ProjectDescriptor newProject, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newProject, ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProject(ProjectDescriptor newProject) {
		if (newProject != eInternalContainer() || (eContainerFeatureID() != ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT && newProject != null)) {
			if (EcoreUtil.isAncestor(this, newProject))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newProject != null)
				msgs = ((InternalEObject)newProject).eInverseAdd(this, ResourcedescPackage.PROJECT_DESCRIPTOR__RESOURCES, ProjectDescriptor.class, msgs);
			msgs = basicSetProject(newProject, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT, newProject, newProject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean hasReferences() {
		return getReferencedResources().size() > 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean hasReferrers() {
		return getReferrerResources().size() > 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferencedResources()).basicAdd(otherEnd, msgs);
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferrerResources()).basicAdd(otherEnd, msgs);
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetProject((ProjectDescriptor)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				return ((InternalEList<?>)getReferencedResources()).basicRemove(otherEnd, msgs);
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				return ((InternalEList<?>)getReferrerResources()).basicRemove(otherEnd, msgs);
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				return basicSetProject(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				return eInternalContainer().eInverseRemove(this, ResourcedescPackage.PROJECT_DESCRIPTOR__RESOURCES, ProjectDescriptor.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__URI:
				return getUri();
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				return getReferencedResources();
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				return getReferrerResources();
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				if (resolve) return getProject();
				return basicGetProject();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__URI:
				setUri((String)newValue);
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				getReferencedResources().clear();
				getReferencedResources().addAll((Collection<? extends ResourceDescriptor>)newValue);
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				getReferrerResources().clear();
				getReferrerResources().addAll((Collection<? extends ResourceDescriptor>)newValue);
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				setProject((ProjectDescriptor)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__URI:
				setUri(URI_EDEFAULT);
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				getReferencedResources().clear();
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				getReferrerResources().clear();
				return;
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				setProject((ProjectDescriptor)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__URI:
				return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERENCED_RESOURCES:
				return referencedResources != null && !referencedResources.isEmpty();
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__REFERRER_RESOURCES:
				return referrerResources != null && !referrerResources.isEmpty();
			case ResourcedescPackage.RESOURCE_DESCRIPTOR__PROJECT:
				return basicGetProject() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (uri: ");
		result.append(uri);
		result.append(')');
		return result.toString();
	}

} //ResourceDescriptorImpl
