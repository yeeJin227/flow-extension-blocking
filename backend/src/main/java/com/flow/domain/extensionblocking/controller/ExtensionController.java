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
    public ResponseEntity<?> getCustomExtensions() {
        try {
            List<CustomExtensionResponseDto> extensions = customExtensionService.getAllCustomExtensions();
            return ResponseEntity.ok(extensions);
        } catch (Exception e) {
            // 로그 출력 (실제 운영 환경에서는 로깅 프레임워크 사용)
            e.printStackTrace();
            // JSON 형식으로 오류 반환
            java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
            errorResponse.put("error", "커스텀 확장자 조회 중 오류가 발생했습니다");
            errorResponse.put("message", e.getMessage() != null ? e.getMessage() : "알 수 없는 오류");
            errorResponse.put("type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
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