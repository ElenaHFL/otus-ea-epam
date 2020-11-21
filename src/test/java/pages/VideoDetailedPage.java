package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class VideoDetailedPage extends AbstractPage {

    // Получили информацию о названии
    @FindBy(css = "main h1.evnt-talk-title")
    public WebElement title;

    // Получили информацию о языке
    @FindBy(css = "main section div.details-cell div.language span")
    public WebElement language;

    // Получили информацию о месте проведения
    @FindBy(css = "main section div.details-cell div.location span")
    public WebElement location;

    // Получили информацию о категории
    @FindBy(css = "main section div.details-cell div.topics label")
    public List<WebElement> topicList;

    public VideoDetailedPage(WebDriver driver) {
        super(driver);
    }

    //Собрали в строку все категории записи
    public String getTopics() {
        return topicList.stream()
                .map(item -> waitForElement(item).getText().trim())
                .collect(Collectors.joining(", "));
    }
}
