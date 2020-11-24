package cases;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.HomePage;
import pages.VideoDetailedPage;
import pages.VideoPage;
import utils.BaseHooks;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
@Slf4j
public class VideoPageTest extends BaseHooks {

    /** Логгер */
    private static final Logger logger = LoggerFactory.getLogger(VideoPageTest.class);

    @Test
    @DisplayName("test_6_filteringReportsByCategory")
    @Epic("EPAM")
    @Feature("Доклады")
    @Story("Фильтрация докладов по категориям")
    @Description("Тест проверяет работу фильтра на вкладке с докладами")
    public void filteringReportsByCategoryTest() {

        /** Данные для теста */
        String country = "Belarus";
        String speech = "ENGLISH";
        String category = "Testing";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);
        VideoDetailedPage videoDetailedPage = new VideoDetailedPage(driver);

        // Пользователь переходит на вкладку Video
        VideoPage videoPage = homePage.openVideoByTab();

        // Пользователь нажимает на More Filters
        videoPage.moreFilters.click();

        // Пользователь выбирает: Category – Testing, Location – Belarus, Language – English, На развернутой вкладке фильтров
        videoPage.setFilter(videoPage.locationFilter, country);
        videoPage.setFilter(videoPage.languageFilter, speech);
        videoPage.setFilterWithScroll(videoPage.categoryFilter, category);

        // На странице отображаются карточки соответствующие правилам выбранных фильтров
        // Пробежимся по страницам с детальной информацией отображающихся записей
        for (String url : videoPage.getUrlList()) {
            // Переходим на страницу с деталями записи
            homePage.open(url);

            // Получаем информацию о записи
            String language = videoDetailedPage.waitForElement(videoDetailedPage.language).getText();
            String location = videoDetailedPage.location.getText();
            String categories = videoDetailedPage.getTopics();

            logger.info(String.format("Информация о записи: язык = '%s', место = '%s', категория = '%s'", language, location, categories));

            assertTrue(language.contains(speech),"Язык записи НЕ соответсвует указанному в фильтре");
            assertTrue(location.contains(country),"Место записи НЕ соответсвует указанному в фильтре");
            assertTrue(categories.contains(category),"Категория записи НЕ соответсвует указанному в фильтре");
        }
    }

    @Test
    @DisplayName("test_7_searchReportsByKeyword")
    @Epic("EPAM")
    @Feature("Доклады")
    @Story("Поиск докладов по ключевому слову")
    @Description("Тест проверяет работу поиска на вкладке с докладами")
    public void searchReportsByKeywordTest() throws InterruptedException {

        /** Данные для теста */
        String word = "QA";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);
        VideoDetailedPage videoDetailedPage = new VideoDetailedPage(driver);

        // Пользователь переходит на вкладку Video
        VideoPage videoPage = homePage.openVideoByTab();

        // Пользователь вводит ключевое слово QA в поле поиска
        videoPage.setSearch(word);

        // На странице отображаются доклады, содержащие в названии ключевое слово поиска
        ArrayList<String> urls = new ArrayList<>();

        // Проверяем доклады на соотв. условиям поиска
        for (WebElement talkName : videoPage.talkСardNameList){
            String name = talkName.getText();
            if (name.contains(word)){
                logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", name, word));
            }else{
                // Возможно название не поместилось полностью на карточке, надо проверить на странице с ее описание, собираем url-ы таких докладов
                urls.add(videoPage.getLinkFromElement(talkName));
            }
        }

        // Проверяем детальные карточки докладов, у которых на свернутой форме не было обнаружено ключевое слово поиска
        for (String url : urls) {
            // Переходим на страницу с деталями записи
            homePage.open(url);

            // Получаем информации о названии
            String title = videoDetailedPage.title.getText();

            logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", title, word));
            assertTrue(title.contains(word),"Доклад с названием НЕ соответсвует условиям поиска");
        }
    }

}
