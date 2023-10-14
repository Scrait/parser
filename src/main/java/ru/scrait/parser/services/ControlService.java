package ru.scrait.parser.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.scrait.parser.components.Driver;
import ru.scrait.parser.models.Item;

import java.util.*;

@Service
public class ControlService {

    private final List<Driver> drivers = new ArrayList<>();
    @Value("${driver.count}")
    private int count;
    @Value("${driver.ip}")
    private String ip;
    @Value("${driver.port}")
    private int port;

    public void init() {
        for (int i = 0; i < count; i++) {
            drivers.add(new Driver(ip, port));
        }
        drivers.forEach(Driver::init);
    }

    public Item getData(long id) {
        return Collections.min(drivers, Comparator.comparing(drivers -> drivers.tasks)).submit(id);
    }

}
