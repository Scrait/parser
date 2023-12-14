package ru.scrait.parser.services;

import org.openqa.selenium.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.scrait.parser.components.Driver;
import ru.scrait.parser.models.Item;
import ru.scrait.parser.models.Response;
import ru.scrait.parser.utils.HttpUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ControlService {

    private final List<Driver> drivers = new ArrayList<>();
    @Value("${driver.count}")
    private int count;
    @Value("${driver.ip}")
    private String ip;
    @Value("${driver.port}")
    private int port;
    @Value("${parser.balancerIp}")
    private String balancerIp;
    @Value("${parser.balancerPort}")
    private int balancerPort;


    public void init() {
        for (int i = 0; i < count; i++) {
            final Point point = switch (i) {
                case 0 -> new Point(0, 0);
                case 1 -> new Point(600, 0);
                case 2 -> new Point(0, 500);
                case 3 -> new Point(600, 500);
                default -> null;
            };
            drivers.add(new Driver(ip, port, point));
        }
        drivers.forEach(Driver::init);
//        final String currentIp;
//        try (final DatagramSocket socket = new DatagramSocket()){
//            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//            currentIp = socket.getLocalAddress().getHostAddress();
//        } catch (UnknownHostException | SocketException e) {
//            throw new RuntimeException(e);
//        }
//        HttpUtils.initParserOnBalancer("127.0.0.1:8080", balancerIp + ":" + balancerPort);
    }

    public Response getData(long id) {
//        while (Collections.min(drivers, Comparator.comparing(drivers -> drivers.tasks)).tasks > 0) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            // bububu
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        final Driver driver = Collections.min(drivers, Comparator.comparing(drivers -> drivers.tasks));
        return driver.submit("id", 1);
    }

}
