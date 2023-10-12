package ru.scrait.parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    private final int code;
    private final String msg;
    private final AbstractData data;

}
