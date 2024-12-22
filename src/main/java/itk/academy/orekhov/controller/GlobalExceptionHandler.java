package itk.academy.orekhov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice // Аннотация указывает, что этот класс содержит глобальные обработчики исключений
class GlobalExceptionHandler {

    // Обработка исключений ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class) // Указывает, какой тип исключений перехватывается
    @ResponseBody // Указывает, что возвращаемый результат будет телом HTTP-ответа
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Создает HTTP-ответ с причиной исключения и соответствующим статусом
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode()); // Заменено getStatus() на getStatusCode()
    }

    // Обработка исключений валидации (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class) // Перехватывает исключения, возникающие при ошибках валидации
    @ResponseBody
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        // Формирует сообщение об ошибке из первого найденного нарушения валидации
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // Составляет сообщение в формате "поле: ошибка"
                .findFirst() // Берет первое найденное сообщение
                .orElse("Validation error"); // Если ошибок нет, возвращает общее сообщение
        // Возвращает ответ с сообщением об ошибке и статусом BAD_REQUEST (400)
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
