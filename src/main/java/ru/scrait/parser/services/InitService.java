package ru.scrait.parser.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.scrait.parser.components.DriverThread;
import ru.scrait.parser.interfaces.IInitService;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitService implements IInitService {

    @Getter
    private final List<DriverThread> threads = new ArrayList<>();

    @Override
    public void init() {
        threads.add(new DriverThread());
        threads.get(0).start();
    }

}
