package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class EventsPage extends AbstractPage {

    public String languagePath = "p[@class='language']";
    public String namePath = "div[@class='evnt-event-name']//span";
    public String datePath = "div[@class='evnt-event-dates']//span[@class='date']";
    public String statusPath = "span[contains(@class,'status')]";
    public String speakersPath = "div[@class='evnt-speaker'][data-name]";

    @FindBy(css = "div.tab-content div.evnt-events-column p.online")
    public WebElement placeField;

    @FindBy(css = "div.tab-content div.evnt-events-column div.evnt-speaker.more span")
    public List<WebElement> speakerMoreField;

    // Получили ссылку на Past Events
    @FindBy(xpath = "//main//span[text()='Past Events']/..")
    public WebElement pastEventsLink;

    // Получили значение счетчика на кнопке Upcoming Events
    @FindBy(xpath = "//main//span[text()='Upcoming events']/../span[3]")
    public WebElement upcomingEventsCountByNumber;

    // Получили значение счетчика на кнопке Past Events
    @FindBy(xpath = "//main//span[text()='Past Events']/../span[3]")
    public WebElement pastEventsCountByNumber;

    // Получили количество карточек мероприятий
    @FindBy(css = "div.tab-content div.evnt-events-column")
    public List<WebElement> eventsByCardList;

    // Получили количество карточек мероприятий за текущую неделю
    @FindBys({
            @FindBy(css = "div.tab-content"),
            @FindBy(xpath = "//*[text()='This week']/.."),
            @FindBy(css = "div.evnt-events-column")
    })
    public List<WebElement> eventsWeekByCardList;

    // Получили информацию о категории
    @FindBy(css = "main section div.details-cell div.topics label")
    public List<WebElement> topicList;

    public EventsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Найти список элементов, с учетом порядка следования")
    public List<WebElement> getFollowingElements(WebElement from, String value) {
        return from.findElements(By.xpath("//following::" + value));
    }

    @Step("Получить список дат")
    public ArrayList<Date> getEventDates(WebElement element) throws ParseException {
        return collectDates(element.findElement(By.cssSelector("p span.date")).getText());
    }

    @Step("Посчитать количество спикеров")
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

}
