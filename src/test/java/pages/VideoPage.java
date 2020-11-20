package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class VideoPage extends AbstractPage {

    // Получили список выступлений
    @FindBy(css = "section.evnt-talks-panel div.evnt-talk-card")
    public List<WebElement> talkСardList;

    // Получили ссылку из элемента
    public String getLinkFromElement(WebElement element) {
        return element.findElement(By.cssSelector("a")).getAttribute("href");
    }

    // Получили ссылку из элемента
    public String getTalkNameFromCard(WebElement element) {
        return element.findElement(By.cssSelector("div.evnt-talk-name span")).getText();
    }

    public VideoPage(WebDriver driver) {
        super(driver);
    }

}
