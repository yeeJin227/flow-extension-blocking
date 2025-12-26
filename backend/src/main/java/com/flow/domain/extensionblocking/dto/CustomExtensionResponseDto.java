package com.flow.domain.extensionblocking.dto;

import com.flow.domain.extensionblocking.entity.CustomExtension;
import lombok.Getter;

@Getter
public class CustomExtensionResponseDto {
    private Long id;
    private String customExtensionName;

    public CustomExtensionResponseDto(CustomExtension customExtension) {
        this.id = customExtension.getId();
        this.customExtensionName = customExtension.getCustomExtensionName();
    }
}
