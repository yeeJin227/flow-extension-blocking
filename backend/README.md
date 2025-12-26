# 확장자 차단 관리 과제 - 백엔드 개발 정리 

<br> 
  
## 📋 프로젝트 개요

파일 업로드 시 차단할 확장자를 관리하는 REST API 서버입니다.  <br> 
고정 확장자와 커스텀 확장자를 데이터베이스(Supabase PostgreSQL)에 저장하고 관리하며, Vercel에서 호스팅된 프론트와 연동됩니다.

<br> 

## 🛠 기술 스택

- **Framework**: Spring Boot
- **Language**: Java
- **ORM**: JPA / Hibernate
- **Database**: PostgreSQL (Supabase)
- **Build Tool**: Gradle
- **Lombok**: 코드 간소화

<br> 

## 📁 프로젝트 구조

```
backend/
├── src/main/java/com/flow/
│   ├── domain/
│   │   └── extensionblocking/
│   │       ├── entity/
│   │       │   ├── FixedExtension.java
│   │       │   └── CustomExtension.java
│   │       ├── repository/
│   │       │   ├── FixedExtensionRepository.java
│   │       │   └── CustomExtensionRepository.java
│   │       ├── service/
│   │       │   ├── FixedExtensionService.java
│   │       │   └── CustomExtensionService.java
│   │       ├── dto/
│   │       │   ├── FixedExtensionRequestDto.java
│   │       │   ├── FixedExtensionResponseDto.java
│   │       │   ├── CustomExtensionRequestDto.java
│   │       │   └── CustomExtensionResponseDto.java
│   │       └── controller/
│   │           └── ExtensionController.java
│   ├── global/
│   │   ├── config/
│   │   │   └── CorsConfig.java
│   │   └── initData/
│   │       └── ProdInitData.java
│   └── FlowApplication.java
└── src/main/resources/
    ├── application.yml
    └── application-dev.yml
    └── application-prod.yml
```

<br> 

##  구현된 기능

### 1️⃣ 고정 확장자 초기 데이터(initdata) 생성

<br> 

**요구사항**: 
- 고정 확장자는 차단을 자주하는 확장자들 리스트이며, default는 unCheck 되어져 있습니다.

**구현 내용**:
- 애플리케이션 시작 시 `ProdInitData`에서 자동으로 고정 확장자 7개 생성
- 확장자 목록: `bat`, `cmd`, `com`, `cpl`, `exe`, `scr`, `js`
- 모든 확장자의 초기 `isCheck` 값은 `false`로 설정

**코드 위치**: `com.flow.global.initData.ProdInitData`

```java
@Transactional
public void createFixedExtensions() {
    if (fixedExtensionRepository.count() == 0) {
        String[] fixedExtensionNames = {"bat", "cmd", "com", "cpl", "exe", "scr", "js"};
        
        for (String extensionName : fixedExtensionNames) {
            FixedExtension fixedExtension = FixedExtension.builder()
                    .fixExtensionName(extensionName)
                    .isCheck(false) // 기본값 false
                    .build();
            fixedExtensionRepository.save(fixedExtension);
        }
    }
}
```

-----


### 2️⃣ 고정 확장자 체크 상태 DB에 저장 및 유지
<br> 

**요구사항**: 
- 고정 확장자를 check or uncheck를 할 경우 db에 저장됩니다. 이는 새로고침 시 유지되어야 합니다.

**구현 내용**:
- `POST /api/extensions/fixed` 엔드포인트로 체크 상태 업데이트
- `FixedExtension` 엔티티의 `isCheck` 필드를 `boolean` 타입으로 저장
- 데이터베이스에 영구 저장되어 새로고침 후에도 상태가 유지됨

**Entity 설계**:
```java
@Column(name = "is_check", nullable = false)
private Boolean isCheck;
    
public void updateCheckStatus(Boolean isCheck) {
    this.isCheck = isCheck;
}
```

-----

### 3️⃣ 커스텀 확장자 최대 입력 길이 20자로 제한
<br> 

**요구사항**: 
- 확장자 최대 입력 길이는 20자리

**구현 내용**:
- `CustomExtension` 엔티티의 `customExtensionName` 필드에 `length = 20` 제약 조건 설정
- Service 레이어에서 추가 검증 후, 20자 초과 시 `IllegalArgumentException` 발생

**Entity 설계**:
```java
@Column(name = "custom_extension_name", nullable = false, unique = true, length = 20)
private String customExtensionName;
```

**Service 검증**:
```java
if (customExtensionName.length() > 20) {
    throw new IllegalArgumentException("확장자 이름은 최대 20자까지 입력 가능합니다.");
}
```

-----

### 4️⃣ 커스텀 확장자 DB에 추가 및 저장
<br> 

**요구사항**: 
- 추가 버튼 클릭 시, db에 저장되며 아래쪽 영역에 표현됩니다.

**구현 내용**:
- `POST /api/extensions/custom` 엔드포인트로 커스텀 확장자 추가
- 입력값을 소문자로 변환하여 저장 (`toLowerCase().trim()`) -> 중복 체크와 DB에 일관된 형식으로 저장하기 위해서 적용함
- 데이터베이스에 저장 후 응답으로 반환

