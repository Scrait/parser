package ru.scrait.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Skus {

    private final String sold;
    private final String price;
    private final String sku_id;
    private final String in_stock;
    private final String sku_props_list_ids;

}
