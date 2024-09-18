package com.example1.demo.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    private final mainController controller = new mainController();

    @Test
    void testCalculateByDays() {
        // Инициализация
        Model model = new BindingAwareModelMap();

        // Ввод данных
        Double salary = 50000.0; // Средняя зарплата
        Integer days = 7;       // Отпускные дни


        String viewName = controller.showCalculatorPage(salary, days, null, null, model);

        // Проверка
        assertEquals("calculator", viewName); // Проверяем, что вернется правильное представление

        // Проверяем, что расчет суммы отпускных выполнен правильно
        assertTrue(model.containsAttribute("sum"));
        assertEquals("15909,09", model.getAttribute("sum"));
    }

    @Test
    void testCalculateByDates() {
        // Инициализация
        Model model = new BindingAwareModelMap();

        // Ввод данных
        Double salary = 66000.0; // Средняя зарплата
        String startDate = "2024-05-01"; // Начало отпуска (1 мая)
        String endDate = "2024-05-10";   // Конец отпуска (10 мая)

        // Вызов метода
        String viewName = controller.showCalculatorPage(salary, null, startDate, endDate, model);

        // Проверка
        assertEquals("calculator", viewName);

        // Проверяем, что расчет суммы отпускных выполнен правильно
        assertTrue(model.containsAttribute("sum"));
        assertEquals("18000,00", model.getAttribute("sum")); // Расчет по рабочим дням без праздников и выходных
        assertTrue(model.containsAttribute("totalDays"));
        assertEquals(6, model.getAttribute("totalDays")); // В диапазоне 1 мая - 10 мая учтено 6 рабочих дней
    }

    @Test
    void testCalculateWithErrorInDates() {
        // Инициализация
        Model model = new BindingAwareModelMap();

        // Ввод некорректных данных
        Double salary = 66000.0;
        String startDate = "invalid-date";
        String endDate = "2024-05-10";

        // Вызов метода
        String viewName = controller.showCalculatorPage(salary, null, startDate, endDate, model);

        // Проверка
        assertEquals("calculator", viewName);

        // Проверка на наличие ошибки
        assertTrue(model.containsAttribute("error"));
        assertEquals("Ошибка в формате дат!", model.getAttribute("error"));
    }
}