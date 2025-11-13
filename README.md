# 🚚 프로젝트명

> MSA 기반 물류 관리 시스템

**개발 기간:** 2025.10.31 ~ 2025.11.13

---

## 👥 팀 구성

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Doritosch">
        <img src="https://github.com/Doritosch.png" width="90px" /><br/>
        <b>김민수</b><br/>
        <sub>팀장 · Gateway / Config / User / 공통 모듈</sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hinoyat">
        <img src="https://github.com/hinoyat.png" width="90px" /><br/>
        <b>강현호</b><br/>
        <sub>허브 · 허브 간 경로</sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hellonaeunkim">
        <img src="https://github.com/hellonaeunkim.png" width="90px" /><br/>
        <b>김나은</b><br/>
        <sub>CI · 상품 · 재고 · 주문</sub>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/eddie412">
        <img src="https://github.com/eddie412.png" width="90px" /><br/>
        <b>김주영</b><br/>
        <sub>배송 · Spring Security</sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/S2hyeyunS2">
        <img src="https://github.com/S2hyeyunS2.png" width="90px" /><br/>
        <b>김혜윤</b><br/>
        <sub>업체 · 슬랙 · AI</sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/6uiwj">
        <img src="https://github.com/6uiwj.png" width="90px" /><br/>
        <b>정민지</b><br/>
        <sub>배송 담당자 · 배송 MSA 구축</sub>
      </a>
    </td>
  </tr>
</table>

---

## 📋 프로젝트 소개

이 프로젝트는 물류 관리·배송 시스템을 MSA 기반으로 구성한 프로젝트입니다.  
허브, 경로, 주문, 배송 등 기능을 도메인 단위로 나누고, 각 서비스가 데이터를 주고받으며 동작하는 구조를 설계했습니다.  
전체적으로 MSA 구조를 직접 구성해 보고, 서비스 간 연동과 도메인 설계를 경험하는 데 집중한 프로젝트입니다.

---

## ✨ 주요 기능


<details>
<summary><strong>1. 인증·사용자 관리</strong></summary>

- 회원가입 / 로그인 / 로그아웃 / 토큰 재발급
- 사용자 단건 조회 / 목록 조회·검색
- 사용자 역할 변경 (MASTER 권한)
- 사용자 정보 수정 & 자기 계정 삭제
- 가입 승인/거절(Approval) 프로세스
- 허브 관리자·마스터 권한에 따른 접근 제어

</details>

<details>
<summary><strong>2. 허브 관리</strong></summary>

- 허브 생성 / 수정 / 삭제
- 허브 단건 조회 / 목록 조회 / 키워드 검색
- 허브 위치 변경에 따른 경로 재계산 트리거 (도메인 이벤트)

</details>

<details>
<summary><strong>3. 허브 경로(Hub Route) 관리</strong></summary>

- 경로 생성 / 수정 / 삭제
- 출발·도착 허브 기준 검색
- 경로 상세 조회
- 허브 변경에 따른 전체 경로 재생성 로직 처리

</details>

<details>
<summary><strong>4. 주문(Order) 관리</strong></summary>

- 주문 생성 / 조회 / 단건 상세 조회
- 주문 수정(수량·요청사항)
- 주문 취소 / 삭제
- 요청사·수령사·상품·상태 기반 필터 검색
- 담당 허브/배송 담당자/회사 관리자 등 권한에 따른 조회 범위 제어

</details>

<details>
<summary><strong>5. 배송(Delivery) 관리</strong></summary>

- 배송 생성(내부 자동 생성 포함)
- 배송 전체 조회 / 상세 조회
- 배송 상태 업데이트
- 배송 삭제
- 배송 경로(legs) 조회 및 상태 변경
- 특정 담당자에게 자동 배정 기능

</details>

<details>
<summary><strong>6. 배송 담당자(Delivery Manager) 관리</strong></summary>

