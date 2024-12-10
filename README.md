![image](https://github.com/user-attachments/assets/374699a2-151d-4d52-b889-f8868cfa94f8)
## ✅ 프로젝트 개요

### **프로젝트명**

**Q-Feed**

### **시기**

- 2024.11.15 ~ 2024.12.10

### **요약**

- **취향 기반 데일리 Q&A 커뮤니티 플랫폼**
- 매일 **생성형 AI**를 통해 카테고리별 새로운 질문이 만들어집니다.
- 개인의 취향과 관심사가 점점 세분화되는 시대에 자신의 생각을 나눌 수 진솔하게 나눌 수 있는 소통의 장을 제공 합니다.

## ✅ **기술 스택**

**언어 및 주요 라이브러리:**

- Java 17
- Spring Boot 3.3.5
- Spring AI 1.0.0 M4
- Spring Security 3.3.5
- Spring Data Jpa 3.3.5

**CI/CD:**

- Github Actions
- Docker
- AWS: EC2, ECR, RDS
- Nginx & Certbot

**Database:**

- MySQL
- Redis
- Redis Vector Database & Search

**Collaboration Tools**

- Jira: 일정 관리
- Notion: 문서 작업
- Slack: 연락
- Github: 코드 협업

## ✅ 시스템 아키텍처
![qfeed drawio](https://github.com/user-attachments/assets/a5e6910b-1093-44aa-b393-739a84ba1e7d)

## ✅ ERD
![QFeed (3)](https://github.com/user-attachments/assets/80ac052d-05f1-49c2-bb56-dda355329d1a)

## ✅ DDD 기반 멀티모듈 구조
### 컨텍스트 맵
<img width="461" alt="image" src="https://github.com/user-attachments/assets/77e6a9f9-fef8-4ff4-afc6-b5e28d3ec5e8">

- 코드 가독성 및 유지보수 효율성 대폭 증가
- 이로 인해 비교적 기능 추가가 많은 SNS 서비스 특성상 새로운 기능 추가/변경 시 도메인 로직을 쉽게 파악 가능
<br><br>
### 모듈 구분
<img width="552" alt="image" src="https://github.com/user-attachments/assets/d242c149-a0c8-4128-a5e5-199fd787e476">

- **Domain**: 비즈니스 핵심 로직 (질문 생성/관리, 답변 모델링 등)
- **Application**: 도메인 로직 기반 유즈케이스 구현
- **API**: REST 엔드포인트 제공 (클라이언트와의 통신)
- **Infra**: 기술적 세부사항 처리 (DB, 캐시, 외부 API 연동)
- **Common**: 공통 코드 재사용 (유틸리티, 상수 등)
- **Security**: 인증 및 인가 로직 처리
- **External-API**: (OpenAI API, Kakao) 연동


## ✅ 시퀀스 다이어그램

### 카테고리별 질문 생성
<img width="550" alt="image" src="https://github.com/user-attachments/assets/8fe526a8-6178-400e-a781-6c486863fb7b">

### 질문 상태 업데이트 
<img width="550" alt="image" src="https://github.com/user-attachments/assets/6946ff57-f229-4149-80e5-53bc878ffb57">

### 로그인
<img width="427" alt="image" src="https://github.com/user-attachments/assets/042a769f-10f2-49ab-9481-8f8c435ae7eb">

### OAuth2 카카오 로그인
<img width="556" alt="image" src="https://github.com/user-attachments/assets/53319f5c-266b-47a6-babe-01d99e124b2c">

### 사용자 정보 조회
<img width="533" alt="image" src="https://github.com/user-attachments/assets/2531348c-22c4-4f36-99de-6a3daabc3637">

### 팔로우 추천 기능
<img width="529" alt="image" src="https://github.com/user-attachments/assets/e2b434d9-464a-44d8-bf89-254f9973e25e">

## ✅ 시연 영상
[![시연영상](https://github.com/user-attachments/assets/39793931-2f4f-4b66-8b7c-b23aea6551e9)](https://drive.google.com/file/d/1ZWTOGsL0m2OUD8KcRKBKnarR-YdMWf1f/view?usp=drive_link)
