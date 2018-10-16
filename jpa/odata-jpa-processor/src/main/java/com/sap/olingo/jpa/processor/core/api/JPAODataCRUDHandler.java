package com.sap.olingo.jpa.processor.core.api;

import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpMethod;

import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAInvocationTargetException;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAProcessException;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAProcessorException;
import com.sap.olingo.jpa.processor.core.modify.JPAUpdateResult;
import com.sap.olingo.jpa.processor.core.processor.JPARequestEntity;
import com.sap.olingo.jpa.processor.core.processor.JPARequestLink;

public final class JPAODataCRUDHandler extends JPAODataGetHandler {

  public JPAODataCRUDHandler(String pUnit) throws ODataException {
    super(pUnit);
    getJPAODataContext().setCUDRequestHandler(new JPADefaultCUDRequestHandler());
  }

  public JPAODataCRUDHandler(final String pUnit, final DataSource ds) throws ODataException {
    super(pUnit, ds);
    getJPAODataContext().setCUDRequestHandler(new JPADefaultCUDRequestHandler());
  }

  @Override
  public JPAODataCRUDContext getJPAODataContext() {
    return (JPAODataCRUDContext) super.getJPAODataContext();
  }

  class JPADefaultCUDRequestHandler extends JPAAbstractCUDRequestHandler {

	  @Override
	  public JPAUpdateResult updateEntity(final JPARequestEntity requestEntity, final EntityManager em,
			final HttpMethod method) throws ODataJPAProcessException {
			
	    if (method == HttpMethod.PATCH || method == HttpMethod.DELETE) {
	      final Object instance = em.find(requestEntity.getEntityType().getTypeClass(), requestEntity.getModifyUtil()
	          .createPrimaryKey(requestEntity.getEntityType(), requestEntity.getKeys(), requestEntity.getEntityType()));
	      requestEntity.getModifyUtil().setAttributesDeep(requestEntity.getData(), instance, requestEntity.getEntityType());
	      updateLinks(requestEntity, em, instance);
	      return new JPAUpdateResult(false, instance);
	    }
	    return super.updateEntity(requestEntity, em, method);
	  }

	  private void updateLinks(final JPARequestEntity requestEntity, final EntityManager em, final Object instance)
	      throws ODataJPAProcessorException, ODataJPAInvocationTargetException {
	    if (requestEntity.getRelationLinks() != null) {
	      for (Entry<JPAAssociationPath, List<JPARequestLink>> links : requestEntity.getRelationLinks().entrySet()) {
	        for (JPARequestLink link : links.getValue()) {
	          final Object related = em.find(link.getEntityType().getTypeClass(), requestEntity.getModifyUtil()
	              .createPrimaryKey(link.getEntityType(), link.getRelatedKeys(), link.getEntityType()));
	          requestEntity.getModifyUtil().linkEntities(instance, related, links.getKey());
	        }
	      }
	    }
	  }
  }
}
