package ru.scrait.parser.components;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.scrait.parser.services.ControlService;

@Component
@RequiredArgsConstructor
public class DriversInitializer {

    private final ControlService initService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        initService.init();
    }

}
