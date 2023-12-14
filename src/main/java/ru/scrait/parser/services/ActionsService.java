package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import ru.scrait.parser.interfaces.IInitDriverService;
import ru.scrait.parser.utils.FindUtils;

/**
 * Здесь описываются все действия с элементами сайта
 *
 * @author Scrait
 * @since 01/10/2023
 */
@RequiredArgsConstructor
public class ActionsService implements IInitDriverService {

    private WebDriver driver;

    @Override
    public void initDriver(WebDriver driver) {
        this.driver = driver;
    }

    protected void holdAndMove(WebElement element) {
        final Action action = new Actions(driver)
                .clickAndHold(element)

                .moveByOffset(FindUtils.randomNumber(3, 6), FindUtils.randomNumber(1, 3))
                .moveByOffset(FindUtils.randomNumber(-3, -1), FindUtils.randomNumber(1, 3))

                .moveByOffset(FindUtils.randomNumber(40, 80), FindUtils.randomNumber(-20, 20))
                .moveByOffset(FindUtils.randomNumber(-4, -2), FindUtils.randomNumber(-20, 20))

                .moveByOffset(FindUtils.randomNumber(220, 250), FindUtils.randomNumber(4, 7))
                .release()

                .moveByOffset(FindUtils.randomNumber(-5, 4), FindUtils.randomNumber(-7, 3))
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

    protected void scrollToScrollHeight() {
        final JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("var el = document.getElementsByClassName('infinite-scroller')[0]; el.scroll(0, el.scrollHeight)");
    }

//    protected void scrollToElement(WebElement element) {
//        final Action action = new Actions(driver)
//                .scrollToElement(element)
//                .build();
//        action.perform();
//    }
}
