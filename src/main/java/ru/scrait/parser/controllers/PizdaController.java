package ru.scrait.parser.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scrait.parser.services.ParseService;

@RestController
@RequiredArgsConstructor
public class PizdaController {

    private final ParseService parseService;

    @RequestMapping
    public String pizda() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(parseService.parse(724733002508L));
    }

}
