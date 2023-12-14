package ru.scrait.parser.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scrait.parser.models.Response;
import ru.scrait.parser.services.ControlService;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final ControlService controlService;

    @RequestMapping
    public ResponseEntity<?> prepare(long id) {
        System.out.println(id);
        return sendData(id);
    }

    private ResponseEntity<?> sendData(long id) {
        final Response response = controlService.getData(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

}
