package ru.scrait.parser.components;

import org.springframework.stereotype.Component;
import ru.scrait.parser.interfaces.IInitService;
import ru.scrait.parser.models.Item;
import ru.scrait.parser.services.ActionsService;
import ru.scrait.parser.services.DriverService;
import ru.scrait.parser.services.ParseService;

@Component
public class Driver implements IInitService {

    public final ParseService parseService;
    private final DriverService driverService;
    private final ActionsService actionsService;
    public int tasks = 0;

    public Driver() {
        driverService = new DriverService();
        actionsService = new ActionsService();
        parseService = new ParseService(actionsService);
    }

    @Override
    public void init() {
        driverService.init();
        parseService.initDriver(driverService.getDriver());
        actionsService.initDriver(driverService.getDriver());
    }


    public Item submit(long id) {
        tasks++;
        final Item item = parseService.getAndParse(id);
        tasks--;
        return item;
    }

}
