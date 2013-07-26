package org.mule.module.apikit;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.port;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import com.jayway.restassured.RestAssured;

import org.junit.Rule;
import org.junit.Test;

public class ArchetypeConfigTestCase extends FunctionalTestCase
{

    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    public int getTestTimeoutSecs()
    {
        return 6000;
    }

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/apikit/archetype/archetype-config.xml";
    }

    @Test
    public void console() throws Exception
    {
        given().header("Accept", "text/html")
                .expect()
                .response().body(allOf(containsString("<title>api:Console</title>"),
                                       containsString("src=\"http://localhost:" + port + "/api\"")))
                .header("Content-type", "text/html").statusCode(200)
                .when().get("/api/console/index.html");
    }

    @Test
    public void getOnResourcesJson() throws Exception
    {
        given().header("Accept", "application/json")
                .expect()
                .response().body(containsString("\"name\": \"Barcelona\","))
                .header("Content-type", "application/json").statusCode(200)
                .when().get("/api/resources");
    }

}
