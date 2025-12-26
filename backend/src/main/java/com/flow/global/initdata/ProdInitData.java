package com.flow.global.initdata;

import com.flow.domain.extensionblocking.entity.FixedExtension;
import com.flow.domain.extensionblocking.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdInitData {

    @Autowired
    @Lazy
    private ProdInitData self;
    private final FixedExtensionRepository fixedExtensionRepository;

    @Bean
    ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            self.createFixedExtensions();
        };
    }

    @Transactional
    public void createFixedExtensions() {
        log.info("===== 고정 확장자 생성 시작 =====");

        if (fixedExtensionRepository.count() == 0) {
            String[] fixedExtensionNames = {"bat", "cmd", "com", "cpl", "exe", "scr", "js"};

            for (String extensionName : fixedExtensionNames) {
                FixedExtension fixedExtension = FixedExtension.builder()
                        .fixExtensionName(extensionName)
                        .isCheck(false) // 기본값 false
                        .build();
                fixedExtensionRepository.save(fixedExtension);
            }

            log.info("고정 확장자 {}개 생성 완료", fixedExtensionNames.length);
        }
    }
}