**Service 로직**:
```java
@Transactional
public CustomExtensionResponseDto addCustomExtension(CustomExtensionRequestDto requestDto) {
    String customExtensionName = requestDto.getCustomExtensionName().toLowerCase().trim();
    
    // 검증 로직...
    
    CustomExtension customExtension = CustomExtension.builder()
            .customExtensionName(customExtensionName)
            .build();
    
    CustomExtension saved = customExtensionRepository.save(customExtension);
    return new CustomExtensionResponseDto(saved);
}
```

-----

### 5️⃣ 커스텀 확장자 최대 개수 200개로 제한
<br> 

**요구사항**: 
- 커스텀 확장자는 최대 200개까지 추가가 가능

**구현 내용**:
- Service 레이어에서 `customExtensionRepository.count()`로 현재 개수 확인
- 200개 초과 시 `IllegalStateException` 발생
- 프론트엔드에서도 사전 검증하고, 백엔드에서도 이중 방어

**Service 검증**:
```java
private static final int MAX_CUSTOM_EXTENSION_COUNT = 200;

if (customExtensionRepository.count() >= MAX_CUSTOM_EXTENSION_COUNT) {
    throw new IllegalStateException("커스텀 확장자는 최대 200개까지 추가할 수 있습니다.");
}
```

-----

### 6️⃣ X 클릭 시, 커스텀 확장자 DB에서 삭제
<br> 

**요구사항**: 
- 확장자 옆 x를 클릭 시 db에서 삭제됨

**구현 내용**:
- `DELETE /api/extensions/custom/{customExtensionName}` 엔드포인트로 삭제
- URL 파라미터로 확장자 이름을 받아서 삭제
- 존재하지 않는 확장자 삭제 시도 시 `IllegalArgumentException` 발생

**Service 로직**:
```java
@Transactional
public void deleteCustomExtension(String customExtensionName) {
    CustomExtension customExtension = customExtensionRepository
            .findByCustomExtensionName(customExtensionName)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 커스텀 확장자입니다."));
    
    customExtensionRepository.delete(customExtension);
}
```
-----

### 7️⃣ 배포, 개발용 CORS 설정
<br> 

**구현 내용**:
- Vercel, localhost:3000 프론트 허용

**CorsConfig 로직**:
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "https://flow-extension-blocking.vercel.app"
                )
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```
----

<br>

## 🎯 요건 이외에 추가로 고려한 사항

<br> 

### 1. 입력값 정규화하여 중복 체크
<br> 

**구현 이유**: 
- 웹 애플리케이션 관점에서는 'EXE'나 'exe'나 모두 차단 대상이므로 입력받은 확장자를 소문자로 통일하여 중복 체크를 편리하게 하고, DB에 모든 확장자를 일관된 형식으로 저장시키기 위해서 적용

**구현 내용**:
- 입력값을 `toLowerCase()`로 소문자 변환, `trim()`으로 앞뒤 공백 제거하여 DB에 저장
- Repository에서 `findByCustomExtensionName()`으로 중복 검사
- 중복 시 `IllegalArgumentException` 발생

**코드 위치**: `CustomExtensionService.addCustomExtension()`

```java
String customExtensionName = requestDto.getCustomExtensionName().toLowerCase().trim();

// 중복 체크
if (customExtensionRepository.findByCustomExtensionName(customExtensionName).isPresent()) {
    throw new IllegalArgumentException("이미 존재하는 커스텀 확장자입니다.");
}
```

-----


### 2. 예외 처리 및 에러 메시지
<br> 

**구현 내용**:
- `IllegalArgumentException`: 잘못된 입력값 (중복, 길이 초과 등)
- `IllegalStateException`: 비즈니스 로직 위반 (최대 개수 초과 등)

**Controller 예외 처리**:
```java
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
```

----


### 3. CORS 설정
<br> 

**구현 내용**:
- `CorsConfig` 클래스에서 프론트엔드(`http://localhost:3000`, 'https://flow-extension-blocking.vercel.app') 허용

**코드 위치**: `com.flow.global.config.CorsConfig`

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",
                    "https://flow-extension-blocking.vercel.app"
                )
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

----


## 🚀 실행 방법

### 1️⃣ 로컬 개발
- IDE에서 FlowExtensionBlockingApplication 실행
- H2 Console: http://localhost:8080/h2-console

### 2️⃣ 배포 환경
- Render 배포: [https://flow-extension-blocking.onrender.com](https://flow-extension-blocking.onrender.com/api)
- 데이터베이스 Supabase(PostgreSQL): [https://flow-extension-blocking.vercel.app/monitoring.html](https://flow-extension-blocking.vercel.app/test.html)
- Spring Boot: Dockerfile로 빌드 후 자동 실행

---

## 📡 API 엔드포인트

### 고정 확장자
- `GET /api/extensions/fixed` → 목록 조회
- `POST /api/extensions/fixed` → 체크 상태 업데이트

### 커스텀 확장자
- `GET /api/extensions/custom` → 목록 조회
- `POST /api/extensions/custom` → 추가
- `DELETE /api/extensions/custom/{customExtensionName}` → 삭제


----


## 📝 주요 설계 내용

- `계층형 아키텍처`: Controller → Service → Repository → Entity → DTO
- `Spring Data JPA`: CRUD 코드 최소화, 메서드 명으로 쿼리 자동 생성
- `Lombok 사용`: 빌더, Getter/Setter, 생성자 자동 생성
- `초기 데이터 자동 생성`: DevInitData, 개발 환경만 적용
- `RESTful API 설계`: 명확한 URL/HTTP 메서드/상태 코드
- `배포 환경 고려`: PostgreSQL, Render, Vercel, CORS 설정

