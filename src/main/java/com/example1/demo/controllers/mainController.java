package com.example1.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;


@Controller
public class mainController {

    // Список праздников
    private Set<LocalDate> getHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(year, Month.JANUARY, 1));  // Новый год
        holidays.add(LocalDate.of(year, Month.JANUARY, 7));  // Рождество
        holidays.add(LocalDate.of(year, Month.MAY, 1));      // Праздник весны и труда
        holidays.add(LocalDate.of(year, Month.MAY, 9));      // День Победы
        holidays.add(LocalDate.of(year, Month.JUNE, 12));    // День России
        holidays.add(LocalDate.of(year, Month.NOVEMBER, 4)); // День народного единства
        return holidays;
    }

    @GetMapping("/calculator")
    public String showCalculatorPage(@RequestParam(value = "salary", required = false) Double salary,
                                     @RequestParam(value = "days", required = false) Integer days,
                                     @RequestParam(value = "startDate", required = false) String startDateStr,
                                     @RequestParam(value = "endDate", required = false) String endDateStr,
                                     Model model) {
        // Сценарий 1: Расчет по количеству дней
        if (salary != null && days != null) {
            double dailyRate = salary / 22; // Пример расчета на 22 рабочих дня
            double vacationPay = dailyRate * days;
            model.addAttribute("sum", String.format("%.2f", vacationPay));
        }

        // Сценарий 2: Расчет по датам
        if (salary != null && startDateStr != null && endDateStr != null) {
            try {
                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = LocalDate.parse(endDateStr);

                int totalVacationDays = calculateVacationDays(startDate, endDate);
                double dailyRate = salary / 22;
                double vacationPay = dailyRate * totalVacationDays;

                model.addAttribute("sum", String.format("%.2f", vacationPay));
                model.addAttribute("totalDays", totalVacationDays);
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка в формате дат!");
            }
        }

        return "calculator";
    }

    // Метод подсчета рабочих дней с учётом праздников и выходных
    private int calculateVacationDays(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidays = getHolidays(startDate.getYear());
        int totalDays = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && !holidays.contains(date)) {
                totalDays++;
            }
        }

        return totalDays;
    }
}
