package itk.academy.orekhov.service;

import itk.academy.orekhov.entity.User;
import itk.academy.orekhov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Аннотация указывает, что класс является сервисом и будет управляться Spring
public class UserService {

    @Autowired
    private UserRepository userRepository; // Инъекция репозитория для работы с данными пользователя

    // Метод для получения всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Возвращает список всех пользователей из базы данных
    }

    // Метод для получения пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id); // Ищет пользователя по ID, возвращает Optional
    }

    // Метод для создания нового пользователя
    public User createUser(User user) {
        return userRepository.save(user); // Сохраняет нового пользователя в базе данных
    }

    // Метод для обновления данных существующего пользователя
    public User updateUser(Long id, User updatedUser) {
        // Проверяем, существует ли пользователь с данным ID
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName()); // Обновляем имя
            user.setEmail(updatedUser.getEmail()); // Обновляем email
            return userRepository.save(user); // Сохраняем обновленные данные
        }).orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден")); // Выбрасываем исключение, если пользователь не найден
    }

    // Метод для удаления пользователя
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id); // Удаляем пользователя по ID
        } else {
            throw new RuntimeException("Пользователь с ID " + id + " не найден"); // Выбрасываем исключение, если пользователь не найден
        }
    }
}
