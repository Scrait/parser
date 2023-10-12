package ru.scrait.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Skus {

    private final int sold;
    private final String price;
    private final long sku_id;
    private final int in_stock;
    private final String sku_props_list_ids;

}
