package ru.scrait.parser.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class Page extends AbstractData {

    private String keywords;
    private int page;
    private Set<Product> products;
    private int page_size;
    private int products_count;
    private int supplier_id;

}
