
package org.mule.module.apikit.rest.resource;

import static com.jayway.restassured.RestAssured.given;

import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.util.IOUtils;

import com.jayway.restassured.RestAssured;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

public class BaseResourceSwaggerFunctionalTestCase extends FunctionalTestCase
{

    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/wsapi/rest/service-config.xml, org/mule/module/wsapi/test-flows-config.xml";
    }

    @Test
    public void getHtml() throws Exception
    {
        given().header("Accept", "text/html").expect().response().statusCode(200).when().get("/api");
    }

    @Test
    public void getHtmlResources() throws Exception
    {
        given().header("Accept", "text/javascript")
            .expect()
            .response()
            .statusCode(200)
            .body(
                Matchers.equalTo(IOUtils.getResourceAsString(
                    "org/mule/module/wsapi/rest/swagger/lib/swagger.js", this.getClass())))
            .when()
            .get("/api/_swagger/lib/swagger.js");
    }

    @Test
    public void baseUriGetSwaggerJson() throws Exception
    {
        given().header("Accept", "application/swagger+json")
            .expect()
            .response()
            .statusCode(200)
            .body(
                Matchers.equalTo("{\"apiVersion\":\"1.0\",\"swaggerVersion\":\"1.0\",\"basePath\":\"http://localhost:"
                                 + serverPort.getNumber()
                                 + "/api\",\"apis\":[{\"path\":\"/leagues\",\"description\":\"\"}]}"))
            .when()
            .get("/api");
    }

}