package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHooks {

    protected WebDriver driver;

    /** Регулярное выражение для выделения дат */
    private final Pattern DATE_PATTERN = Pattern.compile("\\s*(\\d{1,2})\\s*-\\s*(\\d{1,2})(\\s*\\D{3}\\s*\\d{4})");
    /** Формат дат */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    //@BeforeEach
    public void setUp() throws MalformedURLException {
        String selenoidURL = "http://localhost:4444/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        caps.setVersion("86.0");
        caps.setCapability("enableVNC", true);
        caps.setCapability("screenResolution", "1280x1024");
        //caps.setCapability("enableVideo", true);
        caps.setCapability("enableLog", true);

        driver = new RemoteWebDriver(new URL(selenoidURL), caps);

        if (driver != null) {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    @BeforeEach
    public void setUpLocal() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        if (driver != null) {
            //driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void setFilter(WebElement aria, WebElement element) {
        // Раскрываем фильтр
        aria.click();
        element.click();
        aria.click();
    }

    @Step("Получение списка дат из строкового значения {value}")
    public ArrayList<Date> collectDates(String value) throws ParseException {
        ArrayList<Date> dates = new ArrayList<>();

        Matcher matcher = DATE_PATTERN.matcher(value);
        // Если указан диапазон, парсим даты
        if (matcher.matches()) {
            String str1 = matcher.group(1) + matcher.group(3);
            String str2 = matcher.group(2) + matcher.group(3);
            Date date1 = DATE_FORMAT.parse(str1);
            Date date2 = DATE_FORMAT.parse(str2);
            dates.add(date1);
            dates.add(date2);
        }else {
            // Иначе просто сохраняем
            Date date = DATE_FORMAT.parse(value);
            dates.add(date);
        }

        return dates;
    }
}