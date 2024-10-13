package com.task.infrastructure.profile;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<ProfileEntity, Long>, ProfileRepositoryCustom {
}
