package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import ru.scrait.parser.interfaces.IInitService;

/**
 * Здесь описываются все действия с элементами сайта
 *
 * @author Scrait
 * @since 01/10/2023
 */
@Service
@RequiredArgsConstructor
public class ActionsService implements IInitService {

    private final DriverService driverService;
    private WebDriver driver;

    @Override
    public void initDriver() {
        driver = driverService.getDriver();
    }

    protected void holdAndMove(WebElement element) {
        final Action action = new Actions(driver)
                .clickAndHold(element)
                .moveByOffset(1, 0)
                .moveByOffset(300, 0)
                .build();
        action.perform();
    }

    protected void click(WebElement element) {
        final Action action = new Actions(driver)
                .click(element)
                .build();
        action.perform();
    }

    protected void scrollToOffset(int offset) {
        final JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0," + offset + ")");
    }

    protected void scrollToElement(WebElement element) {
        final Action action = new Actions(driver)
                .scrollToElement(element)
                .build();
        action.perform();
    }
}
