package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import ru.scrait.parser.interfaces.IInitDriverService;
import ru.scrait.parser.models.*;
import ru.scrait.parser.utils.FindUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RequiredArgsConstructor
public class CardParseService implements IInitDriverService {

    private final ActionsService actionsService;

    private final String QUERY = "https://s.1688.com/selloffer/offer_search.htm?n=y&netType=1%2C11%2C16&spm=a260k.dacugeneral.search.0&keywords=";

    private WebDriver driver;
    private JSONObject script;
    private JSONObject globalData;
    private final boolean debugMode = true;
    private int tries = 0;
    private int tries2 = 0;


    @Override
    public void initDriver(WebDriver driver) {
        this.driver = driver;
    }

    public Response getAndParse(String keywords, int page) {
        driver.get(QUERY + keywords + (page > 1 ? "&beginPage=" + page : ""));
        tries2 = 0;
        tries = 0;
        return parse(keywords, page);
    }

    public Response parse(String keywords, int currPage) {
        try {
            final Page page = new Page();

            page.setKeywords(keywords);
            page.setPage(currPage);

            setScript();

            page.setProducts(getProducts());

            page.setPage_size(currPage);



            bypassCaptcha();

//            if (driver.getCurrentUrl().contains("wrongpage")) {
//                return new Response(404, false, "page-not-found", null);
//            } else if (driver.getCurrentUrl().contains("factory")) {
//                return new Response(404, false, "page-not-found", null);
//            } else if (tries > 5) {
////                tries = 0;
//                return new Response(404, false, "page-not-found", null);
//            } else if (!driver.getCurrentUrl().contains(String.valueOf(id))) {
//                return getAndParse(id);
//            }
//
//            try {
//                setScript();
//            } catch (Exception e) {
//                tries++;
//            }
//
////            try {
//            item.setImages(getImages());
////            } catch (Exception ex) {
////                return new Response(500, false, "parse-error", null);
////            }
//
//            item.setVideo_url(getVideo_url());
//
//            item.setShop_name(getShop_name());
//
//            item.setSeller_user_id(getSeller_user_id());
//
//            item.setSeller_member_id(getSeller_member_id());
//
//            item.setPrice_range(getPriceRange());
//
//            item.setPrice(getPrice(item.getPrice_range()));
//
//            item.setMin_amount(getMin_amount());
//
//            item.setProps_img(getProps_img());
//
//            item.setDesc_img(getDesc_img());
//
//            item.setProps(getProps());
//
//            item.setSku_props_list(getSku_props_list());
//
//            item.setSkus(getSkus(item.getSku_props_list(), item.getPrice_range()));
//
//            item.setIn_stock(getSkusInStock());
//
//            item.setTitle(getTitle());

            return new Response(200, true, "success", page);
        } catch (Exception ex) {
            if (debugMode) {
                ex.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (tries2 > 40)   {
                throw new RuntimeException("Too many tries");
            }
            tries2++;
            return parse(keywords, currPage);
        }
    }



    private void bypassCaptcha() {
        if (driver.getTitle().equals("Captcha Interception") || isElementExists(By.id("nc_1_n1z"))) {
            //driver.manage().window().fullscreen();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            driver.manage().window().maximize();
            try {
                actionsService.holdAndMove(driver.findElement(By.id("nc_1_n1z")));
            } catch (Exception e) {
                //e.printStackTrace();
                if (isElementExists(By.className("errloading"))) {
                    actionsService.click(driver.findElement(By.className("errloading")));
                    bypassCaptcha();
                    driver.navigate().refresh();
                }
                bypassCaptcha();
                driver.manage().window().setSize(new Dimension(600, 600));
            }
            if (isElementExists(By.className("errloading"))) {
                throw new IllegalStateException();
            }
            driver.manage().window().setSize(new Dimension(600, 600));

//            driver.navigate().refresh();
        }
//        driver.manage().window().fullscreen();
//        driver.manage().window().maximize();
    }

    private void setScript() {
        if (driver.getTitle().equals("Captcha Interception")) {
            throw new IllegalStateException();
        }
        final WebElement element = driver.findElement(By.xpath("/html/body/script[4]"));
        final String scriptStr = element.getAttribute("innerHTML");
        if (debugMode) {
            try {
                Files.writeString(new File("dfdsf").toPath(), scriptStr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        final String subStrBegin = "window.data.offerresultData = successDataCheck(";
        final String subStrEnd = ",\"msg\":\"ok\",\"time\":";
        script = new JSONObject(scriptStr.substring(scriptStr.indexOf(subStrBegin) + subStrBegin.length(), scriptStr.indexOf(subStrEnd) + subStrEnd.length() + 4)).getJSONObject("data");
    }

    private Set<Product> getProducts() {
        final JSONArray array = script.getJSONArray("offerList");
        final Set<Product> set = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            final JSONObject j = array.getJSONObject(i);
            set.add(new Product(
                    j.getJSONObject("image").getString("imgUrl"),
                    j.getJSONObject("tradePrice").getJSONObject("offerPrice").getJSONObject("offerPrice").getString("price"),
                    j.getJSONObject("information").getString("simpleSubject"),
                    j.getJSONObject("company").getString("name"),
                    j.getJSONObject("aliTalk").getString("infoId"),
                    null

                ));
        }

        return set;
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
