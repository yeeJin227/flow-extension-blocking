package com.flow.domain.extensionblocking.dto;

import com.flow.domain.extensionblocking.entity.FixedExtension;
import lombok.Getter;

@Getter
public class FixedExtensionResponseDto {
    private Long id;
    private String fixExtensionName;
    private Boolean isCheck;

    public FixedExtensionResponseDto(FixedExtension fixedExtension) {
        this.id = fixedExtension.getId();
        this.fixExtensionName = fixedExtension.getFixExtensionName();
        this.isCheck = fixedExtension.getIsCheck();
    }
}