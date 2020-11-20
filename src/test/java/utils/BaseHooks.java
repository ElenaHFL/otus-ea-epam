package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHooks {

    protected static WebDriver driver;
    /** Регулярное выражение для выделения дат */
    private final Pattern DATE_PATTERN = Pattern.compile("\\s*(\\d{1,2})\\s*-\\s*(\\d{1,2})(\\s*\\D{3}\\s*\\d{4})");
    /** Формат дат */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

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

    protected WebElement waitForElement(WebElement element) {
        return new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(element));
    }

    // Установить значение в фильтре
    public static void setFilter(WebElement aria, WebElement element) {
        // Раскрываем фильтр
        aria.click();
        // На случай если есть скролл в списке значений
        Actions builder = new Actions(driver);
        builder.moveToElement(element);
        builder.perform();
        element.click();
        // Закрываем фильтр
        aria.click();
    }

    // Получить список дат из строкового значения
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