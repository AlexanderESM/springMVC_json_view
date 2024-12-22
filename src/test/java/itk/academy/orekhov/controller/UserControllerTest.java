package itk.academy.orekhov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Аннотация для запуска Spring Boot тестов
@AutoConfigureMockMvc // Конфигурирует MockMvc для тестирования REST API
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Внедрение MockMvc для выполнения HTTP-запросов

    @Autowired
    private ObjectMapper objectMapper; // Внедрение ObjectMapper для преобразования объектов в JSON

    // Тест для получения всех пользователей
    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")) // Выполнение GET запроса
                .andExpect(status().isOk()) // Ожидаем успешный ответ (HTTP 200)
                .andExpect(jsonPath("$").isEmpty()); // Проверяем, что ответ пуст, если в базе нет пользователей
    }

    // Тест для получения пользователя по ID
    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 1)) // Выполнение GET запроса с параметром ID
                .andExpect(status().isNotFound()); // Ожидаем статус 404, если пользователь не найден
    }

    // Тест для создания нового пользователя
    @Test
    void testCreateUser() throws Exception {
        // Создаем данные для нового пользователя
        Map<String, String> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");

        mockMvc.perform(post("/api/users") // Выполнение POST запроса для создания пользователя
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого как JSON
                        .content(objectMapper.writeValueAsString(user))) // Преобразуем данные пользователя в JSON
                .andExpect(status().isCreated()) // Ожидаем, что пользователь будет создан (HTTP 201)
                .andExpect(jsonPath("$.name").value("John Doe")) // Проверяем, что имя соответствует
                .andExpect(jsonPath("$.email").value("john.doe@example.com")); // Проверяем, что email соответствует
    }

    // Тест для обновления данных пользователя
    @Test
    void testUpdateUser() throws Exception {
        // Создаем новые данные для пользователя
        Map<String, String> user = new HashMap<>();
        user.put("name", "Jane Doe");
        user.put("email", "jane.doe@example.com");

        mockMvc.perform(put("/api/users/{id}", 1) // Выполнение PUT запроса для обновления пользователя с ID 1
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого как JSON
                        .content(objectMapper.writeValueAsString(user))) // Преобразуем данные пользователя в JSON
                .andExpect(status().isNotFound()); // Ожидаем статус 404, если пользователь не найден
    }

    // Тест для удаления пользователя
    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1)) // Выполнение DELETE запроса для удаления пользователя с ID 1
                .andExpect(status().isNotFound()); // Ожидаем статус 404, если пользователь не найден
    }

    // Тест для проверки ошибок валидации
    @Test
    void testValidationError() throws Exception {
        // Создаем данные с ошибками валидации
        Map<String, String> user = new HashMap<>();
        user.put("name", ""); // Невалидное имя (пустое)
        user.put("email", "invalid-email"); // Невалидный email

        mockMvc.perform(post("/api/users") // Выполнение POST запроса с некорректными данными
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого как JSON
                        .content(objectMapper.writeValueAsString(user))) // Преобразуем данные в JSON
                .andExpect(status().isBadRequest()) // Ожидаем ошибку валидации (HTTP 400)
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation error"))); // Проверяем, что ошибка валидации присутствует в ответе
    }
}
