/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.it.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xwiki.it.ui.elements.CreatePagePage;
import org.xwiki.it.ui.elements.CreateSpacePage;
import org.xwiki.it.ui.elements.HomePage;
import org.xwiki.it.ui.elements.ViewPage;
import org.xwiki.it.ui.elements.WYSIWYGEditPage;
import org.xwiki.it.ui.framework.AbstractAdminAuthenticatedTest;
import org.xwiki.it.ui.framework.TestUtils;

/**
 * Validates the support for non-ASCII characters.
 * 
 * @version $Id$
 * @since 2.3M1
 */
public class InternationalizationTest extends AbstractAdminAuthenticatedTest
{
    @Before
    public void setUp()
    {
        super.setUp();

        TestUtils.deletePage("\u0219", "WebHome", getDriver());
        TestUtils.deletePage("Main", "\u0219", getDriver());
    }

    /**
     * Checks that non-ASCII characters are allowed in the space name.
     */
    @Test
    public void testCreateNonAsciiSpace()
    {
        HomePage homePage = new HomePage(getDriver());
        homePage.gotoHomePage();

        CreateSpacePage createSpacePage = homePage.createSpace();
        WYSIWYGEditPage editPage = createSpacePage.createSpace("\u0219");

        // Verify the title field
        Assert.assertEquals("\u0219", editPage.getDocumentTitle());

        // Verify the document space in the metadata
        Assert.assertEquals("\u0219", editPage.getMetaDataValue("space"));

        // Save the space to verify it can be saved with a non-ascii name
        ViewPage savedPage = editPage.save();
        Assert.assertEquals("\u0219", savedPage.getMetaDataValue("space"));
    }

    /**
     * Checks that non-ASCII characters are allowed in the page name.
     */
    @Test
    public void testCreateNonAsciiPage()
    {
        HomePage homePage = new HomePage(getDriver());
        homePage.gotoHomePage();

        CreatePagePage createPagePage = homePage.createPage();
        WYSIWYGEditPage editPage = createPagePage.createPage("Main", "\u0219");

        // Verify the title field
        Assert.assertEquals("\u0219", editPage.getDocumentTitle());

        // Verify the document name in the metadata
        Assert.assertEquals("\u0219", editPage.getMetaDataValue("page"));

        // Save the page to verify it can be saved with a non-ascii name
        ViewPage savedPage = editPage.save();
        Assert.assertEquals("\u0219", savedPage.getMetaDataValue("page"));
    }
}