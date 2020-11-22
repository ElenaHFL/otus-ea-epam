package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.ByteArrayInputStream;

/**
 * Класс описывающий домашнюю страницу
 */
public class HomePage extends AbstractPage {

    /** Ссылка на домашнюю страницу */
    @FindBy(css = "header span.evnt-logo")
    public WebElement homeLink;

    /** Ссылка на вкладку Events */
    @FindBy(xpath = "//header//a[text()='Events']")
    public WebElement eventsLink;

    /** Ссылка на вкладку Video */
    @FindBy(xpath = "//header//a[text()='Video']")
    public WebElement videoLink;

    /** Ссылка на Upcoming Events */
    @FindBy(xpath = "//main//a[text()='Upcoming events']")
    public WebElement upcomingEventsLink;

    /**
     * Конструктор - создание нового объекта
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Открытие страницы - '{baseUrl}'")
    public void open(String baseUrl) {
        driver.get(baseUrl);
        waitForPageLoaded();
        Allure.addAttachment("Главная страница EPAM", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Переход на домашнюю страницу через клик по логотипу")
    public HomePage openHomeByTab() {
        waitForElement(homeLink).click();
        waitForPageLoaded();
        Allure.addAttachment("Домашняя страница EPAM", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return this;
    }

    @Step("Переход по ссылке Upcoming Events на вкладку Events")
    public EventsPage openEventsByLink() {
        waitForElement(upcomingEventsLink).click();
        waitForPageLoaded();
        Allure.addAttachment("Вкладка Events", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new EventsPage(driver);
    }

    @Step("Переход на вкладку Events")
    public EventsPage openEventsByTab() {
        waitForElement(eventsLink).click();
        waitForPageLoaded();
        Allure.addAttachment("Вкладка Events", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new EventsPage(driver);
    }

    @Step("Переход на вкладку Video")
    public VideoPage openVideoByTab() {
        waitForElement(videoLink).click();
        waitForPageLoaded();
        Allure.addAttachment("Вкладка Video", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        return new VideoPage(driver);
    }

}
