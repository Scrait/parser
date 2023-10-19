package ru.scrait.parser.services;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.scrait.parser.interfaces.IInitDriverService;
import ru.scrait.parser.models.Item;
import ru.scrait.parser.models.Prop;
import ru.scrait.parser.models.Skus;
import ru.scrait.parser.utils.FindUtils;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
public class ParseService implements IInitDriverService {

    private final ActionsService actionsService;

    private final String QUERY = "https://detail.1688.com/offer/";

    private WebDriver driver;
    private WebDriverWait wait;
    private JSONObject script;
    private JSONObject globalData;


    @Override
    public void initDriver(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public Item getAndParse(long id) {
        driver.get(QUERY + id + ".html");

        return parse(id);
    }

    public Item parse(long id) {
        try {
            final Item item = new Item();
            item.setProduct_id(id);

            bypassCaptcha();

            setScript();

            item.setImages(getImages());

            item.setVideo_url(getVideo_url());

            item.setShop_name(getShop_name());

            item.setSeller_user_id(getSeller_user_id());

            item.setSeller_member_id(getSeller_member_id());

            item.setPrice_range(getPriceRange());

            item.setPrice(getPrice());

            item.setMin_amount(getMin_amount());

            item.setProps_img(getProps_img());

            item.setDesc_img(getDesc_img());

            item.setProps(getProps());

            item.setSku_props_list(getSku_props_list());

            item.setSkus(getSkus(item.getSku_props_list(), item.getPrice_range()));

            item.setIn_stock(getSkusInStock());

            item.setTitle(getTitle());
            return item;
        } catch (Exception ignored) {
//            ignored.printStackTrace();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            return parse(id);
        }
    }



    private void bypassCaptcha() {
        if (driver.getTitle().equals("Captcha Interception")) {
            driver.manage().window().fullscreen();
            actionsService.holdAndMove(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nc_1_n1z"))));
            driver.navigate().refresh();
        }
        driver.manage().window().fullscreen();
        driver.manage().window().maximize();
    }

    private void setScript() {
        final List<WebElement> elements =  driver.findElements(By.tagName("script"));
        for (WebElement element : elements) {
            try {
                final String scriptStr = element.getAttribute("innerHTML");
                script = new JSONObject(scriptStr.substring(scriptStr.indexOf("INIT_DATA=") + 10));
//                try {
//                    Files.writeString(new File("dfdsf").toPath(), scriptStr);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

                break;
            } catch (Exception ignored) {}
        }
        globalData = script.getJSONObject("globalData");
    }

    private String getTitle() {
        return driver.findElement(By.className("title-text")).getText();
    }



    private Set<String> getImages() {
        final JSONArray array = globalData.getJSONArray("images");
        return FindUtils.getFieldFromArray(array, "fullPathImageURI");
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
        final JSONObject object = globalData.getJSONObject("tempModel");
        return object.getString("companyName");
    }

    private String getSeller_user_id() {
        final JSONObject object = globalData.getJSONObject("tempModel");
        return String.valueOf(object.getLong("sellerUserId"));
    }

    private String getSeller_member_id() {
        final JSONObject object = globalData.getJSONObject("tempModel");
        return object.getString("sellerMemberId");
    }

    private Set<String[]> getPriceRange() {
        final JSONObject object = globalData.getJSONObject("orderParamModel").getJSONObject("orderParam").getJSONObject("skuParam");
        if (!object.getString("skuPriceType").equals("rangePrice")) {
            return new HashSet<>();
        }
        final JSONArray array = object.getJSONArray("skuRangePrices");
        final Set<String[]> set = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            final JSONObject j = array.getJSONObject(i);
            set.add(new String[]{j.getString("price"), String.valueOf(j.getInt("beginAmount"))});
        }
        final Set<String[]> sortedSet = new TreeSet<>(Comparator.comparingInt(o -> Integer.parseInt(o[1])));
        sortedSet.addAll(set);
        return sortedSet;
    }

    private String getPrice() {
        final JSONObject data = script.getJSONObject("data");
        final Iterator<String> iterator = data.keys();
        iterator.next();
        iterator.next();
        iterator.next();
        final JSONObject object = data.getJSONObject(iterator.next()).getJSONObject("data");
        return object.getString("price");
    }

    private int getMin_amount() {
        try {
            final JSONObject object = globalData.getJSONObject("orderParamModel").getJSONObject("orderParam").getJSONObject("mixParam");
            return object.getInt("mixAmount");
        } catch (JSONException e) {
            return 0;
        }
    }

    private Set<String> getDesc_img() {
        driver.manage().window().fullscreen();
        for (int i = 0; i < 600; i++) {
            actionsService.scrollToOffset(75);
        }
        driver.manage().window().maximize();
        driver.manage().window().fullscreen();
        for (int i = 0; i < 600; i++) {
            actionsService.scrollToOffset(-75);
        }
        driver.manage().window().maximize();
        final Set<String> descImages = new HashSet<>();
        driver.findElements(By.className("desc-img-loaded"))
                .forEach(el -> descImages.add(el.getAttribute("src")));
        return descImages;
    }

    private Map<String, String> getProps_img() {
        final JSONObject object = globalData.getJSONObject("skuModel").getJSONArray("skuProps").getJSONObject(0);
        final JSONArray array = object.getJSONArray("value");
        final Map<String, String> map = new HashMap<>();
        for (int i = 0; i < array.length(); i++) {
            final JSONObject j = array.getJSONObject(i);

            String imageUrl = "";
            try {
                imageUrl = j.getString("imageUrl");
            } catch (Exception ignored) {}

            map.put("0:" + i, imageUrl);
        }
        return map;
    }

    private Map<String, String> getSku_props_list() {
        final JSONArray mainArray = globalData.getJSONObject("skuModel").getJSONArray("skuProps");
        final Map<String, String> map = new HashMap<>();
        for (int i = 0; i < mainArray.length(); i++) {
            final JSONObject object = mainArray.getJSONObject(i);
            final String prop = object.getString("prop");
            final JSONArray array = object.getJSONArray("value");
            for (int k = 0; k < array.length(); k++) {
                final JSONObject j = array.getJSONObject(k);
                map.put(i + ":" + k, prop + ": " + j.getString("name"));
            }
        }

        final Map<String, String> sortedMap = new TreeMap<>((o1, o2) -> {
            // generated by tabnine))
            final String[] split1 = o1.split(":");
            final String[] split2 = o2.split(":");
            if (split1[0].equals(split2[0])) {
                return Integer.parseInt(split1[1]) - Integer.parseInt(split2[1]);
            } else {
                return Integer.parseInt(split1[0]) - Integer.parseInt(split2[0]);
            }
        });
        sortedMap.putAll(map);

        return sortedMap;
    }
    private Set<Prop> getProps() {
        final JSONObject data = script.getJSONObject("data");
        final Iterator<String> iterator = data.keys();
        iterator.next();
        final JSONObject object = data.getJSONObject(iterator.next());
        final JSONArray array = object.getJSONArray("data");
        final Set<Prop> set = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            final JSONObject j = array.getJSONObject(i);
            set.add(new Prop(j.getString("name"), true, j.getString("value")));
        }
        return set;
    }

