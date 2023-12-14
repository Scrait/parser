package ru.scrait.parser.services;

import lombok.Getter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.scrait.parser.interfaces.IInitService;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Getter
public class DriverService implements IInitService {

    private WebDriver driver;
    private final String ip;
    private final int port;
    private final Point point;

    public DriverService(String ip, int port, Point point) {
        this.ip = ip;
        this.port = port;
        this.point = point;
    }


    @Override
    public void init() {
        //System.setProperty("webdriver.chrome.driver", "/Users/scrait/Downloads/chromedriver-mac-arm64/chromedriver");
        //System.out.println(path);
        //System.setProperty("webdriver.chrome.driver", "C:/Users/scrai/Documents/chromedriver.exe");

        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        chromeOptions.addArguments("--ignore-ssl-errors=yes");
        chromeOptions.addArguments("--ignore-certificate-errors");
        //chromeOptions.addArguments("--headless=new");
//
//        try {
//            driver = new RemoteWebDriver(new URL("http://" + ip + ":" + port + "/wd/hub"), chromeOptions);
            driver = new ChromeDriver(chromeOptions);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
        //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().window().setSize(new Dimension(600, 600));
        driver.manage().window().setPosition(point);
        //driver.get("https://www.google.com/");
        //driver.manage().window().fullscreen();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
    }

}
