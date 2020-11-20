package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.BaseHooks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class EventsPage extends AbstractPage {

    /** Регулярное выражение для выделения дат */
    private final Pattern DATE_PATTERN = Pattern.compile("\\s*(\\d{1,2})\\s*-\\s*(\\d{1,2})(\\s*\\D{3}\\s*\\d{4})");
    /** Формат дат */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    public String languagePath = "p[@class='language']";
    public String namePath = "div[@class='evnt-event-name']//span";
    public String datePath = "div[@class='evnt-event-dates']//span[@class='date']";
    public String statusPath = "span[contains(@class,'status')]";
    public String speakersPath = "div[@class='evnt-speaker'][data-name]";

    @FindBy(css = "div.tab-content div.evnt-events-column p.online")
    public WebElement placeField;

    @FindBy(css = "div.tab-content div.evnt-events-column div.evnt-speaker.more span")
    public List<WebElement> speakerMoreField;

    public EventsPage(WebDriver driver) {
        super(driver);
    }

    // Пользователь переходит на вкладку events
    public void openByTab() {
        driver.findElement(By.xpath("//header//a[text()='Events']")).click();
    }

    // Пользователь нажимает на Past Events
    public void openPastEvents() {
        waitForElement(driver.findElement(By.xpath("//main//span[text()='Past Events']/.."))).click();
    }

    // Получили значение счетчика на кнопке Upcoming Events
    public String getUpcomingEventsCountByNumber() {
        return waitForElement(driver.findElement(By.xpath("//main//span[text()='Upcoming events']/../span[3]"))).getText();
    }

    // Получили значение счетчика на кнопке Upcoming Events
    public String getPastEventsCountByNumber() {
        return waitForElement(driver.findElement(By.xpath("//main//span[text()='Past Events']/../span[3]"))).getText();
    }

    // Получили количество карточек мероприятий
    public Integer getEventsCountByCard() {
        return driver.findElements(By.cssSelector("div.tab-content div.evnt-events-column")).size();
    }

    // Получили список карточек предстоящих мероприятий
    public List<WebElement> getEventsCards() {
        return driver.findElements(By.cssSelector("div.tab-content div.evnt-events-column"));
    }

    // Найти элемент, с учетом порядка следования
    public WebElement getFollowingElement(WebElement from, String value) {
        return from.findElement(By.xpath("//following::" + value));
    }

    // Найти список элементов, с учетом порядка следования
    public List<WebElement> getFollowingElements(WebElement from, String value) {
        return from.findElements(By.xpath("//following::" + value));
    }

    // Посчитать количество спикеров
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

    // Посчитать количество спикеров
    public ArrayList<Date> getEventDates(WebElement element) throws ParseException {
        String dateEvent = element.findElement(By.cssSelector("p span.date")).getText();
        ArrayList<Date> dates = new ArrayList<>();

        Matcher matcher = DATE_PATTERN.matcher(dateEvent);
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
            Date date = DATE_FORMAT.parse(dateEvent);
            dates.add(date);
        }

        return dates;
    }

    // Установить значение в фильтр Location
    public void setLocationFilter(String value) {
        BaseHooks.setFilter("filter_location", value);
    }

    // Получили информацию из блока регистрации
    public WebElement getRegisterInfo() {
        return waitForElement(driver.findElement(By.id("home")).findElement(By.xpath("//button[text()='Register']")));
    }

    // Получили информацию из блока с датой проведения
    public String getDateInfo() {
        //TODO: придумать лучше локатор
        return waitForElement(driver.findElement(By.xpath("//section//i[contains(@class,'fa-calendar')]/../../..//h4"))).getText();
    }

    // Получили информацию из блока программы
    public List<WebElement> getAgendaInfo() {
        return driver.findElement(By.id("agenda")).findElements(By.cssSelector("section div.agenda-time"));
    }
}
