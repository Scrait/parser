package ru.scrait.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private final String image;
    private final String price;
    private final String title;
    private final String shop_name;
    private final String product_id;
    private final String[] delivery_area;

}
