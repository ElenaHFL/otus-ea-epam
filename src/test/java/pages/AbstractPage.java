package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseHooks;

import java.io.ByteArrayInputStream;

/**
 * Abstract class representation of a Page in the UI. Page object pattern
 */
public abstract class AbstractPage extends BaseHooks {

    /** Драйвер */
    protected WebDriver driver;

    /** Элемент More Filters */
    @FindBys({
            @FindBy(css = "section.evnt-filters-panel"),
            @FindBy(xpath = "//span[text()='More Filters']")
    })
    public WebElement moreFilters;

    /** Зона фильтра Location */
    @FindBy(id = "filter_location")
    public WebElement locationFilter;

    /** Зона фильтра Language */
    @FindBy(id = "filter_language")
    public WebElement languageFilter;

    /** Зона фильтра Category */
    @FindBy(id = "filter_category")
    public WebElement categoryFilter;

    /**
     * Конструктор - создание нового объекта
     */
    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Функция ожидания веб-элемент
     * @return веб-элемент
     */
    public WebElement waitForElement(WebElement element) {
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Функция ожидания загрузки страницы, проверяем что отсутствует индикатор загрузки
     * @return веб-элемент
     */
    public void waitForPageLoaded() {
       new WebDriverWait(driver, 5, 100)
               .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.evnt-loader")));
    }

    @Step("Установка в фильтре значения - '{value}'")
    public void setFilter(WebElement aria, String value) {
        // Раскрываем фильтр
        WebElement element = aria.findElement(By.xpath("..//label[@data-value='" + value + "']"));
        setFilter(aria, element);
        Allure.addAttachment("Фильтр", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

}
