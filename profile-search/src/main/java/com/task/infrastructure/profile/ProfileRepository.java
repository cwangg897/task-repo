package com.task.infrastructure.profile;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>, ProfileRepositoryCustom {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from profiles as p where p.id = :id")
    Optional<ProfileEntity> findByIdWithPessimisticLock(Long id);
}
