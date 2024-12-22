package itk.academy.orekhov.repository;

import itk.academy.orekhov.entity.Order;
import itk.academy.orekhov.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5433/ecommerce_db_test",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres",
        "spring.datasource.driver-class-name=org.postgresql.Driver"
})
public class OrderRepositoryTest {

    private static PostgreSQLContainer<?> postgresContainer;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository; // Для создания пользователя, связанного с заказами

    private User user;

    @BeforeAll
    public static void setUpClass() {
        // Запуск контейнера PostgreSQL с TestContainers
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("ecommerce_db_test")
                .withUsername("postgres")
                .withPassword("postgres");
        postgresContainer.start();

        // Устанавливаем URL для подключения к тестовой базе данных PostgreSQL
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @AfterAll
    public static void tearDownClass() {
        // Остановка контейнера после выполнения тестов
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }

    @BeforeEach
    public void setUp() {
        // Создание пользователя для привязки к заказам
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);
    }

    @Test
    public void testSaveOrder() {
        // Создание нового заказа
        Order order = new Order();
        order.setProduct("Laptop");
        order.setAmount(1200.50);
        order.setStatus("Processing");
        order.setUser(user); // Привязка к пользователю

        // Сохранение заказа
        Order savedOrder = orderRepository.save(order);

        // Проверка, что заказ сохранен
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);
        assertThat(savedOrder.getProduct()).isEqualTo("Laptop");
        assertThat(savedOrder.getUser()).isEqualTo(user);
    }

    @Test
    public void testFindById() {
        // Создание и сохранение заказа
        Order order = new Order();
        order.setProduct("Smartphone");
        order.setAmount(800.75);
        order.setStatus("Shipped");
        order.setUser(user);
        orderRepository.save(order);

        // Поиск заказа по ID
        Optional<Order> foundOrder = orderRepository.findById(order.getId());

        // Проверка, что заказ найден
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getProduct()).isEqualTo("Smartphone");
    }

    @Test
    public void testFindAllOrders() {
        // Создание и сохранение двух заказов
        Order order1 = new Order();
        order1.setProduct("Tablet");
        order1.setAmount(400.00);
        order1.setStatus("Delivered");
        order1.setUser(user);

        Order order2 = new Order();
        order2.setProduct("Headphones");
        order2.setAmount(150.00);
        order2.setStatus("Processing");
        order2.setUser(user);

        orderRepository.save(order1);
        orderRepository.save(order2);

        // Поиск всех заказов
        List<Order> orders = orderRepository.findAll();

        // Проверка, что оба заказа сохранены в базе данных
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getProduct).contains("Tablet", "Headphones");
    }

    @Test
    public void testDeleteOrder() {
        // Создание и сохранение заказа
        Order order = new Order();
        order.setProduct("Keyboard");
        order.setAmount(50.00);
        order.setStatus("Processing");
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);

        // Удаление заказа
        orderRepository.deleteById(savedOrder.getId());

        // Проверка, что заказ удален
        Optional<Order> deletedOrder = orderRepository.findById(savedOrder.getId());
        assertThat(deletedOrder).isNotPresent();
    }
}
