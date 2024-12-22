package itk.academy.orekhov.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import itk.academy.orekhov.view.Views;

@Entity // Аннотация для указания, что данный класс является сущностью JPA (будет отображен в таблице базы данных)
@Table(name = "orders") // Указывает имя таблицы в базе данных для сущности Order
public class Order {

    @Id // Указывает, что это поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения для первичного ключа
    @JsonView(Views.UserDetails.class) // Указывает, что это поле будет сериализовано с использованием представления UserDetails
    private Long id; // Уникальный идентификатор заказа

    @NotBlank(message = "Продукт не может быть пустым") // Проверка, что значение не пустое и не равно null для поля product
    @JsonView(Views.UserDetails.class) // Указывает, что это поле будет сериализовано с использованием представления UserDetails
    private String product; // Название продукта в заказе

    @JsonView(Views.UserDetails.class) // Указывает, что это поле будет сериализовано с использованием представления UserDetails
    private Double amount; // Сумма заказа

    @JsonView(Views.UserDetails.class) // Указывает, что это поле будет сериализовано с использованием представления UserDetails
    private String status; // Статус заказа (например, "в обработке", "отправлено", "доставлено")

    @ManyToOne // Указывает, что каждый заказ связан с одним пользователем
    @JoinColumn(name = "user_id") // Связь через внешний ключ с таблицей пользователей
    @JsonBackReference // Управляет сериализацией двухсторонней связи между заказом и пользователем, предотвращая рекурсию
    private User user; // Пользователь, связанный с данным заказом

    // Геттеры и сеттеры для всех полей
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Переопределение метода toString для удобного отображения данных заказа
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", user=" + (user != null ? user.getId() : "null") + // Добавление id пользователя в строковое представление
                '}'; // Возвращаем строку с информацией о заказе
    }
}
