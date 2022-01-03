import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryOrderTest {
    String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldCardDeliveryOrderFormNotEarlierThan3DaysFromTheCurrentDate() {
        String planningDate = generateDate(3);
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Симферополь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), planningDate);
        $("[data-test-id='name'] input").setValue("Петрова Мария");
        $("[data-test-id='phone'] input").setValue("+79054875965");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $(".button").click();
        $("[data-test-id='notification'] .notification__title").shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate));

    }

    @Test
    void shouldCardDeliveryOrderFormNotEarlierThan1DayFromTheCurrentDate() {
        String planningDate = generateDate(1);
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Симферополь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), planningDate);
        $("[data-test-id='name'] input").setValue("Петрова Мария");
        $("[data-test-id='phone'] input").setValue("+79054875965");
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));

    }

    @Test
    void shouldCardDeliveryOrderFormWithKrasnogorskCity() {
        String planningDate = generateDate(3);
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Красногорск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), planningDate);
        $("[data-test-id='name'] input").setValue("Петрова Мария");
        $("[data-test-id='phone'] input").setValue("+79054875965");
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"));

    }

    @Test
    void shouldCardDeliveryOrderFormWithSymbolsInsteadOfName() {
        String planningDate = generateDate(3);
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Симферополь");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), planningDate);
        $("[data-test-id='name'] input").setValue("%$ /=+*");
        $("[data-test-id='phone'] input").setValue("+79054875965");
        $(".checkbox").click();
        $(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));


    }


}
