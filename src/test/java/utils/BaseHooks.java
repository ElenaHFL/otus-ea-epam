package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class BaseHooks {
    protected static WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Найти список элементов, с учетом порядка следования
    public static void setFilter(String aria, String value) {
        // Пользователь нажимает на Location в блоке фильтров и выбирает Canada в выпадающем списке
        driver.findElement(By.id(aria)).click();
        driver.findElement(By.cssSelector("div[aria-labelledby='" + aria + "'] label[data-value='" + value + "']")).click();
        // Закрываем фильтр
        driver.findElement(By.id(aria)).click();
    }
}