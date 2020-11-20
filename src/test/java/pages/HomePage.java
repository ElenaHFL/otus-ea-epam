package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractPage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Открытие страницы
    public void open(String baseUrl) {
        driver.get(baseUrl);
    }

    // Пользователь переходит на вкладку events
    public void openByTab() {
        driver.findElements(By.xpath("//header//span.evnt-logo"));
    }

    // Пользователь нажимает на Upcoming Events
    public void openUpcomingEvents() {
        driver.findElement(By.xpath("//main//a[text()='Upcoming events']")).click();
    }
}
