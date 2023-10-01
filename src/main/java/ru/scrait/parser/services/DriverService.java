package ru.scrait.parser.services;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import ru.scrait.parser.interfaces.IInitService;

import java.time.Duration;

@Service
public class DriverService implements IInitService {

    @Getter
    private WebDriver driver;

    @Override
    public void initDriver() {
        //System.setProperty("webdriver.chrome.driver", "/Users/scrait/Downloads/chromedriver-mac-arm64/chromedriver");
        System.setProperty("webdriver.chrome.driver", "C:/Users/scrai/Documents/chromedriver.exe");

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addArguments("user-agent=\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36\"");

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
    }

}