- 담당자 등록 / 수정 / 삭제
- 본인 정보 조회
- 담당자 전체 조회 / 상세 조회
- 담당자 순번 조회(로스터 기능)
- 자동 배정 API 지원

</details>

<details>
<summary><strong>7. 업체(Company) 관리</strong></summary>

- 업체 등록 / 수정 / 삭제
- 업체 전체 조회 / 단건 조회
- 업체 검색 (키워드 기반)
- 생산업체 / 수령업체 구분 및 허브 기반 관리

</details>

<details>
<summary><strong>8. 상품(Product) 관리</strong></summary>

- 상품 생성 / 수정 / 삭제
- 상품 전체 조회 / 단건 조회
- 상품 검색
- 상품 재고 조회

</details>

<details>
<summary><strong>9. Slack 메시지 기능</strong></summary>

- Slack 메시지 생성 / 수정 / 삭제
- 메시지 상세 조회 / 목록 조회
- 메시지 상태(PENDING/SENT/FAILED) 관리

</details>

<details>
<summary><strong>10. AI 기능 (Gemini API 활용)</strong></summary>

- 배송 ETA 기반 계획 생성 API
- Slack 전송용 AI 메시지 자동 생성 기능
- AI 기반 물류 프로세스 실험 기능

</details>

---

## 🛠 기술 스택

**Backend**
<p>
<img src="https://img.shields.io/badge/Java%2017-007396?logo=openjdk&logoColor=white" /> 
<img src="https://img.shields.io/badge/Spring%20Boot%203.5.7-6DB33F?logo=springboot&logoColor=white" /> 
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white" />
<img src="https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Cloud%20Gateway-6DB33F?logo=spring&logoColor=white" />
<img src="https://img.shields.io/badge/Eureka-6DB33F?logo=spring&logoColor=white" />
<img src="https://img.shields.io/badge/OpenFeign-000000?logo=openapiinitiative&logoColor=white" />
<img src="https://img.shields.io/badge/Resilience4j-4E8C3B?logo=spring&logoColor=white" />
<img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black" />
<img src="https://img.shields.io/badge/Zipkin-000000?logo=apache&logoColor=white" />
</p>

**Database**
<p> 
<img src="https://img.shields.io/badge/MySQL%208.0.44-4479A1?logo=mysql&logoColor=white" />
<img src="https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white" />
</p>

**External API**
<p>
<img src="https://img.shields.io/badge/Kakao%20Local%20API-FFCD00?logo=kakao&logoColor=000000" /> 
<img src="https://img.shields.io/badge/Google%20Gemini%20API-4285F4?logo=google&logoColor=white" />
</p>

---

## 🏗 아키텍처

### 프로젝트 구조

<details>
<summary><strong>전체 구조 보기</strong></summary>

```text
HubEleven
┣ common
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/common
┃     ┣ annotation
┃     ┣ code
┃     ┣ exception
┃     ┣ model
┃     ┣ request
┃     ┗ response
┣ config
┃ ┣ build.gradle
┃ ┗ src
┃     ┣ main/java/com/hubEleven/config
┃     ┗ main/resources/config-repo
┣ eurekaServer
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/eurekaServer
┣ gateWay
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/gateWay
┃     ┣ config
┃     ┗ security
┣ hub
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/hub
┃     ┣ application
┃     ┣ common
┃     ┣ domain
┃     ┣ infrastructure
┃     ┗ presentation
┣ company
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/company
┃     ┣ application
┃     ┣ domain
┃     ┣ infrastructure
┃     ┗ presentation
┣ delivery
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/delivery
┃     ┣ config
┃     ┣ delivery
┃     ┃   ┣ application
┃     ┃   ┣ domain
┃     ┃   ┣ infrastructure
┃     ┃   ┗ presentation
┃     ┣ deliveryManager
┃     ┃   ┣ application
┃     ┃   ┣ domain
┃     ┃   ┣ infrastructure
┃     ┃   ┗ presentation
┃     ┗ deliveryRoute
┃         ┣ application
┃         ┗ domain
┣ notification
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/notification
┃     ┣ ai
┃     ┃   ┣ application
┃     ┃   ┣ domain
┃     ┃   ┣ infrastructure
┃     ┃   ┗ presentation
┃     ┗ slack
┃         ┣ application
┃         ┣ domain
┃         ┣ infrastructure
┃         ┗ presentation
┣ order
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/order
┃     ┣ application
┃     ┣ domain
┃     ┣ infrastructure
┃     ┗ presentation
┣ product
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/product
┃     ┣ product
┃     ┃   ┣ application
┃     ┃   ┣ domain
┃     ┃   ┣ infrastructure
┃     ┃   ┗ presentation
┃     ┗ stock
┃         ┣ application
┃         ┣ domain
┃         ┣ infrastructure
┃         ┗ presentation
┣ user
┃ ┣ build.gradle
┃ ┗ src/main/java/com/hubEleven/user
┃     ┣ application
┃     ┣ domain
┃     ┣ infrastructure
┃     ┗ presentation
┣ build.gradle
┣ settings.gradle
└ README.md
```
</details>

