package ru.scrait.parser.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scrait.parser.services.ParseService;

@RestController
@RequiredArgsConstructor
public class PizdaController {

    private final ParseService parseService;

    @GetMapping
    public String pizda() {
        return parseService.parse(719198509465L).toString();
    }

}
