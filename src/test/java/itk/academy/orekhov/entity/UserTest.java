package itk.academy.orekhov.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import itk.academy.orekhov.view.Views;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    private User user;

    @BeforeEach
    void setUp() {
        // Настройка начального состояния объекта перед каждым тестом
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    // Тестирование валидации
    @Test
    void testValidUser() {
        var violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User should be valid");
    }

    @Test
    void testInvalidEmail() {
        user.setEmail("invalid-email");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User should be invalid due to incorrect email format");
    }

    @Test
    void testEmptyName() {
        user.setName("");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User should be invalid due to empty name");
    }

    // Тестирование методов equals и hashCode
    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("john.doe@example.com");

        assertEquals(user1, user2, "Users should be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal");

        user2.setId(2L);
        assertNotEquals(user1, user2, "Users with different ids should not be equal");
    }

    // Тестирование сериализации с использованием представлений
    @Test
    void testSerializationWithUserSummaryView() throws Exception {
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithView(Views.UserSummary.class).writeValueAsString(user);

        assertTrue(json.contains("name"));
        assertTrue(json.contains("email"));
        assertFalse(json.contains("orders")); // Заказы не должны присутствовать в UserSummary
    }

    @Test
    void testSerializationWithUserDetailsView() throws Exception {
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        // Добавляем заказ
        Order order = new Order();
        user.getOrders().add(order);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithView(Views.UserDetails.class).writeValueAsString(user);

        assertTrue(json.contains("orders"));
    }

    // Тестирование методов для работы с заказами
    @Test
    void testAddOrder() {
        Order order = new Order();
        user.getOrders().add(order);
        assertEquals(1, user.getOrders().size(), "User should have 1 order");
    }

    @Test
    void testRemoveOrder() {
        Order order1 = new Order();
        Order order2 = new Order();
        user.getOrders().add(order1);
        user.getOrders().add(order2);

        assertEquals(2, user.getOrders().size(), "User should have 2 orders");

        user.getOrders().remove(order1);
        assertEquals(1, user.getOrders().size(), "User should have 1 order after removal");
    }

    // Тестирование пустого списка заказов
    @Test
    void testEmptyOrdersList() {
        assertTrue(user.getOrders().isEmpty(), "User should have an empty orders list initially");
    }

    // Тестирование метода toString
    @Test
    void testToString() {
        String expected = "User{id=null, name='John Doe', email='john.doe@example.com', orders=[]}";
        assertEquals(expected, user.toString(), "toString method should return the correct string representation");
    }
}
