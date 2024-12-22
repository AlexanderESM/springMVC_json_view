package itk.academy.orekhov.controller;

import com.fasterxml.jackson.annotation.JsonView;
import itk.academy.orekhov.entity.User;
import itk.academy.orekhov.repository.UserRepository;
import itk.academy.orekhov.view.Views;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController //автоматически возвращают данные, а не представления. Объединяет две аннотации: @Controller и @ResponseBody.
@RequestMapping("/api/users") // Основной URL для работы с пользователями
@Validated // Включает валидацию на уровне контроллера
class UserController {

    @Autowired // используется для автоматической инъекции зависимостей в классы
    private UserRepository userRepository; // Инъекция репозитория для работы с данными пользователя

    // Получить всех пользователей (используется представление UserSummary)
    @GetMapping //указывает, что метод будет обрабатывать HTTP GET-запросы
    @JsonView(Views.UserSummary.class) //используется для указания, какое представление (view) будет использовано при сериализации объекта в JSON
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Возвращаем список всех пользователей
    }

    // Получить пользователя по ID (используется представление UserDetails)
    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public User getUserById(@PathVariable Long id) {
        // Ищем пользователя по ID, если не найден, выбрасываем исключение
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // Создать нового пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Устанавливает статус ответа на CREATED (201)
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user); // Сохраняем нового пользователя в базе данных
    }

    // Обновить информацию о пользователе
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        // Ищем пользователя по ID, если не найден, выбрасываем исключение
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName()); // Обновляем имя
            user.setEmail(updatedUser.getEmail()); // Обновляем email
            return userRepository.save(user); // Сохраняем обновленные данные
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // Удалить пользователя по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Статус ответа - NO_CONTENT (204), так как ответ пустой
    public void deleteUser(@PathVariable Long id) {
        // Проверяем, существует ли пользователь с таким ID, и если существует, удаляем его
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); // Удаляем пользователя
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"); // Если не найден, выбрасываем исключение
        }
    }
}
