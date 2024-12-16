package s21.test.configs;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import s21.test.weekdays.Friday;
import s21.test.weekdays.Monday;
import s21.test.weekdays.Saturday;
import s21.test.weekdays.Sunday;
import s21.test.weekdays.Thursday;
import s21.test.weekdays.Tuesday;
import s21.test.weekdays.Wednesday;
import s21.test.weekdays.WeekDay;

@Configuration
@ComponentScan("s21.test.entitys")
public class MyConfig {
    @Bean
    public WeekDay getDay() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> new Monday();
            case TUESDAY -> new Tuesday();
            case WEDNESDAY -> new Wednesday();
            case THURSDAY -> new Thursday();
            case FRIDAY -> new Friday();
            case SATURDAY -> new Saturday();
            default -> new Sunday();
        };
    }
}
