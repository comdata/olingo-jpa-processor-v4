package com.sap.olingo.jpa.processor.core.modify;

import javax.persistence.EntityManager;

import org.apache.olingo.server.api.ODataRequest;

import com.sap.olingo.jpa.processor.core.exception.ODataJPAProcessException;
import com.sap.olingo.jpa.processor.core.processor.JPARequestEntity;

public interface JPACUDRequestHandler {
  /**
   * 
   * @param requestEntity
   * @param em
   * @throws ODataJPAProcessException
   */
  public void deleteEntity(final JPARequestEntity requestEntity, final EntityManager em)
      throws ODataJPAProcessException;

  /**
   * Hook to create a new entity. Transaction handling is done outside to guarantee transactional behavior of change
   * sets in batch requests.
   * 
   * @param et Metadata about the entity type that shall be created
   * @param jpaAttributes List of attributes with pojo attributes name and converted into JAVA types
   * @param em Instance of an entity manager.
   * @return The newly created instance or map of created attributes including default and added values
   * following the same rules as jpaAttributes
   * @throws ODataJPAProcessException
   */
  public Object createEntity(final JPARequestEntity requestEntity, final EntityManager em)
      throws ODataJPAProcessException;

  /**
   * <a href=
   * "http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752300"
   * >OData Version 4.0 Part 1 - 11.4.3 Update an Entity</a>
   * @param et Entity type that shall be created
   * @param jpaAttributes List of attributes with pojo attributes name and converted into JAVA types
   * @param keys List of keys defined in the URI with pojo attributes name and converted into JAVA types
   * @param em Entity manager
   * @param method Method (PUT/PATCH) used for update
   * @param header
   * @return The response describes the performed changes (Created or Updated) as well as the result of the operation.
   * It must not be null. Even if nothing was changed => update is idempotent
   * @throws ODataJPAProcessException
   */
  public JPAUpdateResult updateEntity(final JPARequestEntity requestEntity, final EntityManager em,
      final ODataRequest request) throws ODataJPAProcessException;
}
