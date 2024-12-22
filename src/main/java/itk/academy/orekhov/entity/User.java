package itk.academy.orekhov.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import itk.academy.orekhov.view.Views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity // Аннотация для указания, что данный класс является сущностью JPA (будет отображен в таблице базы данных)
@Table(name = "users") // Указывает имя таблицы, в PostgreSQL "user" является зарезервированным словом, поэтому используется другое имя
public class User {

    @Id // Указывает, что данное поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическое генерирование значения для первичного ключа
    @JsonView(Views.UserSummary.class) // Указывает, что это поле будет сериализовано с использованием представления UserSummary
    private Long id; // Уникальный идентификатор пользователя

    @NotBlank // Проверка, что значение не пустое и не равно null
    @JsonView(Views.UserSummary.class) // Указывает, что это поле будет сериализовано с использованием представления UserSummary
    private String name; // Имя пользователя

    @Email // Проверка, что значение соответствует формату email
    @NotBlank // Проверка, что значение не пустое и не равно null
    @JsonView(Views.UserSummary.class) // Указывает, что это поле будет сериализовано с использованием представления UserSummary
    private String email; // Электронная почта пользователя

    @JsonView(Views.UserDetails.class) // Указывает, что это поле будет сериализовано с использованием представления UserDetails
    @JsonManagedReference // Управляет сериализацией двухсторонней связи между User и Order, предотвращая рекурсию
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Связь один ко многим с сущностью Order
    private List<Order> orders = new ArrayList<>(); // Список заказов, связанных с пользователем

    // Геттеры и сеттеры для всех полей
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // Переопределение метода equals для корректного сравнения объектов User
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Если ссылки одинаковые, то объекты равны
        if (o == null || getClass() != o.getClass()) return false; // Если объект другой классов, то они не равны
        User user = (User) o;
        return Objects.equals(id, user.id) && // Сравниваем id пользователей
                Objects.equals(email, user.email); // Сравниваем email пользователей
    }

    // Переопределение метода hashCode для корректной работы в коллекциях
    @Override
    public int hashCode() {
        return Objects.hash(id, email); // Генерация хэш-кода на основе id и email
    }

    // Переопределение метода toString для получения строкового представления объекта
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", orders=" + orders +
                '}'; // Возвращаем строку с информацией о пользователе
    }
}
