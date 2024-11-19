## 1. Introduction

### 1.1 목적 (Purpose)

- 본 문서는 webOS 기반의 **Media Web Application**의 서버와 데이터베이스의 기술 문서이다 프로그램을 사용하면서 발생하는 사용자 로그 및 미디어 데이터를 수집 및 추출하고 서버로 전송하는 클라이언트 프로그램의 요구사항과 아키텍처 설계, 기술적 요구사항을 포함한다.

### 1.2 범위 (Scope)

해당 문서에는 후술한 요구사항을 범위로 한정한다.

- DB에 저장되어 있는 미디어 컨텐츠를 재생한다.
- 미디어 재생에서 발생한 사용자 로그 및 미디어 데이터를 서버로 전송한다.
- 사용자 선호도에 따른 비디오 추천 시스템을 제공한다.
- 시스템 리소스 상태(예: CPU, 메모리 사용량)를 모니터링한다.
- Adaptive Streaming을 통해 다양한 미디어 포맷을 지원한다.
- webOS 기반의 TV에서 작동하는 User Interface를 구현한다.
- 수집한 미디어 데이터 시각화를 통계해 모니터링한다.

### 1.3 용어 및 약어 정리 (Definitions and Abbreviations)

- Codec (Coder & Decoder) : 음성 또는 영상의 신호를 디지털 신호로 변환하는 코더와 그 반대로 변환시켜 주는 디코더를 통틀어 부르는 용어이다.
- GUI (Graphic User Interface) : 사용자가 편리하게 사용할 수 있도록 입출력 등의 기능을 알기 쉬운 아이콘 따위의 그래픽으로 나타내서 사용자와 컴퓨터가 상호 작용하는 방식이다.
- CLI (Command Line Interface) : 텍스트 터미널을 통해 사용자와 컴퓨터가 상호 작용하는 방식이다.
- UI (User Interface) : 사용자와 사물 또는 시스템, 컴퓨터 프로그램 등 사이에서 의사소통을 할 수 있도록 일시적 또는 영구적인 접근을 목적으로 만들어진 물리적, 가상적 매개체를 뜻한다.
- POSIX (Portable Operating System Interface) : 서로 다른 UNIX OS의 공통 API를 정리하여 이식성이 높은 유닉스 응용 프로그램을 개발하기 위한 목적으로 IEEE가 책정한 애플리케이션 인터페이스 규격이다.
- API (Application Programming Interface) : 응용 프로그램에서 사용할 수 있도록, 운영 체제나 프로그래밍 언어가 제공하는 기능을 제어할 수 있게 만든 인터페이스를 뜻한다.
- Spring boot : Java 기반의 애플리케이션 개발을 간소화하기 위한 프레임워크이다. 복잡한 설정 없이 빠르게 애플리케이션을 개발할 수 있도록 자동 설정, 내장 서버, 스타터 의존성 등을 제공한다. 마이크로서비스 아키텍처와 RESTful API 개발에 적합하며 생산성과 확장성을 동시에 지원한다.
- React: 사용자 인터페이스를 구축하기 위한 JavaScript 라이브러리이다. 컴포넌트 기반의 접근 방식을 통해 효율적인 UI 개발을 가능하게 한다.
- Enact: 대형 화면 기기를 위한 사용자 인터페이스 개발에 특화된 React 기반의 응용 프로그램 프레임워크이다. 성능 최적화, 접근성, 국제화를 포함한 다양한 기능을 갖추고 있으며, webOS 플랫폼과의 호환성에 중점을 둔다.
- Redux: JavaScript 앱의 상태 관리를 위한 라이브러리이다. 애플리케이션의 상태를 중앙 집중식 저장소에서 관리하여 일관된 동작과 쉬운 상태 관리를 가능하게 한다.
- MySQL : 오픈 소스 관계형 데이터베이스 관리 시스템(RDBMS)이다. 구조화된 데이터를 테이블 형식으로 저장하며 SQL(Structured Query Language)을 사용해 데이터를 쉽게 관리하고 검색할 수 있다.
- HLS: Apple Inc.에 의해 개발된 비디오 스트리밍 프로토콜이다. 적응형 스트리밍을 지원하여 다양한 네트워크 속도와 장치에서 안정적인 스트리밍을 제공한다. HLS는 m3u8 재생 목록 파일을 통해 콘텐츠를 전달한다.
- Luna Service API: webOS TV에서 제공하는 핵심 서비스와 기능을 포함하는 API로, webOS 시스템 서비스와의 통신을 가능하게 해준다. Luna Service API를 통해 다양한 시스템 기능에 액세스하고, 사용자 정의 기능을 webOS에 통합할 수 있다.

***

## 2. 종합기술 (Overall Description)

### 2.1 시스템 인터페이스 (System Interface)

- 시스템은 리눅스 환경을 기반으로 하며, Frontend는 Enact를 사용하여 UI를 구현하여 사용자와 소통한다. Backend는 Spring boot로 시스템을 구축하고  MySql를 이용하여 사용자와 미디어 데이터를 관리한다.

### 2.1 시스템 컨텍스트 (System Context)

<img src="https://github.com/user-attachments/assets/a5f6e662-a99d-42fb-9c3c-b6266f5978a9">

**User**

프로그램을 사용하는 사용자를 의미한다. 사용자는 프론트엔드에서 제공하는 UI를 통해 지원하는 기능을 사용할 수 있다.

**FrontEnd**

본 프론트엔드의 핵심 기능은 사용자에게 다양한 미디어 콘텐츠 재생 기능을 제공하는 것이다. 사용자가 재생하고 싶은 미디어 목록을 선택할 수 있도록 하고, 이어 듣기 기능을 위해 사용자의 재생 기록을 백엔드와 주고받는다. 또한, 사용자의 기기 자원 현황과 사용 패턴을 시각적으로 보여주는 기능을 통해 더욱 편리한 사용 환경을 제공한다.

**BackEnd**

프론트엔드에서 요청하는 정보를 제공하는 서버이다. 사용자 정보와 미디어 정보를 데이터베이스에 저장하고 관리하며, 프론트엔드의 데이터 생성, 삭제, 수정 요청을 처리한다. 시스템 현황은 프론트엔드의 요청이 있을 때만 갱신하여 전달한다.

### 2.2 사용자 인터페이스 (User Interface)

- 사용자의 편의를 최우선으로 고려하여 사용자 정보와 미디어 목록을 손쉽게 확인할 수 있도록 제공한다. 플레이어 화면에서는 재생, 일시정지, 중지, 10초 앞으로 감기 등의 기능을 직관적으로 사용할 수 있도록 UI를 설계한다.

## 3. 상세 요구사항 (Specific Requirements)

### 3.1 기능 요구사항 (Functional Requirements)
### 3.2 품질 속성 (Quality Attribute)
### 3.3 제약 사항 (Constraint Requirement)
***

# 4. Architecture Overview
## 4.1 **Static Perspective**
### 4.1.1 Frontend architecture
### 4.1.2 Backend architecture

## 4.2 Dynamic Perspective

### 4.2.1 State diagram
### 4.2.2 Sequence diagram
### 4.2.3 Module

***

## 5.Data Design

***

## 6. Architecture/Design Decisions