### Architecture
![img_1.png](img_1.png)
---

## 📡 API 명세

[프로젝트 문서 (Notion)](https://www.notion.so/teamsparta/11-29d2dc3ef514800bba5afaa01dbcc458)

---

## 🗄 ERD
![img.png](images/ERD.png)

---

## 💭 회고

### 김민수
이번 프로젝트를 통해 MSA에서 고려해야 할 요소 및 소통의 중요성을 느낄 수 있었습니다.  
MSA 구조를 적용하면서, 각 서비스가 독립적으로 동작하면서 서로 협력해야 한다는 점에서 서비스 간 통신, 데이터 일관성, 장애 전파 등 고려해야 할 요소가 많다는 것을 체감했습니다.

협업 과정에서는 소통이 원활하지 않았던 부분도 있었습니다.  
MSA 특성상 각자 맡은 서비스의 책임이 분리되어 있어 작은 변경 사항 하나가 다른 서비스에 영향을 미칠 수 있었고, 충분한 논의가 이루어지지 않으면 개발 효율뿐만 아니라 시스템 안정성까지 흔들릴 수 있다는 것을 경험했습니다.

이번 프로젝트에서는 이러한 소통과 조율의 아쉬움이 남았지만, 그만큼 앞으로 팀 프로젝트에서 어떻게 소통 방식을 개선할지 고민하는 계기가 된 것 같습니다.

---

### 강현호
이번 프로젝트는 DDD 구조로 개발하며 도메인 중심으로 기능을 나누고, 서비스의 책임을 명확하게 분리하는 방향을 경험했습니다.  
허브 생성·위치 변경 시 발생하는 부가 작업은 도메메인 이벤트로 처리해 핵심 로직과 부수 로직을 자연스럽게 분리할 수 있었고, 구조적인 확장성도 함께 얻을 수 있었습니다.

또한 조회가 자주 발생하는 API에는 캐싱을 적용해 성능을 개선했고, Spring Cache와 Redis 캐시를 함께 사용해 보면서 단일 인스턴스 환경에서는 Spring Cache가, 여러 인스턴스로 확장되는 MSA 환경에서는 Redis가 더 적합하다는 점을 직접 체감할 수 있었습니다.  
캐시 적용 전후를 측정해 보며 구조적 선택이 응답 속도와 처리량에 어떤 영향을 주는지도 확인할 수 있었습니다.

무엇보다 팀원들과 기술적 선택에 대해 지속적으로 의견을 나누며 더 나은 방향을 찾는 과정이 큰 도움이 되었고, 이런 논의가 서비스 품질 향상으로 이어지는 긍정적인 경험을 할 수 있었습니다.

---

### 김나은
이번 프로젝트는 MSA와 DDD를 처음 적용하면서 도메인 분리와 서비스 경계 설정이 전체 시스템의 품질을 결정짓는다는 것을 깨달았고, 초기 설계 단계의 중요성을 체감했습니다.  
FeignClient를 통한 동기 방식의 서비스 통신을 구현하면서 네트워크 장애 시 재시도 로직의 필요성을 경험했고, 향후 RabbitMQ 같은 비동기 방식도 상황에 따라 필요하다는 것을 알게 되었습니다.

아쉬웠던 점은 단위 테스트와 통합 테스트를 충분히 작성하지 못해 런타임에 예상치 못한 버그를 발견하는 경우가 많았다는 것입니다.  
Zipkin을 통한 분산 추적 시스템을 도입하여 서비스 간 요청 흐름을 시각화하는 기반을 마련했고, 향후 각 서비스의 성능 병목 지점을 파악하고 장애 분석에 적극 활용할 계획입니다.

이번 경험을 통해 MSA 환경에서의 분산 시스템 설계, 장애 대응 전략 등 실무에서 필요한 역량을 쌓을 수 있었습니다.

---

### 김주영
이번 프로젝트를 통해 MSA와 DDD를 적용해 보며 각 서비스가 독립적으로 동작하면 서로 통신하는 방식에 대해 더 깊게 알게 되었습니다.  
처음 해보는 방식이였기에 어려움도 있었지만 많은 것을 배울 수 있었습니다.

시간적 여유가 더 있었다면 메시징 방식과 최적화를 더 해보고 싶다는 생각을 했습니다.

---

### 김혜윤
이번 프로젝트에서는 MSA 구조 활용과 DDD 설계 원칙을 적용해보았습니다.  
기존 모놀리식 개발 환경에서는 하나의 변경이 다른 도메인까지 쉽게 영향을 주곤 했지만, MSA 환경에서는 서비스가 분리되어 있어 책임 범위가 명확하고 변경 사항이 상대적으로 적다는 장점을 체감할 수 있었습니다.

하지만 프로젝트를 진행하면서 가장 크게 느낀 점은 서비스가 독립적일수록 커뮤니케이션이 중요하다는 것을 깨달았습니다.  
또한 Feign Client를 활용해 서비스 간 통신을 진행해보면서 다음 프로젝트에서는 Kafka와 같은 메시징 큐를 도입하여 서비스 간 결합도를 낮추고 비동기 이벤트 기반 아키텍처로 확장성을 높이고 싶다는 생각이 들었습니다.

---

### 정민지
MSA 프로젝트를 진행하며 많은 것을 배우고 느낄 수 있었습니다.  
MSA 구조에서는 각 서비스가 독립적으로 개발되기 때문에 API 규격과 데이터 흐름을 명확히 공유하는 것이 매우 중요했습니다.
서비스별 진행 상황이 다르다 보니 FeignClient 호출 시 발생하는 예외나 응답 불일치 문제가 빈번하게 발생해 다소 까다로움을 느끼기도 했습니다.  
모놀리식 개발보다 팀원 간 소통과 협의가 더욱 중요하다는 점을 깨달았습니다.
데이터 통신 과정에서 어떤 방식과 형태로 데이터를 주고받을지에 대한 문제가 발생했었는데, 관련 담당자끼리 의견을 나누고 조언도 구하며 합의점을 찾을 수 있었습니다.  
모르는 부분이나 애매한 부분은 적극적으로 상의하면서 문제를 최소화할 수 있었고, 이를 통해 원활하게 협업할 수 있었습니다.
문제를 해결하는 과정에서, 단순히 내 서비스만 고려하는 것이 아니라 전체 시스템 관점에서 문제를 바라보는 시야를 기를 수 있었고, 팀 커뮤니케이션의 중요성을 다시 한 번 깨달을 수 있었습니다.  
첫 MSA 프로젝트라 미흡한 점과 보완할 부분도 많았지만, MSA 아키텍처를 경험하며 내 도메인에만 집중하지 않고 전체 서비스의 흐름을 이해하는 시야를 넓힐 수 있었던 소중한 기회였습니다.
