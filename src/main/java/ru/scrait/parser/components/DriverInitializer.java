package ru.scrait.parser.components;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.scrait.parser.services.InitService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DriverInitializer {

    private final InitService initService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        initService.init();
    }

}
