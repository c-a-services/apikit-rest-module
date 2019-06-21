/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit;

import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.runners.Parameterized;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.test.runner.ArtifactClassLoaderRunnerConfig;

import static java.util.Arrays.asList;
import static org.mule.module.apikit.api.RamlHandler.MULE_APIKIT_PARSER_PROPERTY;

//@RunnerDelegateTo(Parameterized.class)
@ArtifactClassLoaderRunnerConfig
public abstract class AbstractMultiParserFunctionalTestCase extends MuleArtifactFunctionalTestCase {

//  @Parameterized.Parameter(value = 0)
  public String parser = "RAML";

  @Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> data() {
    return asList(new Object[][] {
        {"RAML"},
        {"AMF"}
    });
  }

  @Rule
  public DynamicPort serverPort = new DynamicPort("serverPort");

  @Override
  public int getTestTimeoutSecs() {
    return 6000;
  }

  protected void doSetUpBeforeMuleContextCreation() {
    System.setProperty(MULE_APIKIT_PARSER_PROPERTY, parser);
  }

  @Override
  protected void doSetUp() throws Exception {
    RestAssured.port = serverPort.getNumber();
    super.doSetUp();
  }

  @AfterClass
  public static void afterClass() {
    System.clearProperty(MULE_APIKIT_PARSER_PROPERTY);
  }

  protected boolean isAmfParser() {
    return "AMF".equals(parser);
  }
}