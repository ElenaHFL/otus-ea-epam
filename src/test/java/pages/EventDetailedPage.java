package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

/**
 * Класс описывающий страницу с детальной информацией о мероприятии
 */
public class EventDetailedPage extends AbstractPage {

    /** Информацию о программе */
    @FindBys({
            @FindBy(id = "agenda"),
            @FindBy(css = "section div.agenda-time")
    })
    public List<WebElement> agendaList;

    /** Информацию о регистрации */
    @FindBys({
            @FindBy(id = "home"),
            @FindBy(xpath = "//button[text()='Register']")
    })
    public WebElement register;

    /** Информацию о дате проведения */
    @FindAll({
            @FindBy(css = "div.evnt-details span.date"),
            @FindBy(xpath = "//section//i[contains(@class,'fa-calendar')]/ancestor::*[@class='evnt-icon-point']//*[@class='evnt-icon-info']/*[last()]")
    })
    public WebElement date;

    /**
     * Конструктор - создание нового объекта
     */
    public EventDetailedPage(WebDriver driver) {
        super(driver);
    }

}
