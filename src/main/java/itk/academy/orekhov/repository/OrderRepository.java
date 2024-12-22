package itk.academy.orekhov.repository;

import itk.academy.orekhov.entity.Order; // Импортируйте правильный класс сущности Order
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {} // Используйте правильную сущность
