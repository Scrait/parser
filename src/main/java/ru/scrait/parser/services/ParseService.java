package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import ru.scrait.parser.interfaces.IInitDriverService;
import ru.scrait.parser.models.Item;
import ru.scrait.parser.models.Skus;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ParseService implements IInitDriverService {

    private final ActionsService actionsService;

    private final String QUERY = "https://detail.1688.com/offer/";

    private WebDriver driver;
    private WebDriverWait wait;

    @Override
    public void initDriver(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public Item getAndParse(long id) {
        driver.get(QUERY + id + ".html");
        actionsService.scrollToOffset(-10000);
        return parse(id);
    }

    public Item parse(long id) {
        try {
//            actionsService.scrollToOffset(-10000);
//            actionsService.scrollToOffset(5000);
//            actionsService.scrollToOffset(-10000);
            final Item item = new Item();
            item.setProduct_id(id);

            //driver.get(QUERY + id + ".html");

            bypassCaptcha();

            //bypassCaptcha();

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

            /**
             * Получаем вид до скролла
             */
            final String type = driver.findElement(By.className("sku-prop-module-name")).getText();

            /**
             * Нажимаем на кнопку для расширения вариантов
             * для того, чтобы показались все данные
             */
            try {
                //actionsService.scrollToOffset(250);
                actionsService.click(driver.findElement(By.className("sku-wrapper-expend-button")));
            } catch (Exception ignored) {}

            final List<WebElement> props = driver.findElements(By.className("sku-item-wrapper"));
            //actionsService.scrollToOffset(-10000);

            item.setProps_img(getProps_img(props));

            item.setDesc_img(getDesc_img());

            item.setSku_props_list(getSku_props_list(props, type));

            item.setSkus(getSkus(props));

            item.setIn_stock(getSkusInStock(item.getSkus()));

            item.setTitle(getTitle());
            return item;
        } catch (Exception ignored) {
            //ignored.printStackTrace();
            return parse(id);
        }
    }

    private void bypassCaptcha() {
        if (driver.getTitle().equals("Captcha Interception")) {
            actionsService.holdAndMove(driver.findElement(By.id("nc_1_n1z")));
        }

        if (isElementExists(By.className("baxia-dialog-close"))) {
            try {
                actionsService.click(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("baxia-dialog-close"))));
            } catch (Exception ignored) {}
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
        final By by = By.xpath("//*[@id=\"detail-main-video-content\"]/div/video");
        if (isElementExists(by)) {
            return driver.findElement(by).getAttribute("src");
        } else {
            return null;
        }
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
//        actionsService.scrollToOffset(1000);
//        actionsService.scrollToOffset(1000);
//        actionsService.scrollToOffset(1000);
//        actionsService.scrollToOffset(1000);
//        actionsService.scrollToOffset(1000);

        driver.findElements(By.className("desc-img-loaded"))
                .forEach(el -> descImages.add(el.getAttribute("src")));
        //actionsService.scrollToOffset(-10000);
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

    private Map<String, String> getSku_props_list(List<WebElement> props, String type) {
        final Map<String, String> propsSkuList = new HashMap<>();
        props.forEach(webElement -> propsSkuList.put("0:" + props.indexOf(webElement),
                type + ": " + webElement.findElement(By.className("sku-item-name")).getText()));
        return propsSkuList;
    }

    private List<Skus> getSkus(List<WebElement> props) {
        final List<Skus> skus = new ArrayList<>();
        props.forEach(webElement -> skus.add(new Skus(
                String.valueOf(0),
                webElement.findElement(By.className("discountPrice-price")).getText().substring(
                        0, webElement.findElement(By.className("discountPrice-price")).getText().length() - 1
                ),
                "",
                webElement.findElement(By.className("sku-item-sale-num")).getText().substring(
                        0, webElement.findElement(By.className("sku-item-sale-num")).getText().length() - 3
                ),
                "0:" + props.indexOf(webElement)

        )));
        return skus;
    }

    private long getSkusInStock(List<Skus> skus) {
        AtomicLong inStock = new AtomicLong();
        skus.forEach(s -> inStock.addAndGet(Long.parseLong(s.getIn_stock())));
        return inStock.get();
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
