package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.ByteArrayInputStream;

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

    @Step("Открытие страницы {baseUrl}")
    public void open(String baseUrl) {
        driver.get(baseUrl);
        Allure.addAttachment("Главная страница EPAM", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Переход на домашнюю страницу через клик по логотипу")
    public HomePage openHomeByTab() {
        waitForElement(homeLink).click();
        Allure.addAttachment("Домашняя страница EPAM", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return this;
    }

    @Step("Переход по ссылке Upcoming Events на вкладку Events")
    public EventsPage openEventsByLink() {
        waitForElement(upcomingEventsLink).click();
        Allure.addAttachment("Вкладка Events", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new EventsPage(driver);
    }

    @Step("Переход на вкладку Events")
    public EventsPage openEventsByTab() {
        waitForElement(eventsLink).click();
        Allure.addAttachment("Вкладка Events", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new EventsPage(driver);
    }

    @Step("Переход на вкладку Video")
    public VideoPage openVideoByTab() {
        waitForElement(videoLink).click();
        Allure.addAttachment("Вкладка Video", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new VideoPage(driver);
    }

}
