package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс описывающий страницу с детальной информацией о выступлении
 */
public class VideoDetailedPage extends AbstractPage {

    /** Информация о названии */
    @FindBy(css = "main h1.evnt-talk-title")
    public WebElement title;

    /** Информация о языке */
    @FindBy(css = "main section div.details-cell div.language span")
    public WebElement language;

    /** Информация о месте проведения */
    @FindBy(css = "main section div.details-cell div.location span")
    public WebElement location;

    /** Информация о категории */
    @FindBy(css = "main section div.details-cell div.topics label")
    public List<WebElement> topicList;

    /**
     * Конструктор - создание нового объекта
     */
    public VideoDetailedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Функция получения списка категорий из массива веб-элементов
     * @return возвращает строку (разделитель категорий запятая)
     */
    public String getTopics() {
        return topicList.stream()
                .map(item -> waitForElement(item).getText().trim())
                .collect(Collectors.joining(", "));
    }
}
