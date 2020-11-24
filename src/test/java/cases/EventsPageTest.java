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
import pages.EventDetailedPage;
import pages.EventsPage;
import pages.HomePage;
import utils.BaseHooks;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
@Slf4j
public class EventsPageTest extends BaseHooks {

    /** Логгер */
    private static final Logger logger = LoggerFactory.getLogger(VideoPageTest.class);

    @Test
    @DisplayName("test_1_viewUpcomingEvents")
    @Epic("EPAM")
    @Feature("Мероприятия")
    @Story("Просмотр предстоящих мероприятий")
    @Description("Тест проверяет корректность отображения списка предстоящих мероприятий")
    public void viewUpcomingEventsTest() {
        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openHomeByTab();

        // Пользователь нажимает на Upcoming Events
        EventsPage eventPage = homePage.openEventsByLink();

        // На странице отображаются карточки предстоящих мероприятий. Количество карточек равно счетчику на кнопке Upcoming Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = eventPage.upcomingEventsCountByNumber.getText();
        // Считаем сколько карточек
        Integer countByCard = eventPage.eventsByCardList.size();

        logger.info(String.format("Счетчик показывает %s, а карточек нашли %s", countByNumber, countByCard));

        // Проверяем равенство значений
        assertTrue(countByNumber.equals(countByCard.toString()), "Количество карточек НЕ равно счетчику на кнопке Upcoming Events");
    }

    @Test
    @DisplayName("test_2_viewingEventCards")
    @Epic("EPAM")
    @Feature("Мероприятия")
    @Story("Просмотр карточек мероприятий")
    @Description("Тест проверяет отображение информации на карточках мероприятий")
    public void viewingEventCardsTest() {
        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openHomeByTab();

        // Пользователь нажимает на Upcoming Events
        EventsPage eventPage = homePage.openEventsByLink();

        // На странице отображаются карточки предстоящих мероприятий
        if (eventPage.eventsByCardList.size() > 0) {
            // В карточке указана информация о мероприятии
            // Важно проверить порядок отображаемых блоков с информацией в карточке мероприятия
            // • место проведения, язык
            // • название мероприятия, дата мероприятия, информация о регистрации
            // • список спикеров
            WebElement card = eventPage.eventsByCardList.get(0);

            WebElement place = eventPage.getElements(card, eventPage.placePath).get(0);
            WebElement language = eventPage.getElements(card, eventPage.languagePath).get(0);
            WebElement name = eventPage.getElements(card, eventPage.namePath).get(0);
            WebElement date = eventPage.getElements(card, eventPage.datePath).get(0);
            WebElement status = eventPage.getElements(card, eventPage.statusPath).get(0);
            List<WebElement> speakers = eventPage.getElements(card, eventPage.speakersPath);
            Integer speaker = eventPage.getSpeakersCount(speakers);

            logger.info(new StringBuilder()
                    .append("Карточки предстоящих мероприятий отображаются. Информация по первой:")
                    .append("\nМесто проведения = " + place.getText())
                    .append("\nЯзык = " + language.getText())
                    .append("\nДата = " + date.getText())
                    .append("\nРегистрация = " + status.getText())
                    .append("\nВсего спикеров = " + speaker)
                    .toString());

            assertTrue(place.isDisplayed(), "Место проведения НЕ отображается на карточке");
            assertTrue(language.isDisplayed(), "Язык НЕ отображается на карточке");
            assertTrue(name.isDisplayed(), "Название мероприятия НЕ отображается на карточке");
            assertTrue(date.isDisplayed(), "Дата мероприятия НЕ отображается на карточке");
            assertTrue(status.isDisplayed(), "Информация о регистрации НЕ отображается на карточке");
            assertTrue(speakers.get(0).isDisplayed(), "Список спикеров НЕ отображается на карточке");

            assertTrue(eventPage.isFollowing(place, eventPage.languagePath), "Информации о языке НЕ расположена ниже места проведения");
            assertTrue(eventPage.isFollowing(language, eventPage.namePath), "Информации о названии НЕ расположена ниже информации о языке");
            assertTrue(eventPage.isFollowing(name, eventPage.datePath), "Информации о дате НЕ расположена ниже информации о названии");
            assertTrue(eventPage.isFollowing(date, eventPage.statusPath), "Информации о регистрации НЕ расположена ниже информации о дате");
            assertTrue(eventPage.isFollowing(status, eventPage.speakersPath), "Информации о спикерах НЕ расположена ниже информации о регистрации");

        } else {
            logger.warn("На странице нет ни одной карточки предстоящих мероприятий");
        }
    }

