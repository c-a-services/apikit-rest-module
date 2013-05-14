/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.apikit.rest.config;

import org.mule.config.spring.parsers.generic.ChildDefinitionParser;
import org.mule.module.apikit.rest.documentation.swagger.SwaggerResourceDocumentationStrategy;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class SwaggerDocumentationDefinitionParser extends ChildDefinitionParser
{

    public SwaggerDocumentationDefinitionParser()
    {
        super("documentationStrategy", SwaggerResourceDocumentationStrategy.class, true);
        addIgnored("enableConsole");
        addIgnored("consolePath");
    }

    @Override
    protected void parseChild(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
    {
        builder.addConstructorArgValue(element.getAttribute("enableConsole"));
        builder.addConstructorArgValue(element.getAttribute("consolePath"));
        super.parseChild(element, parserContext, builder);
    }

}