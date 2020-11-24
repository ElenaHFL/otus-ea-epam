package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseHooks;

import java.io.ByteArrayInputStream;
import java.time.Duration;

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

    /** Индикатор загрузки */
    public By loader = By.cssSelector("div.evnt-loader");

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
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
    }

    /**
     * Функция нажатия кнопки ESCAPE
     */
    public void clickEscape() {
        driver.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.ESCAPE));
    }

    /**
     * Дефолтный вызов метода setFilter, без скролла
     */
    public void setFilter(WebElement aria, String value) {
        setFilter(aria, value, false );
    }

    @Step("Установка в фильтре значения - '{value}'")
    public void setFilter(WebElement aria, String value, Boolean needScroll) {
        WebElement element = aria.findElement(By.xpath("..//label[@data-value='" + value + "']"));
        // Раскрываем фильтр
        aria.click();
        // На случай если есть скролл в списке значений
        if (needScroll) new Actions(driver).moveToElement(element).perform();
        element.click();
        // Закрываем фильтр через ESC
        clickEscape();
        Allure.addAttachment("Фильтр", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }
}
