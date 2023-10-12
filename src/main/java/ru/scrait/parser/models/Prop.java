package ru.scrait.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Prop {

    private final String name;
    private final boolean show;
    private final String value;

}
