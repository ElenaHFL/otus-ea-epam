package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

/**
 * Класс описывающий страницу мероприятий
 */
public class EventsPage extends AbstractPage {

    /** Тег с данными о языке */
    public String placePath = "p[@class='online']";
    /** Тег с данными о языке */
    public String languagePath = "p[@class='language']";
    /** Тег с данными о названии */
    public String namePath = "div[@class='evnt-event-name']//span";
    /** Тег с данными о дате */
    public String datePath = "div[@class='evnt-event-dates']//span[@class='date']";
    /** Тег с данными о статусе */
    public String statusPath = "span[contains(@class,'status')]";
    /** Тег с данными о выступающих */
    public String speakersPath = "div[@class='evnt-speaker'][@data-name]";

    /** Список дополнительных выступающих */
    @FindBy(css = "div.tab-content div.evnt-events-column div.evnt-speaker.more span")
    public List<WebElement> speakerMoreField;

    /** Ссылка на Past Events */
    @FindBy(xpath = "//main//span[text()='Past Events']/..")
    public WebElement pastEventsLink;

    /** Значение счетчика на кнопке Upcoming Events */
    @FindBy(xpath = "//main//span[text()='Upcoming events']/../span[3]")
    public WebElement upcomingEventsCountByNumber;

    /** Значение счетчика на кнопке Past Events */
    @FindBy(xpath = "//main//span[text()='Past Events']/../span[3]")
    public WebElement pastEventsCountByNumber;

    /** Список карточек мероприятий */
    @FindBy(css = "div.tab-content div.evnt-events-column")
    public List<WebElement> eventsByCardList;

    /** Список карточек мероприятий за текущую неделю */
    @FindBys({
            @FindBy(css = "div.tab-content"),
            @FindBy(xpath = "//*[text()='This week']/.."),
            @FindBy(css = "div.evnt-events-column")
    })
    public List<WebElement> eventsWeekByCardList;

    /**
     * Конструктор - создание нового объекта
     */
    public EventsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Функция поиска потомков переданного в параметре веб-элемента
     * @param from - точка отсчета для поиска
     * @param value - часть локатора для поиска
     * @return список веб-элементов
     */
    public List<WebElement> getElements(WebElement from, String value) {
        return from.findElements(By.xpath("./descendant::" + value));
    }

    /**
     * Функция проверки расположения элементов
     * @param from - точка отсчета для поиска
     * @param value - часть локатора для поиска
     * @return true/false
     */
    public boolean isFollowing(WebElement from, String value) {
        return from.findElements(By.xpath("./following::" + value)).stream()
                .filter(item -> item.findElements(By.xpath("./ancestor::div[contains(@class,'evnt-cards-container')]/preceding-sibling::div[contains(@class,'evnt-cards-container')]")).size() == 0)
                .filter(item -> item.findElements(By.xpath("./ancestor::div[contains(@class,'evnt-events-column')]/preceding-sibling::div[contains(@class,'evnt-events-column')]")).size() == 0)
                .collect(Collectors.toList()).size() != 0;
    }

    /**
     * Функция для получения списка дат
     * @param element - веб-элемент, из которого будут извлекаться даты
     * @return список дат
     */
    public ArrayList<Date> getEventDates(WebElement element) throws ParseException {
        return collectDates(element.findElement(By.cssSelector("p span.date")).getText());
    }

    /**
     * Функция для подсчета количества спикеров (основные + дополнительные)
     * @param webElementList - список спикеров в виде веб-элементов
     * @return количество спикеров
     */
    public Integer getSpeakersCount(List<WebElement> webElementList) {
        Integer сount = webElementList.size();
        // Могут быть дополнительные спикеры (добавим их если есть)
        List<WebElement> speakerMore = speakerMoreField;
        if (speakerMore != null) {
            String more = speakerMore.get(0).getText();
            сount = сount + parseInt(more.replace("+", ""));
        }

        return сount;
    }

    @Step("Переход на страницу с подробной информацией о мероприятии, через клик по карточке")
    public EventDetailedPage navigateAndClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        element.click();
        waitForPageLoaded();
        Allure.addAttachment("Карточка мероприятия", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new EventDetailedPage(driver);
    }
}
