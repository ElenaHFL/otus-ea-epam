package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import utils.BaseHooks;

/**
 * Abstract class representation of a Page in the UI. Page object pattern
 */
public abstract class AbstractPage extends BaseHooks {

    protected WebDriver driver;

    // Получили элемент More Filters
    @FindBys({
            @FindBy(css = "section.evnt-filters-panel"),
            @FindBy(xpath = "//span[text()='More Filters']")
    })
    public WebElement moreFilters;

    // Получили зону фильтра Location
    @FindBy(id = "filter_location")
    public WebElement locationFilter;

    // Получили зону фильтра Language
    @FindBy(id = "filter_language")
    public WebElement languageFilter;

    // Получили зону фильтра Category
    @FindBy(id = "filter_category")
    public WebElement categoryFilter;

    /*
     * Constructor injecting the WebDriver interface
     *
     * @param webDriver
     */
    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Установить значение {value} в фильтре")
    public void setFilter(WebElement aria, String value) {
        // Раскрываем фильтр
        WebElement element = aria.findElement(By.xpath("..//label[@data-value='" + value + "']"));
        setFilter(aria, element);
    }

}