    @Test
    @DisplayName("test_3_validatingDatesForUpcomingEvents")
    @Epic("EPAM")
    @Feature("Мероприятия")
    @Story("Валидация дат предстоящих мероприятий")
    @Description("Тест проверяет, что даты предстоящих мероприятий больше текущей даты")
    public void validatingDatesForUpcomingEventsTest() throws ParseException {
        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openHomeByTab();

        // Пользователь нажимает на Upcoming Events
        EventsPage eventPage = homePage.openEventsByLink();

        // На странице отображаются карточки предстоящих мероприятий
        // В блоке This week даты проведения мероприятий больше или равны текущей дате и находятся в пределах текущей недели
        // Если блока This week нет, сравним все что есть
        List<WebElement> cards = eventPage.eventsWeekByCardList.size() == 0 ? eventPage.eventsByCardList : eventPage.eventsWeekByCardList;
        if (cards.size() > 0) {

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

    @Test
    @DisplayName("test_4_viewPastEventsInCanada")
    @Epic("EPAM")
    @Feature("Мероприятия")
    @Story("Просмотр прошедших мероприятий в Канаде")
    @Description("Тест проверяет отображение прошедших мероприятий в Канаде")
    public void viewPastEventsInCanadaTest() throws ParseException {

        /** Данные для теста */
        String country = "Canada";

        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openHomeByTab();

        // Пользователь переходит на вкладку Events
        EventsPage eventPage = homePage.openEventsByTab();

        // Пользователь нажимает на Past Events
        eventPage.pastEventsLink.click();

        // Пользователь нажимает на Location в блоке фильтров и выбирает Canada в выпадающем списке
        eventPage.setFilter(eventPage.locationFilter, country);

        // На странице отображаются карточки прошедших мероприятий. Количество карточек равно счетчику на кнопке Past Events
        // Считываем значение счетчика на кнопке Upcoming Events
        String countByNumber = eventPage.pastEventsCountByNumber.getText();
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

    @Test
    @DisplayName("test_5_viewingDetailedInformationAboutTheEvent")
    @Epic("EPAM")
    @Feature("Мероприятия")
    @Story("Просмотр детальной информации о мероприятии")
    @Description("Тест проверяет отображение детальной информации о мероприятии")
    public void viewingDetailedInformationAboutTheEventTest() {
        HomePage homePage = new HomePage(driver);
        homePage.open(baseUrl);

        // Пользователь переходит на вкладку events
        homePage.openHomeByTab();

        // Пользователь нажимает на Upcoming Events
        EventsPage eventPage = homePage.openEventsByLink();

        // Пользователь нажимает на любую карточку
        // Происходит переход на страницу с подробной информацией о мероприятии
        List<WebElement> eventList = eventPage.eventsByCardList;
        Integer randomIndex = (int)(Math.random()*eventList.size());
        EventDetailedPage eventDetailedPage = eventPage.navigateAndClick(eventList.get(randomIndex));

        // На странице с информацией о мероприятии отображается блок с кнопкой для регистрации, дата и время, программа мероприятия
        WebElement register = eventDetailedPage.register;
        String date = eventDetailedPage.date.getText();
        Integer count = eventDetailedPage.agendaList.size();

        logger.info(String.format("У выбранного события (в списке '%s'й) дата проведения '%s', в программе блоков '%s'", randomIndex + 1, date, count));

        assertTrue(register.isDisplayed(), "Кнопка регистрации НЕ отображается");
        assertTrue(!date.equalsIgnoreCase(""), "Дата и время мероприятия отсутствуют");
        assertTrue(count > 0, "Программа мероприятия пустая");
    }

}
