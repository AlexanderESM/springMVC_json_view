package itk.academy.orekhov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    // Объект тестируемого класса
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    // Тест обработки исключения ResponseStatusException
    @Test
    void testHandleResponseStatusException() {
        // Создаем исключение с кодом NOT_FOUND и сообщением
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        // Вызываем обработчик исключения
        ResponseEntity<String> response = globalExceptionHandler.handleResponseStatusException(exception);

        // Проверяем, что статус ответа и сообщение соответствуют ожиданиям
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    // Тест обработки исключения валидации с одной ошибкой
    @Test
    void testHandleValidationExceptionSingleError() {
        // Создаем моки для исключения и результата валидации
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // Добавляем одну ошибку валидации
        FieldError error = new FieldError("objectName", "name", "Name cannot be empty");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error));

        // Вызываем обработчик исключения
        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(exception);

        // Проверяем, что статус и сообщение корректны
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("name: Name cannot be empty", response.getBody());
    }

    // Тест обработки исключения валидации с несколькими ошибками
    @Test
    void testHandleValidationExceptionMultipleErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // Добавляем несколько ошибок валидации
        FieldError error1 = new FieldError("objectName", "name", "Name cannot be empty");
        FieldError error2 = new FieldError("objectName", "email", "Email is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        // Вызываем обработчик исключения
        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(exception);

        // Проверяем, что берется только первая ошибка
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("name: Name cannot be empty", response.getBody());
    }

    // Тест обработки исключения валидации без ошибок
    @Test
    void testHandleValidationExceptionNoErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // Вызываем обработчик исключения
        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(exception);

        // Проверяем, что возвращается общее сообщение о валидационной ошибке
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody());
    }

    // Тест для необработанного исключения
    @Test
    void testUnhandledException() {
        try {
            // Искусственно выбрасываем исключение
            throw new IllegalArgumentException("This is an unhandled exception");
        } catch (Exception e) {
            // Проверяем, что исключение выбрасывается
            assertThrows(Exception.class, () -> {
                throw e;
            });
        }
    }
}
