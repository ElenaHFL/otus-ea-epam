package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    // Получили ссылку на Home
    @FindBy(css = "header span.evnt-logo")
    public WebElement homeLink;

    // Получили ссылку на вкладку Events
    @FindBy(xpath = "//header//a[text()='Events']")
    public WebElement eventsLink;

    // Получили ссылку на вкладку Video
    @FindBy(xpath = "//header//a[text()='Video']")
    public WebElement videoLink;

    // Получили ссылку на Upcoming Events
    @FindBy(xpath = "//main//a[text()='Upcoming events']")
    public WebElement upcomingEventsLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Открытие страницы
    public void open(String baseUrl) {
        driver.get(baseUrl);
    }
}
