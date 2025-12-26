package com.flow.domain.extensionblocking.repository;

import com.flow.domain.extensionblocking.entity.CustomExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {
    Optional<CustomExtension> findByCustomExtensionName(String customExtensionName);
    long count();
}