package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Service;
import ru.scrait.parser.interfaces.IInitService;
import ru.scrait.parser.models.Item;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParseService implements IInitService {

    private final ActionsService actionsService;
    private final DriverService driverService;
    private final InitService initService;

    private final String QUERY = "https://detail.1688.com/offer/";

    private WebDriver driver;

    @Override
    public void initDriver() {
        driver = driverService.getDriver();
    }

    public Item parse(long id) {
        final Item item = new Item();
        item.setProduct_id(id);

        driver.get(QUERY + id + ".html");

        bypassCaptcha();

        item.setTitle(getTitle());

        final List<WebElement> imagesWE = driver.findElements(By.className("detail-gallery-turn-wrapper"));

        item.setImages(getImages(imagesWE));

        item.setVideo_url(getVideo_url());

        item.setShop_name(getShop_name());

        item.setSeller_user_id(getSeller_user_id(imagesWE));

        //item.setSeller_member_id();

        item.setPrice(getPrice());

        //item.setPrice_range();

        //item.setIn_stock();

        item.setMin_amount(getMin_amount());

        item.setDesc_img(getDesc_img());

        /**
         * Нажимаем на кнопку для расширения вариантов
         * для того, чтобы показались все данные
         */
        actionsService.scrollToOffset(driver.findElement(By.className("sku-wrapper-expend-button")).getLocation().y / 2);
        actionsService.click(driver.findElement(By.className("sku-wrapper-expend-button")));
        final List<WebElement> props = driver.findElements(By.className("sku-item-wrapper"));

        item.setProps_img(getProps_img(props));

        item.setSku_props_list(getSku_props_list(props));

        //item.setSkus();

        return item;
    }

    private void bypassCaptcha() {
        if (driver.getTitle().equals("Captcha Interception")) {
            actionsService.holdAndMove(driver.findElement(By.id("nc_1_n1z")));
        }
        if (isElementExists(By.className("baxia-dialog-close"))) {
            actionsService.click(driver.findElement(By.className("baxia-dialog-close")));
        }
    }

    private String getTitle() {
        return driver.findElement(By.className("title-text")).getText();
    }

    private List<String> getImages(List<WebElement> imagesWE) {
        final List<String> images = new ArrayList<>();
        imagesWE.forEach(el -> images.add(el.findElement(By.tagName("img")).getAttribute("src")));
        return images;
    }

    private String getVideo_url() {
        return driver.findElement(By.className("lib-video")).getAttribute("src");
    }

    private String getShop_name() {
         return driver.findElement(
                        By.xpath("//*[@id=\"hd_0_container_0\"]/div/div[2]/div/p[1]/span[1]"))
                .getText();
    }

    private String getSeller_user_id(List<WebElement> imagesWE) {
        final String imgSrc = imagesWE.get(0).findElement(By.tagName("img")).getAttribute("src");
        return imgSrc.substring(imgSrc.indexOf("!!") + 2, imgSrc.indexOf("!!") + 2 + 13);
    }

    private String getPrice() {
        return driver.findElement(By.className("price-text")).getText();
    }

    private int getMin_amount() {
        return Integer.parseInt(driver.findElement(By.className("unit-text")).getText().substring(0, 1));
    }

    private List<String> getDesc_img() {
        final List<String> descImages = new ArrayList<>();
        actionsService.scrollToOffset(5000);
        driver.findElements(By.className("desc-img-loaded"))
                .forEach(el -> descImages.add(el.getAttribute("src")));
        actionsService.scrollToOffset(-5000);
        return descImages;
    }

    private Map<String, String> getProps_img(List<WebElement> props) {
        final Map<String, String> propsImages = new HashMap<>();
        try {
            props.forEach(webElement -> propsImages.put("0:" + props.indexOf(webElement),
                    webElement.findElement(By.className("sku-item-image")).getCssValue("background").
                            substring(
                                    webElement.findElement(By.className("sku-item-image")).getCssValue("background").indexOf("https://"),
                                    webElement.findElement(By.className("sku-item-image")).getCssValue("background").indexOf(") no-repeat")
                            )));
        } catch (Exception ignored) {

        }
        return propsImages;
    }

    private Map<String, String> getSku_props_list(List<WebElement> props) {
        final Map<String, String> propsSkuList = new HashMap<>();
        final String type = driver.findElement(By.className("sku-prop-module-name")).getText();
        props.forEach(webElement -> propsSkuList.put("0:" + props.indexOf(webElement),
                type + ": " + webElement.findElement(By.className("sku-item-name")).getText()));
        return propsSkuList;
    }

    private boolean isElementExists(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }


}
