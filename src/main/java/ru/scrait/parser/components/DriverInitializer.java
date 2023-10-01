package ru.scrait.parser.components;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.scrait.parser.services.ActionsService;
import ru.scrait.parser.services.DriverService;
import ru.scrait.parser.services.InitService;
import ru.scrait.parser.services.ParseService;

@Component
@RequiredArgsConstructor
public class DriverInitializer {

    private final DriverService driverService;
    private final ParseService parserService;
    private final ActionsService activityService;
    private final InitService initService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        initService.init(driverService, parserService, activityService);
    }

}
