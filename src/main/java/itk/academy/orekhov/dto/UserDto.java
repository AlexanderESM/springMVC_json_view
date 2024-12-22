package itk.academy.orekhov.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    private Long id; // Уникальный идентификатор пользователя

    @NotBlank(message = "Имя не может быть пустым") // Валидация: обязательное поле
    private String name; // Имя пользователя

    @Email(message = "Некорректный формат email") // Валидация: проверка формата email
    @NotBlank(message = "Email не может быть пустым") // Валидация: обязательное поле
    private String email; // Электронная почта пользователя

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Конструктор по умолчанию
    public UserDto() {
    }

    // Конструктор с параметрами
    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Метод для удобного отображения информации о пользователе
    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
