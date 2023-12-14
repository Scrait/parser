package ru.scrait.parser.components;

import org.openqa.selenium.Point;
import ru.scrait.parser.interfaces.IInitService;
import ru.scrait.parser.models.Response;
import ru.scrait.parser.models.Type;
import ru.scrait.parser.services.ActionsService;
import ru.scrait.parser.services.CardParseService;
import ru.scrait.parser.services.DriverService;
import ru.scrait.parser.services.ParseService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver implements IInitService {

    public final ParseService parseService;
    public final CardParseService cardParseService;
    private final DriverService driverService;
    private final ActionsService actionsService;
    public int tasks = 0;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public Driver(String ip, int port, Point point) {
        driverService = new DriverService(ip, port, point);
        actionsService = new ActionsService();
        parseService = new ParseService(actionsService);
        cardParseService = new CardParseService(actionsService);
    }

    @Override
    public void init() {
        driverService.init();
        parseService.initDriver(driverService.getDriver());
        cardParseService.initDriver(driverService.getDriver());
        actionsService.initDriver(driverService.getDriver());
    }


    public Response submit(long id) {
        tasks++;
        Response response;
        try {
            response = executor.submit(() -> parseService.getAndParse(id)).get();
        } catch (Exception e) {
            response = new Response(500, false, "parse-error", null);
        }
        tasks--;
        return response;
    }

    public Response submit(String keywords, int page) {
        tasks++;
        Response response;
        try {
            response = executor.submit(() -> cardParseService.getAndParse(keywords, page)).get();
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(500, false, "parse-error", null);
        }
        tasks--;
        return response;
    }

}
