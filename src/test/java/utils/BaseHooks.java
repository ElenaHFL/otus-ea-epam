package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHooks {

    /** Драйвер */
    protected WebDriver driver;
    /** Регулярное выражение для выделения дат */
    private final Pattern DATE_PATTERN = Pattern.compile("\\s*(\\d{1,2})\\s*-\\s*(\\d{1,2})(\\s*\\D{3}\\s*\\d{4})");
    /** Формат дат */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    /** Базовая ссылка на проверяемый сайт */
    protected static String baseUrl;

    //@BeforeEach
    public void setUpRemote(TestInfo testInfo) throws MalformedURLException {

        // Получили название теста
        String testName = String.valueOf(testInfo.getDisplayName());
        // Получили время выполнения теста
        String testTime = LocalDateTime.now().toString().replaceAll("[^\\d]","");

        String selenoidURL = "http://localhost:4444/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        caps.setVersion("86.0");
        caps.setCapability("enableVNC", true);
        caps.setCapability("enableVideo", true);
        caps.setCapability("enableLog", true);
        caps.setCapability("screenResolution", "1280x1024");
        caps.setCapability("logName", testName + "_" + testTime + ".log");
        caps.setCapability("videoName", testName + "_" + testTime + ".mp4");

        driver = new RemoteWebDriver(new URL(selenoidURL), caps);

        if (driver != null) {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        }
    }

    @BeforeEach
    public void setUpLocal() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        /*WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();*/

        if (driver != null) {
            //driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        }
    }

    @BeforeEach
    public void loadConfig() throws Throwable {
        SuiteConfiguration config = new SuiteConfiguration();
        baseUrl = config.getProperty("site.url");
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Получение списка дат из строкового значения - '{value}'")
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