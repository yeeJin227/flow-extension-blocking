package com.flow.domain.extensionblocking.service;

import com.flow.domain.extensionblocking.dto.FixedExtensionRequestDto;
import com.flow.domain.extensionblocking.dto.FixedExtensionResponseDto;
import com.flow.domain.extensionblocking.entity.FixedExtension;
import com.flow.domain.extensionblocking.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FixedExtensionService {

    private final FixedExtensionRepository fixedExtensionRepository;

    public List<FixedExtensionResponseDto> getAllFixedExtensions() {
        return fixedExtensionRepository.findAll().stream()
                .map(FixedExtensionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public FixedExtensionResponseDto updateFixedExtension(FixedExtensionRequestDto requestDto) {
        FixedExtension fixedExtension = fixedExtensionRepository
                .findByFixExtensionName(requestDto.getFixExtensionName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고정 확장자입니다."));

        fixedExtension.updateCheckStatus(requestDto.getIsCheck());

        return new FixedExtensionResponseDto(fixedExtension);
    }
}
