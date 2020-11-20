package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

public class VideoPage extends AbstractPage {

    // Получили список выступлений
    @FindBy(css = "section.evnt-talks-panel div.evnt-talk-card div.evnt-talk-name span")
    public List<WebElement> talkСardNameList;

    // Получили список выступлений
    @FindBy(css = "section.evnt-filters-panel .evnt-results-cell span")
    public WebElement foundResults;

    // Получили зону поиска
    @FindBys({
            @FindBy(css = "section.evnt-filters-panel"),
            @FindBy(css = "input.evnt-search")
    })
    public WebElement search;

    public VideoPage(WebDriver driver) {
        super(driver);
    }

    // Получили ссылку из элемента
    public String getLinkFromElement(WebElement element) {
        return waitForElement(element.findElement(By.xpath("./ancestor::a[@href]"))).getAttribute("href");
    }

    // Получили ссылку из элемента
    public void setSearch(String value) throws InterruptedException {
        String countBefore = foundResults.getText();
        // Пользователь вводит ключевое слово QA в поле поиска
        waitForElement(search).sendKeys(value);
        // Подождем пока фильтр применится, ориентируемся на изменение числа записей
        while(foundResults.getText().equals(countBefore)){
            Thread.sleep(500);
        }
    }
}
