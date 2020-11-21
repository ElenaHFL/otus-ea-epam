package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Класс описывающий страницу с видео материалами о выступлениях
 */
public class VideoPage extends AbstractPage {

    /** Список названий выступлений на карточках */
    @FindBy(css = "section.evnt-talks-panel div.evnt-talk-card div.evnt-talk-name span")
    public List<WebElement> talkСardNameList;

    /** Список выступлений */
    @FindBy(css = "section.evnt-filters-panel .evnt-results-cell span")
    public WebElement foundResults;

    /** Поле поиска */
    @FindBys({
            @FindBy(css = "section.evnt-filters-panel"),
            @FindBy(css = "input.evnt-search")
    })
    public WebElement search;

    /**
     * Конструктор - создание нового объекта
     */
    public VideoPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Функция получения из веб-элемента ссылки на на детальную карточку выступления
     * @return строку
     */
    public String getLinkFromElement(WebElement element) {
        return waitForElement(element).findElement(By.xpath("./ancestor::a[@href]")).getAttribute("href");
    }

    @Step("Ввод в поисковую строку значения {value}")
    public void setSearch(String value) throws InterruptedException {
        String countBefore = foundResults.getText();
        // Пользователь вводит ключевое слово QA в поле поиска
        waitForElement(search).sendKeys(value);
        // Подождем пока фильтр применится, ориентируемся на изменение числа записей
        while(foundResults.getText().equals(countBefore)){
            Thread.sleep(500);
        }
        Allure.addAttachment("Поиск", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Установка в фильтре значения - {value}")
    public void setFilterWithScroll(WebElement aria, String value) {
        WebElement element = aria.findElement(By.xpath("..//label[@data-value='" + value + "']"));

        // Раскрываем фильтр
        aria.click();
        // На случай если есть скролл в списке значени
        Actions builder = new Actions(driver);
        builder.moveToElement(element).perform();
        element.click();

        // Закрываем фильтр
        aria.click();
    }

}
