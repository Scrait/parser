package ru.scrait.parser.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Item {

    private long product_id;
    private String title;
    private List<String> images;
    private String video_url;
    private String shop_name;
    private String seller_user_id;
    private String seller_member_id;
    private String price;
    private List<String> price_range;
    private long in_stock;
    private int min_amount;
    private List<String> desc_img;
    private Map<String, String> props_img;
    private Map<String, String> props;
    private Map<String, String> sku_props_list;
    private List<Map<String, String>> skus;

}
