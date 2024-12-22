package itk.academy.orekhov.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

class OrderTest {

    // Создание фабрики и валидатора для валидации сущности
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    // Тестирование валидации для пустого значения продукта
    @Test
    void testOrderValidation() {
        Order order = new Order();
        order.setProduct("");  // Пустой продукт (должно быть обязательное поле)

        // Выполнение валидации для объекта заказа
        var violations = validator.validate(order);

        // Ожидаем, что будет одно нарушение валидации для поля "product"
        assertEquals(1, violations.size());
        // Проверяем, что ошибка валидации соответствует требованию для поля "product"
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Продукт не может быть пустым")));
    }

    // Тестирование корректного заказа (без нарушений валидации)
    @Test
    void testValidOrder() {
        Order order = new Order();
        order.setProduct("Product A");  // Продукт
        order.setAmount(100.0);  // Сумма заказа
        order.setStatus("Ожидание");  // Статус заказа

        // Выполнение валидации для объекта заказа
        var violations = validator.validate(order);

        // Ожидаем, что не будет нарушений валидации, так как все поля корректны
        assertTrue(violations.isEmpty());
    }

    // Тестирование метода toString() для корректного отображения данных заказа
    @Test
    void testOrderToString() {
        Order order = new Order();
        order.setId(1L);  // Идентификатор заказа
        order.setProduct("Product A");  // Продукт
        order.setAmount(100.0);  // Сумма заказа
        order.setStatus("Ожидание");  // Статус заказа

        // Ожидаемое значение для метода toString()
        String expected = "Order{id=1, product='Product A', amount=100.0, status='Ожидание', user=null}";

        // Сравниваем строковое представление объекта с ожидаемым значением
        assertEquals(expected, order.toString());
    }
}