package cases;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.EventsPage;
import pages.HomePage;
import pages.VideoPage;
import utils.BaseHooks;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PageObjectTest extends BaseHooks {

    private static final Logger logger = LoggerFactory.getLogger(PageObjectTest.class);
    String baseUrl = "https://events.epam.com";

    // Просмотр предстоящих мероприятий
    // TODO: Дебильные ожидания подгрузки элементов, сделать обертку
    @Test
    public void viewUpcomingEventsTest() {
        HomePage homePage = new HomePage(driver);
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openByTab();

        // Пользователь нажимает на Upcoming Events
        homePage.openUpcomingEvents();

        // На странице отображаются карточки предстоящих мероприятий. Количество карточек равно счетчику на кнопке Upcoming Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = eventPage.getUpcomingEventsCountByNumber();
        // Считаем сколько карточек
        String countByCard = eventPage.getEventsCountByCard().toString();

        logger.info(String.format("Счетчик показывает %s, а карточек нашли %s", countByNumber, countByCard));

        // Проверяем равенство значений
        assertTrue(countByNumber.equals(countByCard), "Количество карточек НЕ равно счетчику на кнопке Upcoming Events");
    }

    // Просмотр карточек мероприятий
    @Test
    public void viewingEventCardsTest() {
        HomePage homePage = new HomePage(driver);
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openByTab();

        // Пользователь нажимает на Upcoming Events
        homePage.openUpcomingEvents();

        // На странице отображаются карточки предстоящих мероприятий
        if (eventPage.getEventsCountByCard() > 0) {
            // В карточке указана информация о мероприятии
            // Важно проверить порядок отображаемых блоков с информацией в карточке мероприятия
            // • место проведения, язык
            // • название мероприятия, дата мероприятия, информация о регистрации
            // • список спикеров
            // TODO: подумать как проверять порядок отдельно, наличие элементов отдельно
            WebElement place = eventPage.placeField;
            WebElement language = eventPage.getFollowingElement(place, eventPage.languagePath);
            WebElement name = eventPage.getFollowingElement(language, eventPage.namePath);
            WebElement date = eventPage.getFollowingElement(name, eventPage.datePath);
            WebElement status = eventPage.getFollowingElement(date, eventPage.statusPath);
            List<WebElement> speakers = eventPage.getFollowingElements(status, eventPage.speakersPath);
            Integer speaker = eventPage.getSpeakersCount(speakers);

            // TODO: научиться красивей лог лепить, а то строка капец длинная и вытянутая
            logger.info(String.format("Карточки предстоящих мероприятий отображаются. Информация по первой:\n Место проведения '%s'\n Язык '%s'\n Название мероприятия '%s'\n Дата '%s'\n Регистрация '%s'\n Всего спикеров '%s'", place.getText(), language.getText(), name.getText(), date.getText(), status.getText(), speaker));
        } else {
            logger.warn("На странице нет ни одной карточки предстоящих мероприятий");
        }
    }

    // Валидация дат предстоящих мероприятий
    @Test
    public void validatingDatesForUpcomingEventsTest() throws ParseException {
        HomePage homePage = new HomePage(driver);
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openByTab();

        // Пользователь нажимает на Upcoming Events
        homePage.openUpcomingEvents();

        // На странице отображаются карточки предстоящих мероприятий
        List<WebElement> cards = eventPage.getEventsCards();
        if (cards.size() > 0) {

            // В блоке This week даты проведения мероприятий больше или равны текущей дате и находятся в пределах текущей недели
            // TODO: блока This week нет пока, сравним все что есть
            Date today = new Date();
            ArrayList<Date> dates;

            for(WebElement card : cards) {
                dates = eventPage.getEventDates(card);
                logger.info(String.format("Проверка дат: %s должна быть больше текущей %s", dates, today));

                for (Date dt : dates) {
                    assertTrue(dt.compareTo(today) > 0, "Дата предстоящего события меньше текущей");
                }
            }

        } else {
            logger.warn("На странице нет ни одной карточки предстоящих мероприятий");
        }
    }

    // Просмотр прошедших мероприятий в Канаде
    @Test
    public void viewPastEventsInCanadaTest() throws ParseException {
        String country = "Canada";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        EventsPage eventPage = new EventsPage(driver);
        eventPage.openByTab();

        // Пользователь нажимает на Past Events
        eventPage.openPastEvents();

        // Пользователь нажимает на Location в блоке фильтров и выбирает Canada в выпадающем списке
        eventPage.setLocationFilter(country);

        // На странице отображаются карточки прошедших мероприятий. Количество карточек равно счетчику на кнопке Past Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = eventPage.getPastEventsCountByNumber();
        // Считаем сколько карточек
        String countByCard = eventPage.getEventsCountByCard().toString();

        logger.info(String.format("Счетчик показывает %s, а карточек нашли %s", countByNumber, countByCard));

        // Проверяем равенство значений
        assertTrue(countByNumber.equals(countByCard), "Количество карточек НЕ равно счетчику на кнопке Past Events");

        // Даты проведенных мероприятий меньше текущей даты
        // На странице отображаются карточки предстоящих мероприятий
        List<WebElement> cards = eventPage.getEventsCards();
        if (cards.size() > 0) {

            Date today = new Date();
            ArrayList<Date> dates;

            for(WebElement card : cards) {
                dates = eventPage.getEventDates(card);
                logger.info(String.format("Проверка дат: %s должны быть меньше текущей %s", dates, today));

                for (Date dt : dates) {
                    assertTrue(dt.compareTo(today) < 0,"Дата прошедшего события БОЛЬШЕ текущей");
                }
            }

        } else {
            logger.warn("На странице нет ни одной карточки прошедших мероприятий");
        }
    }

    // Просмотр детальной информации о мероприятии
    @Test
    public void viewingDetailedInformationAboutTheEventTest() {
        HomePage homePage = new HomePage(driver);
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openByTab();

        // Пользователь нажимает на Upcoming Events
        homePage.openUpcomingEvents();

        // Пользователь нажимает на любую карточку
        eventPage.getEventsCards().get(0).click();

        // Происходит переход на страницу с подробной информацией о мероприятии
        // TODO: подумать как валидировать и надо ли?

        // На странице с информацией о мероприятии отображается блок с кнопкой для регистрации, дата и время, программа мероприятия
        // TODO: подумать как валидировать их наличие
        eventPage.getRegisterInfo().isDisplayed();
        String date = eventPage.getDateInfo();
        Integer count = eventPage.getAgendaInfo().size();
        logger.info(String.format("У первого события в сприске дата проведения %s, в программе %s блоков", date, count));
    }

    // Фильтрация докладов по категориям
    @Test
    public void filteringReportsByCategoryTest() {
        String country = "Belarus";
        String speech = "ENGLISH";
        String category = "Testing";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку Video
        VideoPage videoPage = new VideoPage(driver);
        videoPage.openByTab();

        // Пользователь нажимает на More Filters
        videoPage.expandMoreFilter();

        // Пользователь выбирает: Category – Testing, Location – Belarus, Language – English, На развернутой вкладке фильтров
        videoPage.setLocationFilter(country);
        videoPage.setLanguageFilter(speech);
        videoPage.setCategoryFilter(category);

        // На странице отображаются карточки соответствующие правилам выбранных фильтров
        List<WebElement> talks = videoPage.getTalksCards();
        ArrayList<String> urls = new ArrayList<>();
        for (WebElement talk : talks){
            urls.add(videoPage.getLinkFromElement(talk));
        }

        for (String url : urls) {
            // Переходим на страницу с деталями записи
            driver.get(url);

            // Получаем информацию о записи
            String language = videoPage.language.getText();
            String location = videoPage.location.getText();
            List<WebElement> topics = videoPage.topics;
            // TODO: лишний пробел спереди, переделать на красивый вариант
            String categories = "";
            for (WebElement topic : topics){
                categories = categories + " " + topic.getText();
            }

            logger.info(String.format("Информация о записи: язык = %s, место = %s, категория = %s", language, location, categories));

            assertTrue(language.contains(speech),"Язык записи НЕ соответсвует указанному в фильтре");
            assertTrue(location.contains(country),"Место записи НЕ соответсвует указанному в фильтре");
            assertTrue(categories.contains(category),"Категория записи НЕ соответсвует указанному в фильтре");
        }
    }

    // Поиск докладов по ключевому слову
    @Test
    public void searchReportsByKeywordTest() {
        String word = "QA";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку Video
        VideoPage videoPage = new VideoPage(driver);
        videoPage.openByTab();

        // Пользователь вводит ключевое слово QA в поле поиска
        videoPage.setSearch(word);

        // На странице отображаются доклады, содержащие в названии ключевое слово поиска
        List<WebElement> talks = videoPage.getTalksCards();
        ArrayList<String> urls = new ArrayList<>();

        // Проверяем доклады на соотв. условиям поиска
        for (WebElement talk : talks){
            String name = videoPage.getTalkNameFromCard(talk);
            if (name.contains(word)){
                logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", name, word));
            }else{
                // Возможно название не поместилось полностью на карточке, надо проверить на странице с ее описание, собираем url-ы таких докладов
                urls.add(talk.findElement(By.cssSelector("a")).getAttribute("href"));
            }
        }

        for (String url : urls) {
            // Переходим на страницу с деталями записи
            driver.get(url);

            // Получаем информации о названии
            String title = videoPage.title.getText();

            logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", title, word));
            assertTrue(title.contains(word),"Доклад с названием НЕ соответсвует условиям поиска");
        }
    }

}
