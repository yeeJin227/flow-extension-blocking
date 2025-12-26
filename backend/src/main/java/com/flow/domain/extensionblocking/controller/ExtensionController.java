package com.flow.domain.extensionblocking.controller;

import com.flow.domain.extensionblocking.dto.CustomExtensionRequestDto;
import com.flow.domain.extensionblocking.dto.CustomExtensionResponseDto;
import com.flow.domain.extensionblocking.dto.FixedExtensionRequestDto;
import com.flow.domain.extensionblocking.dto.FixedExtensionResponseDto;
import com.flow.domain.extensionblocking.service.CustomExtensionService;
import com.flow.domain.extensionblocking.service.FixedExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extensions")
@RequiredArgsConstructor
public class ExtensionController {

    private final FixedExtensionService fixedExtensionService;
    private final CustomExtensionService customExtensionService;

    // 고정 확장자 목록 조회
    @GetMapping("/fixed")
    public ResponseEntity<List<FixedExtensionResponseDto>> getFixedExtensions() {
        List<FixedExtensionResponseDto> extensions = fixedExtensionService.getAllFixedExtensions();
        return ResponseEntity.ok(extensions);
    }

    // 고정 확장자 체크 상태 업데이트
    @PostMapping("/fixed")
    public ResponseEntity<FixedExtensionResponseDto> updateFixedExtension(
            @RequestBody FixedExtensionRequestDto requestDto) {
        FixedExtensionResponseDto response = fixedExtensionService.updateFixedExtension(requestDto);
        return ResponseEntity.ok(response);
    }

    // 커스텀 확장자 목록 조회
    @GetMapping("/custom")
    public ResponseEntity<List<CustomExtensionResponseDto>> getCustomExtensions() {
        List<CustomExtensionResponseDto> extensions = customExtensionService.getAllCustomExtensions();
        return ResponseEntity.ok(extensions);
    }

    // 커스텀 확장자 추가
    @PostMapping("/custom")
    public ResponseEntity<CustomExtensionResponseDto> addCustomExtension(
            @RequestBody CustomExtensionRequestDto requestDto) {
        try {
            CustomExtensionResponseDto response = customExtensionService.addCustomExtension(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 커스텀 확장자 삭제
    @DeleteMapping("/custom/{customExtensionName}")
    public ResponseEntity<Void> deleteCustomExtension(
            @PathVariable String customExtensionName) {
        try {
            customExtensionService.deleteCustomExtension(customExtensionName);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}