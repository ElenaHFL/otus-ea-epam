package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import utils.BaseHooks;

import java.util.List;

public class VideoPage extends AbstractPage {

    @FindBy(css = "main section div.details-cell div.language span")
    public WebElement language;

    @FindBy(css = "main section div.details-cell div.location span")
    public WebElement location;

    @FindBy(css = "main section div.details-cell div.topics label")
    public List<WebElement> topics;

    @FindBy(css = "main h1.evnt-talk-title")
    public WebElement title;

    public VideoPage(WebDriver driver) {
        super(driver);
    }

    // Пользователь переходит на вкладку Video
    public void openByTab() {
        driver.findElement(By.xpath("//header//a[text()='Video']")).click();
    }

    // Установить значение в фильтр Location
    public void setLocationFilter(String value) {
        BaseHooks.setFilter("filter_location", value);
    }

    // Установить значение в фильтр Language
    public void setLanguageFilter(String value) {
        BaseHooks.setFilter("filter_language", value);
    }

    // Установить значение в фильтр Language
    public void setCategoryFilter(String value) {
        driver.findElement(By.id("filter_category")).click();
        WebElement popUp = driver.findElement(By.cssSelector("div[aria-labelledby='filter_category'] div.evnt-filter-menu-scroll"));
        WebElement el = driver.findElement(By.cssSelector("div[aria-labelledby='filter_category'] label[data-value='" + value + "']"));

        Actions builder = new Actions(driver);
        builder.moveToElement(el);
        builder.perform();
        el.click();

        driver.findElement(By.id("filter_category")).click();
    }

    // Установить значение в фильтр Language
    public void setSearch(String value) {
        WebElement filter = driver.findElement(By.cssSelector("section.evnt-filters-panel"));
        filter.findElement(By.cssSelector("input.evnt-search")).sendKeys(value);
    }

    // Пользователь нажимает на More Filters
    public void expandMoreFilter(){
        WebElement filter = driver.findElement(By.cssSelector("section.evnt-filters-panel"));
        filter.findElement(By.xpath("//span[text()='More Filters']")).click();
    }

    // Получили список выступлений
    public List<WebElement> getTalksCards() {
        return driver.findElements(By.cssSelector("section.evnt-talks-panel div.evnt-talk-card"));
    }

    // Получили ссылку из элемента
    public String getLinkFromElement(WebElement element) {
        return element.findElement(By.cssSelector("a")).getAttribute("href");
    }

    // Получили ссылку из элемента
    public String getTalkNameFromCard(WebElement element) {
        return element.findElement(By.cssSelector("div.evnt-talk-name span")).getText();
    }

}
