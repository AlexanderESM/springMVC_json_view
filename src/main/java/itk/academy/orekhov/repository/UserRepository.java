package itk.academy.orekhov.repository;

import itk.academy.orekhov.entity.User; // Импортируйте правильный класс сущности User
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {} // Используйте правильную сущность
