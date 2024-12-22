package itk.academy.orekhov.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    // Создание фабрики валидатора для проверки аннотаций валидации
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static final Validator validator = factory.getValidator();

    @Test
    void testValidationFailsForInvalidFields() {
        // Тест на проверку некорректных данных
        UserDto userDto = new UserDto();
        userDto.setName(""); // Пустое имя
        userDto.setEmail("invalid-email"); // Некорректный email

        // Выполняем валидацию
        var violations = validator.validate(userDto);

        // Ожидаем 2 нарушения: для поля name и email
        assertEquals(2, violations.size());
        // Проверяем, что ошибка связана с пустым именем
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Имя не может быть пустым")));
        // Проверяем, что ошибка связана с некорректным email
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Некорректный формат email")));
    }

    @Test
    void testValidationPassesForValidFields() {
        // Тест на проверку корректных данных
        UserDto userDto = new UserDto(1L, "John Doe", "john.doe@example.com");

        // Выполняем валидацию
        var violations = validator.validate(userDto);

        // Убеждаемся, что нарушений нет
        assertTrue(violations.isEmpty());
    }

    @Test
    void testToString() {
        // Тест метода toString
        UserDto userDto = new UserDto(1L, "John Doe", "john.doe@example.com");
        String expected = "UserDto{id=1, name='John Doe', email='john.doe@example.com'}";

        // Проверяем, что метод toString возвращает ожидаемую строку
        assertEquals(expected, userDto.toString());
    }
}
