package com.sdl.selenium.extjs4.window;

import com.sdl.selenium.web.WebLocator;
import com.sdl.selenium.web.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window extends WebLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class);

    public Window() {
        setClassName("Window");
        setBaseCls("x-window");
        setTemplate("title", "count(*[contains(@class,'x-window-header') or contains(@class, '-tl')]//*[text()='%s']) > 0");
    }

    public Window(String title) {
        this();
        setTitle(title);
    }

    public String getTitleWindow() {
        ready();
        WebLocator locator = new WebLocator(this).setClasses("x-window-header-text");
        return locator.getHtmlText();
    }

    /**
     * click on element with class "x-tool-" + id
     * @param id element
     * @return true | false
     */
    public boolean clickOnTool(String id) {
        WebLocator toolElement = getToolElement(id).setVisibility(true);
        return toolElement.click();
    }

    public boolean close() {
        boolean closed = clickOnTool("close");
        if (closed) {
            Utils.sleep(50);
        }
        return closed;
    }

    public boolean maximize() {
        boolean isMaximized = isMaximized();
        boolean maximized = isMaximized || clickOnTool("maximize");
        if (!isMaximized && maximized) {
            Utils.sleep(50);
        }
        return maximized;
    }

    public boolean isMaximized() {
        WebLocator maximizeTool = getToolElement("maximize");
        return !maximizeTool.isVisible();
    }

    private WebLocator getToolElement(String id) {
        return new WebLocator(this).setClasses("x-tool-" + id);
    }

    public boolean restore() {
        return clickOnTool("restore");
    }

    public boolean minimize() {
        return clickOnTool("minimize");
    }

    public boolean toggle() {
        return clickOnTool("toggle");
    }
}