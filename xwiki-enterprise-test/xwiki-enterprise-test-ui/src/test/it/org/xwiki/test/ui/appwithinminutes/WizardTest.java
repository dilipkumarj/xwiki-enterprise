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
package org.xwiki.test.ui.appwithinminutes;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.xwiki.test.po.AbstractTest;
import org.xwiki.test.po.appwithinminutes.AppWithinMinutesHomePage;
import org.xwiki.test.po.appwithinminutes.ApplicationClassEditPage;
import org.xwiki.test.po.appwithinminutes.ApplicationCreatePage;
import org.xwiki.test.po.appwithinminutes.ApplicationHomeEditPage;
import org.xwiki.test.po.appwithinminutes.ApplicationHomePage;
import org.xwiki.test.po.appwithinminutes.ClassFieldEditPane;
import org.xwiki.test.po.appwithinminutes.EntryEditPage;
import org.xwiki.test.po.appwithinminutes.EntryNamePane;
import org.xwiki.test.po.platform.LiveTableElement;

/**
 * Tests the App Within Minutes wizard.
 * 
 * @version $Id$
 * @since 3.3
 */
public class WizardTest extends AbstractTest
{
    /**
     * Tests the application creation process from start to end.
     */
    @Test
    public void testCreateApplication()
    {
        // Register a simple user, login and go to the App Within Minutes home page.
        String userName = RandomStringUtils.randomAlphanumeric(5);
        String password = RandomStringUtils.randomAlphanumeric(6);
        AppWithinMinutesHomePage appWithinMinutesHomePage = new AppWithinMinutesHomePage();
        getUtil().registerLoginAndGotoPage(userName, password, appWithinMinutesHomePage.getURL());

        // Click the Create Application button.
        ApplicationCreatePage appCreatePage = appWithinMinutesHomePage.clickCreateApplication();

        // Step 1
        // Enter the application name (random name).
        String appName = RandomStringUtils.randomAlphabetic(6);
        appCreatePage.setApplicationName(appName);

        // Wait for the preview.
        appCreatePage.waitForApplicationNamePreview();

        // Move to the next step.
        ApplicationClassEditPage classEditPage = appCreatePage.clickNextStep();

        // Step 2
        // Add a 'Short Text' field.
        ClassFieldEditPane fieldEditPane = classEditPage.addField("Short Text");

        // Set the field pretty name and default value
        fieldEditPane.setPrettyName("City Name");
        fieldEditPane.setDefaultValue("Paris");

        // Move to the next step.
        ApplicationHomeEditPage homeEditPage = classEditPage.clickNextStep();

        // Move back to the second step.
        classEditPage = homeEditPage.clickPreviousStep();

        // Open the configuration panel and set the field name
        fieldEditPane = new ClassFieldEditPane("shortText1");
        fieldEditPane.openConfigPanel();
        fieldEditPane.setName("cityName");

        // Move to the next step.
        homeEditPage = classEditPage.clickNextStep();

        // Step 3
        // Enter the application description.
        String appDescription = "Simple application to manage data about various cities";
        homeEditPage.setDescription(appDescription);

        // Add the Short Text field from the previous step to the list of columns.
        homeEditPage.addLiveTableColumn("City Name");

        // Click the finish button which should lead us to the application home page.
        ApplicationHomePage homePage = homeEditPage.clickFinish();

        // Assert the application description is present.
        Assert.assertTrue(homePage.getContent().contains(appDescription));

        // Add a new entry.
        String firstEntryName = RandomStringUtils.randomAlphanumeric(6);
        EntryNamePane entryNamePane = homePage.clickAddNewEntry();
        entryNamePane.setName(firstEntryName);
        EntryEditPage entryEditPage = entryNamePane.clickAdd();

        // Assert the pretty name and the default value of the Short Text field.
        // Apparently WebElement#getText() takes into account the text-transform CSS property.
        Assert.assertEquals("CITY NAME", entryEditPage.getLabel("cityName"));
        Assert.assertEquals("Paris", entryEditPage.getValue("cityName"));

        // Change the field value.
        entryEditPage.setValue("cityName", "London");

        // Save and go back to the application home page.
        String appHomePageTitle = appName + " Home";
        entryEditPage.clickSaveAndView().clickBreadcrumbLink(appHomePageTitle);
        homePage = new ApplicationHomePage();

        // Assert the entry we have just created is listed in the live table.
        LiveTableElement entriesLiveTable = homePage.getEntriesLiveTable();
        entriesLiveTable.waitUntilReady();
        Assert.assertTrue(entriesLiveTable.hasRow("City Name", "London"));

        // Click the edit button.
        homePage.edit();
        homeEditPage = new ApplicationHomeEditPage();

        // Change the application description.
        appDescription = "The best app!";
        homeEditPage.setDescription(appDescription);

        // Remove one of the live table columns.
        homeEditPage.removeLiveTableColumn("Actions");

        // Save
        homePage = homeEditPage.clickSaveAndView();

        // Assert that the application description has changed and that the column has been removed.
        Assert.assertTrue(homePage.getContent().contains(appDescription));
        entriesLiveTable = homePage.getEntriesLiveTable();
        entriesLiveTable.waitUntilReady();
        Assert.assertFalse(entriesLiveTable.hasColumn("Actions"));

        // Click the link to edit the application class.
        classEditPage = homePage.clickEditClass();

        // Drag a Number field.
        fieldEditPane = classEditPage.addField("Number");

        // Set the field pretty name.
        fieldEditPane.setPrettyName("Population Size");

        // Save and go back to the application home page.
        classEditPage.clickSaveAndView().clickBreadcrumbLink(appHomePageTitle);
        homePage = new ApplicationHomePage();

        // Add a new entry.
        String secondEntryName = RandomStringUtils.randomAlphanumeric(6);
        entryNamePane = homePage.clickAddNewEntry();
        entryNamePane.setName(secondEntryName);
        entryEditPage = entryNamePane.clickAdd();

        // Assert the new field is displayed in the edit sheet (field name was auto-generated).
        // Apparently WebElement#getText() takes into account the text-transform CSS property.
        Assert.assertEquals("POPULATION SIZE", entryEditPage.getLabel("number1"));

        // Save and go back to the application home page.
        entryEditPage.clickSaveAndView().clickBreadcrumbLink(appHomePageTitle);
        homePage = new ApplicationHomePage();

        // Assert both entries are displayed in the live table.
        entriesLiveTable = homePage.getEntriesLiveTable();
        entriesLiveTable.waitUntilReady();
        Assert.assertTrue(entriesLiveTable.hasRow("Page name", firstEntryName));
        Assert.assertTrue(entriesLiveTable.hasRow("Page name", secondEntryName));

        // Go to the App Within Minutes home page.
        appWithinMinutesHomePage = AppWithinMinutesHomePage.gotoPage();

        // Assert that the created application is listed in the live table.
        LiveTableElement appsLiveTable = appWithinMinutesHomePage.getAppsLiveTable();
        appsLiveTable.waitUntilReady();
        Assert.assertTrue(appsLiveTable.hasRow("Application name", appName));
    }
}
