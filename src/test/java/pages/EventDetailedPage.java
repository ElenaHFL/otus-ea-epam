package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

public class EventDetailedPage extends AbstractPage {

    // Получили информацию о программе
    @FindBys({
            @FindBy(id = "agenda"),
            @FindBy(css = "section div.agenda-time")
    })
    public List<WebElement> agendaList;

    // Получили информацию о регистрации
    @FindBys({
            @FindBy(id = "home"),
            @FindBy(xpath = "//button[text()='Register']")
    })
    public WebElement register;

    // Получили информацию о дате проведения

    @FindAll({
            @FindBy(css = "div.evnt-details span.date"),
            @FindBy(xpath = "//section//i[contains(@class,'fa-calendar')]/ancestor::*[@class='evnt-icon-point']//*[@class='evnt-icon-info']/*[last()]")
    })
    public WebElement date;

    public EventDetailedPage(WebDriver driver) {
        super(driver);
    }

}
