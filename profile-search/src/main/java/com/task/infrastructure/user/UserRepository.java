package com.task.infrastructure.user;

import com.task.infrastructure.profile.ProfileEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select u from users as u where u.id = :id")
    Optional<UserEntity> findByIdWithPessimisticLock(Long id);
}
