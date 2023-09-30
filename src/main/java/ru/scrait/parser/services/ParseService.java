package ru.scrait.parser.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import ru.scrait.parser.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ParseService {

    private WebDriver driver = new ChromeDriver();

    private final String QUERY = "https://detail.1688.com/offer/";

    public Item parse(long id) {
        final Item item = new Item();
        item.setProduct_id(id);

        initDriver();
        driver.get(QUERY + id + ".html");

        if (driver.getTitle().equals("Captcha Interception")) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //holdAndMove(driver.findElement(By.className("btn_slide")));
        }



        item.setTitle(driver.findElement(By.className("title-text")).getText());

        final List<String> images = new ArrayList<>();
        final List<WebElement> imagesWE = driver.findElements(By.className("detail-gallery-turn-wrapper"));
        imagesWE.forEach(el -> images.add(el.findElement(By.tagName("img")).getAttribute("src")));
        item.setImages(images);

        //TODO:check later
        item.setVideo_url("potom");

        item.setShop_name(driver.findElement(
                By.xpath("//*[@id=\"hd_0_container_0\"]/div/div[2]/div/p[1]/span[1]"))
                .getText()
        );

        System.out.println(imagesWE);
        final String imgSrc = imagesWE.get(0).findElement(By.tagName("img")).getAttribute("src");
        item.setSeller_user_id(imgSrc.substring(
                imgSrc.indexOf("!!") + 2, imgSrc.indexOf("-0-cib.jpg"))
        );

        //item.setSeller_member_id();

        item.setPrice(driver.findElement(By.className("price-text")).getText());

        //item.setPrice_range();

        //item.setIn_stock();

        item.setMin_amount(Integer.parseInt(
                driver.findElement(By.className("unit-text")).getText().substring(0, 1))
        );

        final List<String> descImages = new ArrayList<>();
        driver.findElements(By.className("desc-img-loaded"))
                .forEach(el -> descImages.add(el.getText()));
        item.setDesc_img(descImages);


        /**
         * Нажимаем на кнопку для расширения вариантов для того чтобы показались все данные
         */
        click(driver.findElement(By.className("sku-wrapper-expend-button")));

        final List<WebElement> props = driver.findElements(By.className("sku-item-wrapper"));

        final Map<String, String> propsImages = new HashMap<>();
        props.forEach(webElement -> propsImages.put("0:" + props.indexOf(webElement),
                webElement.findElement(By.className("sku-item-image")).getCssValue("background").
                        substring(
                                webElement.findElement(By.className("sku-item-image")).getCssValue("background").indexOf("https://"),
                                webElement.findElement(By.className("sku-item-image")).getCssValue("background").indexOf(") no-repeat")
                        )));
        item.setProps_img(propsImages);

        final Map<String, String> propsSkuList = new HashMap<>();
        props.forEach(webElement -> propsSkuList.put("0:" + props.indexOf(webElement),
                "Color: " + webElement.findElement(By.className("sku-item-name")).getText()));
        item.setSku_props_list(propsSkuList);

        //item.setSkus();

        driver.close();

        return item;
    }

    private void initDriver() {
        //System.setProperty("webdriver.chrome.driver", "/Users/scrait/Downloads/chromedriver-mac-arm64/chromedriver");
        System.setProperty("webdriver.chrome.driver", "C:/Users/scrai/Documents/chromedriver.exe");
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        //chromeOptions.setExperimentalOption("excludeSwitches", "enable-automation");
        //chromeOptions.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private void holdAndMove(WebElement element) {
        Actions action = new Actions(driver);
        action.clickAndHold(element).build().perform();
        action.moveToLocation(element.getLocation().x + 100, element.getLocation().y);
        action.release();
        //you need to release the control from the test
        //actions.MoveToElement(element).Release();
    }

    private void click(WebElement element) {
        Actions action = new Actions(driver);
        action.click(element).build().perform();
    }



}
