package com.sdl.selenium.bootstrap.form;

import com.sdl.selenium.web.By;
import com.sdl.selenium.web.WebLocator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UneditableInputTest {
    public static WebLocator container = new WebLocator("container");

    @DataProvider
    public static Object[][] testConstructorPathDataProvider() {
        return new Object[][]{
                {new UneditableInput(),                                       "//span[contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
                {new UneditableInput().setId("ID"),                           "//span[@id='ID' and contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
                {new UneditableInput(container),                              "//*[contains(concat(' ', @class, ' '), ' container ')]//span[contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
                {new UneditableInput(container).setElPath("//*[contains(text(), 'Register')]"), "//*[contains(concat(' ', @class, ' '), ' container ')]//*[contains(text(), 'Register')]"},
                {new UneditableInput(container, "TextFieldText"),             "//*[contains(concat(' ', @class, ' '), ' container ')]//label[text()='TextFieldText']//following-sibling::*//span[contains(concat(' ', @class, ' '), ' uneditable-input ')]"},

                {new UneditableInput(By.id("ID")),                           "//span[@id='ID' and contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
                {new UneditableInput(By.container(container)),                              "//*[contains(concat(' ', @class, ' '), ' container ')]//span[contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
                {new UneditableInput(By.container(container), By.xpath("//*[contains(text(), 'Register')]")), "//*[contains(concat(' ', @class, ' '), ' container ')]//*[contains(text(), 'Register')]"},
                {new UneditableInput(By.container(container), By.label("TextFieldText")),             "//*[contains(concat(' ', @class, ' '), ' container ')]//label[text()='TextFieldText']//following-sibling::*//span[contains(concat(' ', @class, ' '), ' uneditable-input ')]"},
        };
    }

    @Test(dataProvider = "testConstructorPathDataProvider")
    public void getPathSelectorCorrectlyFromConstructors(UneditableInput uneditableInput, String expectedXpath) {
        Assert.assertEquals(uneditableInput.getPath(), expectedXpath);
    }
}
