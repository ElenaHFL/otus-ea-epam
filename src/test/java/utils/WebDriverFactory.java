package utils;

import com.google.common.base.Strings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {

    // Допустимые имена браузеров
    enum DriverName {
        CHROME{
            public WebDriver create(String str){
                WebDriverManager.chromedriver().setup();
                if (str == null) return new ChromeDriver();
                else return new ChromeDriver(new ChromeOptions().addArguments(str));
            }
        },
        FIREFOX{
            public WebDriver create(String str){
                WebDriverManager.firefoxdriver().setup();
                if (str == null) return new FirefoxDriver();
                else return new FirefoxDriver(new FirefoxOptions().addArguments(str));
            }
        },
        IE{
            public WebDriver create(String str){
                WebDriverManager.iedriver().setup();
                // NOTE: шоб заработал, надо выровнить уровень защиты + сделать масштаб 100%
                if (str == null) return new InternetExplorerDriver();
                else {
                    // Например: -Doptions=ignoreZoomSetting (тогда масштаб не важен)
                    DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(str, true);
                    return new InternetExplorerDriver(new InternetExplorerOptions().merge(capabilities));
                }
            }
        };

        public abstract WebDriver create(String str);
    }

    public static WebDriver create(String webDriverName) {
        return create(webDriverName, null);
    }

    public static WebDriver create(String webDriverName, String options) {
        return normalizeValue(webDriverName).create(options);
    }

    protected static DriverName normalizeValue(String value) {
        value = Strings.nullToEmpty(value)
                .toUpperCase()
                .replace("'", "");

        try {
            return Enum.valueOf(DriverName.class, value);
        } catch(IllegalArgumentException ex) {
            System.out.println("Браузер для запуска либо не указан, либо не из списка допустимых - CHROME/FIREFOX/IE. По умолчанию будет запущен CHROME.");
            return DriverName.CHROME;
        }

    }
}