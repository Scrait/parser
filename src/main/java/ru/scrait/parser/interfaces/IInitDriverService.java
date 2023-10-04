package ru.scrait.parser.interfaces;

import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface IInitDriverService {

    void initDriver(WebDriver driver);

}
