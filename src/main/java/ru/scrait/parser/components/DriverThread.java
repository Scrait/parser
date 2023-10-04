package ru.scrait.parser.components;

import ru.scrait.parser.services.ActionsService;
import ru.scrait.parser.services.DriverService;
import ru.scrait.parser.services.ParseService;

import java.util.ArrayList;
import java.util.List;

public class DriverThread extends Thread {

    public final List<Long> tasks = new ArrayList<>();
    private final ParseService parseService;
    private final DriverService driverService;
    private final ActionsService actionsService;

    public DriverThread() {
        driverService = new DriverService();
        actionsService = new ActionsService();
        parseService = new ParseService(actionsService);
    }

    @Override
    public void run() {
        driverService.init();
        parseService.initDriver(driverService.getDriver());
        actionsService.initDriver(driverService.getDriver());
        while (true) {
            final List<Long> tasks = this.tasks;
            if (!tasks.isEmpty()) {
                parseService.parse(tasks.get(0));
            }
        }
    }

}
