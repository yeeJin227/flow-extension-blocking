package com.flow.domain.extensionblocking.service;

import com.flow.domain.extensionblocking.dto.CustomExtensionRequestDto;
import com.flow.domain.extensionblocking.dto.CustomExtensionResponseDto;
import com.flow.domain.extensionblocking.entity.CustomExtension;
import com.flow.domain.extensionblocking.repository.CustomExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomExtensionService {

    private final CustomExtensionRepository customExtensionRepository;
    private static final int MAX_CUSTOM_EXTENSION_COUNT = 200;

    public List<CustomExtensionResponseDto> getAllCustomExtensions() {
        return customExtensionRepository.findAll().stream()
                .map(CustomExtensionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomExtensionResponseDto addCustomExtension(CustomExtensionRequestDto requestDto) {
        String customExtensionName = requestDto.getCustomExtensionName().toLowerCase().trim();

        // 최대 개수 체크
        if (customExtensionRepository.count() >= MAX_CUSTOM_EXTENSION_COUNT) {
            throw new IllegalStateException("커스텀 확장자는 최대 200개까지 추가할 수 있습니다.");
        }

        // 중복 체크
        if (customExtensionRepository.findByCustomExtensionName(customExtensionName).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 커스텀 확장자입니다.");
        }

        // 길이 체크
        if (customExtensionName.length() > 20) {
            throw new IllegalArgumentException("확장자 이름은 최대 20자까지 입력 가능합니다.");
        }

        CustomExtension customExtension = CustomExtension.builder()
                .customExtensionName(customExtensionName)
                .build();

        CustomExtension saved = customExtensionRepository.save(customExtension);
        return new CustomExtensionResponseDto(saved);
    }

    @Transactional
    public void deleteCustomExtension(String customExtensionName) {
        CustomExtension customExtension = customExtensionRepository
                .findByCustomExtensionName(customExtensionName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 커스텀 확장자입니다."));

        customExtensionRepository.delete(customExtension);
    }
}
