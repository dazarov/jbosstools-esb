/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.esb.ui.editor.form;

import java.util.*;

import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.model.util.ClassLoaderUtil;
import org.jboss.tools.common.model.ui.forms.*;
import org.jboss.tools.esb.core.model.ESBConstants;

/**
 * @author Viacheslav Kabanovich
 */
public class ESBXMLFormLayoutData implements IFormLayoutData, ESBConstants {
	static {
		ClassLoaderUtil.init();
	}
	
	public static String EMPTY_DESCRIPTION = ""; //$NON-NLS-1$

	private final static IFormData[] FORM_LAYOUT_DEFINITIONS = new IFormData[] {

		
	};

	private static Map<String,IFormData> FORM_LAYOUT_DEFINITION_MAP = Collections.synchronizedMap(new ArrayToMap(FORM_LAYOUT_DEFINITIONS));
	
	private static ESBXMLFormLayoutData INSTANCE = new ESBXMLFormLayoutData();
	
	public static IFormLayoutData getInstance() {
		return INSTANCE;
	}
	
	private ESBXMLFormLayoutData() {}

	public IFormData getFormData(String entityName) {
		IFormData data = (IFormData)FORM_LAYOUT_DEFINITION_MAP.get(entityName);
		if(data == null) {
			data = generateDefaultFormData(entityName);
		}
		return data;
	}
	
	private IFormData generateDefaultFormData(String entityName) {
		IFormData data = null;
		XModelEntity entity = XModelMetaDataImpl.getInstance().getEntity(entityName);
		if(entity != null) {
			data = generateDefaultFormData(entity);
		}
		if(data != null) {
			FORM_LAYOUT_DEFINITION_MAP.put(entityName, data);
		}
		return data;		
	}
	
	private IFormData generateDefaultFormData(XModelEntity entity) {
		String entityName = entity.getName();
		List<IFormData> list = new ArrayList<IFormData>();
		IFormData g = ModelFormLayoutData.createGeneralFormData(entity);
		if(g != null) list.add(g);
		if(entityName.startsWith(PREACTION_PREFIX)) {
			if(entity.getChild(ENT_ESB_ROUTE_TO) != null) {
				list.add(ESBListsFormLayoutData.ESB_ROUTE_LIST_DEFINITION);
			}
			//do nothing; when specific children exist use specific forms
		} else if(entity.getChild(ENT_ESB_PROPERTY) != null) {
			list.add(ESBListsFormLayoutData.ESB_PROPERTY_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_LISTENERS)) {
			list.add(ESBListsFormLayoutData.ESB_LISTENER_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_ACTIONS)) {
			list.add(ESBListsFormLayoutData.ESB_ACTION_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_SERVICES)) {
			list.add(ESBListsFormLayoutData.ESB_SERVICE_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_SERVICE)) {
			list.add(ESBListsFormLayoutData.ESB_LISTENER_SUB_LIST_DEFINITION);
			list.add(ESBListsFormLayoutData.ESB_ACTION_SUB_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_PROVIDERS)) {
			list.add(ESBListsFormLayoutData.ESB_PROVIDER_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_FILE_101)) {
			list.add(ESBListsFormLayoutData.ESB_PROVIDER_SUB_LIST_DEFINITION);
			list.add(ESBListsFormLayoutData.ESB_SERVICE_SUB_LIST_DEFINITION);
		} else if(entityName.equals(ENT_ESB_PROPERTY)) {
			list.add(ModelFormLayoutData.TAG_LIST);
		}
		if(entityName.equals(ENT_ESB_SCHEDULE_PROVIDER)) {
			list.add(ESBListsFormLayoutData.ESB_SCHEDULE_LIST_DEFINITION);
		}
		if(entity.getChild(ENT_ESB_BUS) != null) {
			list.add(ESBListsFormLayoutData.ESB_BUS_LIST_DEFINITION);
		}
		IFormData a = ModelFormLayoutData.createAdvancedFormData(entityName);
		if(a != null) list.add(a);
		IFormData[] ds = list.toArray(new IFormData[0]);
		IFormData data = new FormData(entityName, new String[]{null}, ds);
		return data;
	}

}