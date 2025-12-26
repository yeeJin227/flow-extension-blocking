package com.flow.domain.extensionblocking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FixedExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fix_extension_name", nullable = false, unique = true, length = 20)
    private String fixExtensionName;

    @Column(name = "is_check", nullable = false)
    private Boolean isCheck;

    public void updateCheckStatus(Boolean isCheck) {
        this.isCheck = isCheck;
    }
}