    private Set<Skus> getSkus(Map<String, String> skuPropsList, Set<String[]> priceRange) {
        final JSONObject object = globalData.getJSONObject("skuModel").getJSONObject("skuInfoMap");
        final Iterator<String> iterator = object.keys();
        final Set<Skus> set = new HashSet<>();
        while (iterator.hasNext()) {
            final JSONObject iterObject = object.getJSONObject(iterator.next());

            final StringBuilder skuPropsListIds = new StringBuilder();
            for (String specAttrs : iterObject.getString("specAttrs").replace("&gt", "").split(";")) {
                skuPropsListIds.append(FindUtils.getKeyFromValue(skuPropsList, specAttrs)).append(";");
            }

            String price = "";
            try {
                price = iterObject.getString("price");
            } catch (Exception ignored) {
                price = Collections.max(priceRange.stream().toList(), Comparator.comparing(strings -> Float.parseFloat(strings[0])))[0];
            }

            set.add(new Skus(
                    iterObject.getInt("saleCount"),
                    price,
                    iterObject.getLong("skuId"),
                    iterObject.getInt("canBookCount"),
                    skuPropsListIds.substring(0, skuPropsListIds.length() - 1)
            ));
        }


        return set;
    }

    private long getSkusInStock() {
        final JSONObject object = globalData.getJSONObject("orderParamModel").getJSONObject("orderParam");
        return object.getLong("canBookedAmount");
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
