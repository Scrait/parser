package ru.scrait.parser.components;

import ru.scrait.parser.interfaces.IInitService;
import ru.scrait.parser.models.Item;
import ru.scrait.parser.services.ActionsService;
import ru.scrait.parser.services.DriverService;
import ru.scrait.parser.services.ParseService;

public class Driver implements IInitService {

    public final ParseService parseService;
    private final DriverService driverService;
    private final ActionsService actionsService;
    public int tasks = 0;

    public Driver(String ip, int port) {
        driverService = new DriverService(ip, port);
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
