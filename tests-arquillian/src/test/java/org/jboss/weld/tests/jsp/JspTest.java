/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tests.jsp;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.tests.category.Integration;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Nicklas Karlsson
 * @author Dan Allen
 */
@Category(Integration.class)
@RunWith(Arquillian.class)
@Run(RunModeType.AS_CLIENT)
public class JspTest
{
   @Deployment
   public static WebArchive createDeployment() 
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
               .addWebResource(JspTest.class.getPackage(), "web.xml", "web.xml")
               .addWebResource(JspTest.class.getPackage(), "faces-config.xml", "faces-config.xml")
               .addResource(JspTest.class.getPackage(), "index.jsp", "index.jsp")
               .addResource(JspTest.class.getPackage(), "home.jspx", "home.jspx")
               .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void testConversationPropagationToNonExistentConversationLeadsException() throws Exception
   {
      WebClient client = new WebClient();
      client.setThrowExceptionOnFailingStatusCode(false);
      Page page = client.getPage(getPath("/index.jsp"));

      Assert.assertEquals(200, page.getWebResponse().getStatusCode());
      Assert.assertTrue(page.getWebResponse().getRequestUrl().toString().contains("home.jsf"));
   }

   protected String getPath(String page)
   {
      // TODO: this should be moved out and be handled by Arquillian
      return "http://localhost:8080/test/" + page;
   }
}