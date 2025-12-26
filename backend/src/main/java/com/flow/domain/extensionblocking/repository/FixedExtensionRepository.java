package com.flow.domain.extensionblocking.repository;

import com.flow.domain.extensionblocking.entity.FixedExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long> {
    Optional<FixedExtension> findByFixExtensionName(String fixExtensionName);
}