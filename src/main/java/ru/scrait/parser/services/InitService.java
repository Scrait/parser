package ru.scrait.parser.services;
;
import org.springframework.stereotype.Service;
import ru.scrait.parser.interfaces.IInitService;

@Service
public class InitService implements IInitService {

    private DriverService driverService;
    private ParseService parserService;
    private ActionsService activityService;

    public void init(DriverService driverService, ParseService parserService, ActionsService activityService) {
        this.driverService = driverService;
        this.parserService = parserService;
        this.activityService = activityService;
        initDriver();
    }

    @Override
    public void initDriver() {
        driverService.initDriver();
        parserService.initDriver();
        activityService.initDriver();
    }
}
