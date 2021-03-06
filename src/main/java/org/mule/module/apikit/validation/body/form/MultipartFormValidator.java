/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.validation.body.form;


import org.mule.apikit.model.parameter.Parameter;
import org.mule.module.apikit.StreamUtils;
import org.mule.module.apikit.api.exception.InvalidFormParameterException;
import org.mule.module.apikit.validation.body.form.transformation.MultipartFormData;
import org.mule.module.apikit.validation.body.form.transformation.MultipartFormDataParameter;
import org.mule.runtime.api.metadata.TypedValue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MultipartFormValidator implements FormValidator<TypedValue> {

  Map<String, List<Parameter>> formParameters;

  public MultipartFormValidator(Map<String, List<Parameter>> formParameters) {
    this.formParameters = formParameters;
  }

  @Override
  public TypedValue validate(TypedValue originalPayload) throws InvalidFormParameterException {
    final InputStream inputStream = StreamUtils.unwrapCursorStream(originalPayload.getValue());
    final String boundary = getBoundary(originalPayload);
    MultipartFormData multipartFormData = new MultipartFormData(inputStream, boundary);
    Map<String, MultipartFormDataParameter> actualParameters = multipartFormData.getFormDataParameters();

    for (String expectedKey : formParameters.keySet()) {
      List<Parameter> params = formParameters.get(expectedKey);
      if (params != null && params.size() == 1){
        Parameter expected = params.get(0);
        if (actualParameters.containsKey(expectedKey)) {
          MultipartFormDataParameter multipartFormDataParameter = actualParameters.get(expectedKey);
          multipartFormDataParameter.validate(expected);
        } else {
          if (expected.getDefaultValue() != null) {
            multipartFormData.addDefault(expectedKey,expected.getDefaultValue());
          } else if (expected.isRequired()) {
            throw new InvalidFormParameterException("Required form parameter " + expectedKey + " not specified");
          }
        }
      }
    }

    return TypedValue.of(multipartFormData.build());
  }

  private String getBoundary(TypedValue originalPayload) throws InvalidFormParameterException {
    String boundary = originalPayload.getDataType().getMediaType().getParameter("boundary");
    if(boundary == null){
      throw new InvalidFormParameterException("Required boundary parameter not found");
    }
    return boundary;
  }

}
