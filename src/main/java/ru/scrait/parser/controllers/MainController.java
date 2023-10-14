package ru.scrait.parser.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scrait.parser.models.Response;
import ru.scrait.parser.services.ControlService;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final ControlService controlService;

    @RequestMapping
    public String prepare(long id) throws JsonProcessingException {
        return sendData(id);
    }

    private String sendData(long id) throws JsonProcessingException {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new Response(200, "success", controlService.getData(id)));
        } catch (Exception e) {
            //second try
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new Response(200, "success", controlService.getData(id)));
        }
    }
}
