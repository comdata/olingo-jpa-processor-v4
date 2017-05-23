package com.sap.olingo.jpa.processor.core.filter;

import org.apache.olingo.server.api.uri.queryoption.expression.Member;

final class JPALambdaAnyOperation extends JPALambdaOperation implements JPAExpressionOperator {

  public JPALambdaAnyOperation(final JPAFilterComplierAccess jpaComplier, final Member member) {
    super(jpaComplier, member);
  }

  @Override
  public Enum<?> getOperator() {
    // TODO Auto-generated method stub
    return null;
  }

}
