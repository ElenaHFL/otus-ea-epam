package cases;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.*;
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
        homePage.homeLink.click();

        // Пользователь нажимает на Upcoming Events
        homePage.upcomingEventsLink.click();

        // На странице отображаются карточки предстоящих мероприятий. Количество карточек равно счетчику на кнопке Upcoming Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = waitForElement(eventPage.upcomingEventsCountByNumber).getText();

        // Считаем сколько карточек
        Integer countByCard = eventPage.eventsByCardList.size();

        logger.info(String.format("Счетчик показывает %s, а карточек нашли %s", countByNumber, countByCard));

        // Проверяем равенство значений
        assertTrue(countByNumber.equals(countByCard.toString()), "Количество карточек НЕ равно счетчику на кнопке Upcoming Events");
    }

    // Просмотр карточек мероприятий
    @Test
    public void viewingEventCardsTest() {
        HomePage homePage = new HomePage(driver);
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.homeLink.click();

        // Пользователь нажимает на Upcoming Events
        homePage.upcomingEventsLink.click();

        // На странице отображаются карточки предстоящих мероприятий
        if (eventPage.eventsByCardList.size() > 0) {
            // В карточке указана информация о мероприятии
            // Важно проверить порядок отображаемых блоков с информацией в карточке мероприятия
            // • место проведения, язык
            // • название мероприятия, дата мероприятия, информация о регистрации
            // • список спикеров
            // TODO: подумать как проверять порядок отдельно, наличие элементов отдельно
            WebElement place = eventPage.placeField;
            WebElement language = eventPage.getFollowingElements(place, eventPage.languagePath).get(0);
            WebElement name = eventPage.getFollowingElements(language, eventPage.namePath).get(0);
            WebElement date = eventPage.getFollowingElements(name, eventPage.datePath).get(0);
            WebElement status = eventPage.getFollowingElements(date, eventPage.statusPath).get(0);
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
        homePage.homeLink.click();

        // Пользователь нажимает на Upcoming Events
        homePage.upcomingEventsLink.click();

        // На странице отображаются карточки предстоящих мероприятий
        List<WebElement> cards = eventPage.eventsByCardList;
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
        EventsPage eventPage = new EventsPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку Events
        homePage.eventsLink.click();

        // Пользователь нажимает на Past Events
        waitForElement(eventPage.pastEventsLink).click();

        // Пользователь нажимает на Location в блоке фильтров и выбирает Canada в выпадающем списке
        eventPage.setFilter(eventPage.locationFilter,country);

        // На странице отображаются карточки прошедших мероприятий. Количество карточек равно счетчику на кнопке Past Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = waitForElement(eventPage.pastEventsCountByNumber).getText();
        // Считаем сколько карточек
        Integer countByCard = eventPage.eventsByCardList.size();

        logger.info(String.format("Счетчик показывает %s, а карточек нашли %s", countByNumber, countByCard));

        // Проверяем равенство значений
        assertTrue(countByNumber.equals(countByCard.toString()), "Количество карточек НЕ равно счетчику на кнопке Past Events");

        // Даты проведенных мероприятий меньше текущей даты
        // На странице отображаются карточки предстоящих мероприятий
        List<WebElement> cards = eventPage.eventsByCardList;
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
        EventDetailedPage eventDetailedPage = new EventDetailedPage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.homeLink.click();

        // Пользователь нажимает на Upcoming Events
        homePage.upcomingEventsLink.click();

        // Пользователь нажимает на любую карточку
        eventPage.eventsByCardList.get(0).click();

        // Происходит переход на страницу с подробной информацией о мероприятии
        // TODO: подумать как валидировать и надо ли?

        // На странице с информацией о мероприятии отображается блок с кнопкой для регистрации, дата и время, программа мероприятия
        // TODO: подумать как валидировать их наличие
        waitForElement(eventDetailedPage.register).isDisplayed();
        String date = eventDetailedPage.date.getText();
        Integer count = eventDetailedPage.agendaList.size();
        logger.info(String.format("У первого события в сприске дата проведения %s, в программе %s блоков", date, count));
    }

    // Фильтрация докладов по категориям
    @Test
    public void filteringReportsByCategoryTest() {
        String country = "Belarus";
        String speech = "ENGLISH";
        String category = "Testing";

        HomePage homePage = new HomePage(driver);
        VideoPage videoPage = new VideoPage(driver);
        VideoDetailedPage videoDetailedPage = new VideoDetailedPage(driver);

        homePage.open(baseUrl);

        // Пользователь переходит на вкладку Video
        homePage.videoLink.click();

        // Пользователь нажимает на More Filters
        videoPage.moreFilters.click();

        // Пользователь выбирает: Category – Testing, Location – Belarus, Language – English, На развернутой вкладке фильтров
        videoPage.setFilter(videoPage.locationFilter,country);
        videoPage.setFilter(videoPage.languageFilter,speech);
        videoPage.setFilter(videoPage.categoryFilter,category);

        // На странице отображаются карточки соответствующие правилам выбранных фильтров
        List<WebElement> talks = videoPage.talkСardList;
        ArrayList<String> urls = new ArrayList<>();
        for (WebElement talk : talks){
            urls.add(videoPage.getLinkFromElement(talk));
        }

        for (String url : urls) {
            // Переходим на страницу с деталями записи
            driver.get(url);

            // Получаем информацию о записи
            String language = videoDetailedPage.language.getText();
            String location = videoDetailedPage.location.getText();
            List<WebElement> topics = videoDetailedPage.topicList;
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
        VideoPage videoPage = new VideoPage(driver);
        VideoDetailedPage videoDetailedPage = new VideoDetailedPage(driver);

        homePage.open(baseUrl);

        // Пользователь переходит на вкладку Video
        homePage.videoLink.click();

        // Пользователь вводит ключевое слово QA в поле поиска
        videoPage.search.sendKeys(word);

        // На странице отображаются доклады, содержащие в названии ключевое слово поиска
        List<WebElement> talks = videoPage.talkСardList;
        ArrayList<String> urls = new ArrayList<>();

        // Проверяем доклады на соотв. условиям поиска
        for (WebElement talk : talks){
            String name = videoPage.getTalkNameFromCard(talk);
            if (name.contains(word)){
                logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", name, word));
            }else{
                // Возможно название не поместилось полностью на карточке, надо проверить на странице с ее описание, собираем url-ы таких докладов
                urls.add(videoPage.getLinkFromElement(talk));
            }
        }

        for (String url : urls) {
            // Переходим на страницу с деталями записи
            driver.get(url);

            // Получаем информации о названии
            String title = videoDetailedPage.title.getText();

            logger.info(String.format("Доклад с названием '%s' содержит слово '%s'", title, word));
            assertTrue(title.contains(word),"Доклад с названием НЕ соответсвует условиям поиска");
        }
    }

}
