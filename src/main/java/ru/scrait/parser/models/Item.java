package ru.scrait.parser.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class Item extends AbstractData {

    private long product_id;
    private String title;
    private Set<String> images;
    private String video_url;
    private String shop_name;
    private String seller_user_id;
    private String seller_member_id;
    private String price;
    private Set<String[]> price_range;
    private long in_stock;
    private int min_amount;
    private Set<String> desc_img;
    private Map<String, String> props_img;
    private Set<Prop> props;
    private Map<String, String> sku_props_list;
    private Set<Skus> skus;

}
