## 1. Introduction

### 1.1 목적 (Purpose)

- 본 문서는 webOS 기반의 **Media Web Application**의 서버와 데이터베이스의 기술 문서이다 프로그램을 사용하면서 발생하는 사용자 로그 및 미디어 데이터를 수집 및 추출하고 서버로 전송하는 클라이언트 프로그램의 요구사항과 아키텍처 설계, 기술적 요구사항을 포함한다.

### 1.2 범위 (Scope)

해당 문서에는 후술한 요구사항을 범위로 한정한다.

- DB에 저장되어 있는 미디어 컨텐츠를 재생한다.
- 미디어 재생에서 발생한 사용자 로그 및 미디어 데이터를 서버로 전송한다.
- 사용자 선호도에 따른 비디오 추천 시스템을 제공한다.
- 시스템 리소스 상태(예: CPU, 메모리 사용량)를 모니터링한다.
- webOS 기반의 TV에서 작동하는 User Interface를 구현한다.
- 사용자의 좋아요와 북마크를 관리한다.


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
- MySQL : 오픈 소스 관계형 데이터베이스 관리 시스템(RDBMS)이다. 구조화된 데이터를 테이블 형식으로 저장하며 SQL(Structured Query Language)을 사용해 데이터를 쉽게 관리하고 검색할 수 있다.
- Luna Service API: webOS TV에서 제공하는 핵심 서비스와 기능을 포함하는 API로, webOS 시스템 서비스와의 통신을 가능하게 해준다. Luna Service API를 통해 다양한 시스템 기능에 액세스하고, 사용자 정의 기능을 webOS에 통합할 수 있다.

***

## 2. 종합기술 (Overall Description)

### 2.1 시스템 인터페이스 (System Interface)

- 시스템은 리눅스 환경을 기반으로 하며, Frontend는 Enact를 사용하여 UI를 구현하여 사용자와 소통한다. Backend는 Spring boot로 시스템을 구축하고  MySql를 이용하여 사용자와 미디어 데이터를 관리한다.

### 2.1 시스템 컨텍스트 (System Context)

![image](https://github.com/user-attachments/assets/18f2132c-7cc5-4139-9dd9-64fdc6f24aab)

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

**3.1.1 접속 시 첫 화면**

1. `FR01` 회원가입 버튼
    - `FR01-1` 사용자로부터 회원가입 요청을 받으면 아이디와 비밀번호를 입력받는다.
    - `FR01-2` 사용자로부터 실명, 닉네임, 나이, 성별 입력받는다.
    - `FR01-3` 사용자로부터 선호하는 영화 장르 10개 중 최대 4개 선택받는다.
    - `FR01-4` 사용자에게 프로필 이미지 리스트를 제공하고 기본 프로필을 선택하게 한다.
2. `FR02` 로그인 버튼
    - `FR02-1` 사용자로부터 로그인 요청을 받으면 아이디와 비밀번호를 입력받게 한다. 데이터베이스에 사용자 정보가 있는 지 확인한 뒤 유효한 계정이라면 JWT를 발급한다.

**3.1.2 로그인 이후**

1. `FR03` Videos 탭 
    - `FR03-1` 재생 가능한 미디어 목록을 전부 나열한다.
        - `FR03-1-1` 나열된 미디어들은 썸네일 이미지, 제목의 정보가 같이 보이도록 한다.
          ![image](https://github.com/user-attachments/assets/8f4da207-94f5-4908-a808-98fcdc6503ab)
    - `FR03-2` 기본 Video Player이다.
        - `FR03-2-1` 해당 영상의 시청 기록이 존재한다면 팝업으로 마지막으로 시청한 시간부터 이어 볼 것인 지 팝업을 표시한다
            - `FR03-2-1-1` 처음으로 버튼을 누르면 영상을 처음부터 재생한다.
              ![image](https://github.com/user-attachments/assets/368afe52-f088-4abb-91cc-6b42234eb760)
            - `FR03-2-1-2` 이어보기 버튼을 누르면 영상을 마지막 시청 시간부터 재생한다.
        - `FR03-2-2` 미디어 재생 중 첫 번째 버튼은 해당 영상에 대한 댓글을 볼 수 있다.
          ![image](https://github.com/user-attachments/assets/2e01b346-f20f-4a42-8d46-c08027f16d0d)
            - `FR03-2-6-1` 1부터 5까지의 별점을 부여할 수 있다.
            - `FR03-2-6-2` 댓글 내용을 작성할 수 있다.
            - `FR03-2-6-3` 작성 버튼을 누르면 댓글이 생성되어 댓글 목록 맨 위에 출력된다.
            - `FR03-2-6-4` 댓글은 생성 시간 내림차순으로 위에서 부터 출력된다.
        - `FR03-2-3` 미디어 재생 중 두 번째 버튼을 누르면 해당 영상의 재생 속도를 조절할 수 있다.
          ![image](https://github.com/user-attachments/assets/4a77e02e-c32a-47ff-9d24-ab1cd63acb0b)
        - `FR03-2-4` 미디어 하단의 세 번째 버튼을 누르면 해당 영상에 좋아요을 추가할 수 있다.
            - `FR03-2-4-1` 빈 하트를 누르면 좋아요가 추가된다.
            - `FR03-2-4-2` 활성화된 하트를 누르면 하트가 빈 하트로 변경되며 좋아요가 삭제된다.
        - `FR03-2-5` 미디어 재생 중 네 번째 버튼을 누르면 해당 영상에 북마크를 추가할 수 있다.
            - `FR03-2-5-1` 빈 별을 누르면 북마크가 추가된다.
            - `FR03-2-5-2`  활성화된 별을 누르면 빈 별로 변경되며 북마크가 삭제된다.
        - `FR03-2-6`미디어 재생 중 다섯 번째 버튼은 전체 화면 설정을 관리한다.
            - `FR03-2-6-1`전체화면이 아니라면 전체화면으로 전환한다.
                - `FR03-2-6-1-1` 전체화면에서는 미디어 하단의 버튼들을 감춰야한다.
            - `FR03-2-6-2` 전체화면이라면 작은 화면으로 전환한다.
2. `FR04` Recommend 탭.  사용자의 취향 아카이브에 맞는 미디어를 추천해 태그와 함께 표시한다.
(아카이브는 사용자의 선택한 취향 정보를 담고 있는 집합이다.)
- `FR04-1` 사용자의 취향 아카이브를 표시한다.
- ![image](https://github.com/user-attachments/assets/cd55275e-f0d7-4bb0-9ad2-35fa2739e4f7)
    - `FR04-1-1` 맨 왼쪽 아카이브는 추천 기준인 현재 아카이브를 표시한다.
    - `FR04-1-2` 현재 아카이브 옆으로 생성시간 내림차순으로 이전 아카이브를 표시한다.
    - `FR04-1-3` 아카이브에는 생성 시간과 선택한 취향 태그를 함께 표시한다.
    - `FR04-1-4` 각 아카이브 버튼을 누르면 현재 아카이브를 수정할 수 있다.
        - `FR04-1-4-1` 아카이브 버튼을 누르면 수정을 묻는 팝업을 띄운다.
            - `FR04-1-4-2-1` 수정 버튼을 누르면 현재 아카이브 위치로 이동한다.
            - `FR04-1-4-2-2` 취소 버튼을 누르면 팝업이 사라진다.
        - `FR04-1-4-2` 아카이브가 수정되면 이에 따라 미디어를 재추천한다.
    - `FR04-1-5` 아카이브 생성 버튼을 누르면 온보딩 화면으로 전환되며 새로운 아카이브를 생성한다.
- `FR04-2` 현재 취향 아카이브에 의해 추천된 미디어 + 유저의 나이와 연령대에서 많이 보는 미디어를 표시한다.
- `FR04-3` 조회수 상위 5개의 영상을 내림차순으로 표시한다.
    - `FR04-3-1` 각 영상의 조회수를 같이 표시한다.
3 `FR05`: My Page 탭. 사용자 계정에 대한 다양한 정보를 표시한다.
    - `FR05-1` 사용자의 계정 정보를 표시한다.
    - `FR05-2` 사용자의 북마크 영상들을 표시한다
    - `FR05-3` 사용자의 작성한 한줄평들을 표시한다.
        - `FR05-3-1` 댓글이 작성된 영상 정보를 간략하게 표시한다.
    - 'FR05-4' 사용자의 좋아요 영상들을 표시한다.
4 `FR06`: Status 탭. 시스템 자원 현황을 시각화해 그래프로 표시한다. (Luna API에서 참고하면 됨)
    - `FR06-1` 현재 TV에 대한 정보를 출력한다.
    - `FR06-2` 현재 CPU 현황을 user, system, nice, idle 네 가지 항목으로 나누어 파이 그래프에 나타낸다.
    - `FR06-3` 현재 Memory 현황을 current vmalloc size, swap used, usable memory 세 가지 항목으로 나누어 파이 그래프에 나타낸다.
    - `FR06-4` 새로고침 버튼을 누르면 시스템 자원 정보를 실시간 정보로 업데이트해 출력한다

### 3.2 품질 속성 (Quality Attribute)

1. `QA01` JWT를 통한 인증, 인가 과정을 통해 권한에 해당하는 기능만 수행해야한다.

### 3.3 제약 사항 (**Constraint Requirement)**

1. `CR01` Frontend는 Enact Framework로 작성한다.
2. `CR02` BackEnd는 Spring boot Framework로 작성한다.
3. `CR03` 데이터베이스는 MySql를 사용하며, Spring boot JPA를 사용하여 백엔드와 연결한다.
***

# 4. Architecture Overview

## 4.1 **Static Perspective**

### 4.1.1 Frontend architecture

- `FR01`: MyPage component
    - loadLikedVideos(): 사용자가 좋아요를 단 동영상 목록을 back server로부터 가져온다.
    - loadBookmarkedVideos(): 사용자가 북마크로 저장해 둔 동영상 목록을 back server로부터 가져온다.
    - loadComments(): 사용자가 남긴 댓글과 평점 목록을 back server로부터 가져온다.
- `FR02`: Recommend component
    - loadLatestArchive(): 사용자의 취향 아카이브 중 가장 최근에 저장한 아카이브를 불러온다.
    - loadArchives(): 사용자의 모든 취향 아카이브를 back server로부터 가져온다.
    - loadRecommendVideos(): 추천시스템에 의해 추천받은 사용자 맞춤형 5개의 동영상을 불러온다.
    - loadTargetGroupVideos(): 사용자가 속한 그룹(나이대&성별)이 좋아요를 많이 누른 순으로 상위 5개의 동영상을 불러온다.
    - loadTop5ViewedVideos(): 전체 동영상 중 조회수가 가장 높은 순으로 상위 5개 동영상을 불러온다.
- `FR03`: Status component
    - renderTVInfo(): TV 모델명과 같은 정보를 조회하여 불러온다.
    - setData(): setCpuData()와 setMemoryData()의 LUNA API 호출을 통해 실시간으로 CPU와 메모리 현황을 불러온다.
    - CpuDoughnutData()/MemDoughnutData(): CPU와 메모리 데이터를 도넛그래프로 시각화하기 위해 데이터를 세팅한다.
- `FR04`: Videos component
    - loadVideos(): 데이터베이스에 존재하는 모든 동영상 목록을 조회한다.
- `FR05`: Login component
    - handleLogin(): 사용자의 계정 정보를 전송해 서버에 인증 과정을 요청한다.
- `FR06`: Onboarding component
    - handleSelectGenre() : 선택한 장르 취향을 벡터로 관리한다.
    - handleSubmit() : 생성한 취향 벡터를 서버에 전송해 저장한다.
    - handleCloseButton() : 회원가입 완료 팝업을 닫는다.
- `FR07`: Signup component
    - handleLoginIdChange() : InputField의 loginId 값을 실시간으로 변수에 저장한다.
    - handlePasswordChange() : InputField의 password 값을 실시간으로 변수에 저장한다.
    - handleSummit() : 입력한 새 계정 정보를 서버에 전송해 회원가입을 요청한다.
    - handleCloseButton : Header의 close버튼을 누를 시 바로 직전 패널로 돌아간다.
- `FR08`: UserState component
    - handleSummit() : 입력한 유저의 상세정보를 서버에 전송해 저장한다.
    - handleGender() : 클릭한 Button의 값을 gender 변수에 저장한다.
    - handleCloseButton : Header의 close버튼을 누를 시 바로 직전 패널로 돌아간다.
- `FR09`: Video component
    - toggleFullScreen() : 전체화면 버튼을 클릭 시 영상의 크기가 전체화면으로 변경된다.
    - fetchIcons() : 랜더링 전에 좋아요와 북마크 여부를 서버에 요청해 icon을 상태에 맞게 설정한다.
          - 좋아요를 눌렀다면 heart 아이콘을, 누르지 않았다면 hearthollow를 icon으로 설정한다.
          - 북마크를 눌렀다면 star 아이콘을, 누르지 않았다면 starhollow를 icon으로 설정한다.
    - fetchVideo() : video 정보와 시청기록을 조회한다.
    - handlePlayFromStart() : 이어서 시청하기를 클릭하면 비디오의 현재 시점을 마지막 시청 시간으로 이동시킨다.
    - handlePlayFromTimestamp() : 처음부터 시청하기를 클릭하면 비디오의 현재 시점을 처음으로 이동시킨다.
    - handleLikeToggle() : 좋아요 버튼을 유저의 상태를 기반으로 수정한다.
    - handleBookmarkToggle() : 북마크 버튼을 유저의 상태를 기반으로 수정한다.
    - handleBack() : video Component의 backButton을 클릭 시 시청 기록을 생성하거나 업데이트해 저장하고 직전 패널로 이동한다.
    - handlePopupClose() : 북마크와 좋아요 버튼을 누를 시 짧은 시간동안 팝업으로 현재 상태를 알리고 사라지게 한다.
    - handleCommentToggle() : 서버에서 현재 비디오의 댓글들을 조회해 랜더링한다.
    - handleRating() : 누른 별의 위치에 따라 starhollow 아이콘을 star 아이콘으로 수정해 랜더링한다.
          - 4번째 별을 눌렀다면 1~4번째 별이 전부 star 아이콘으로 수정된다.
    - handleCommentSubmit() : 사용자의 선택한 별점과 댓글을 서버에 전송해 댓글을 생성한다.
    - handleSpeedChange() : 재생 속도를 수정한다.
    - toggleSpeedPopup() : 선택 가능한 재생 속도를 팝업으로 띄운다.

### 4.1.2 Backend architecture

- `BA01`: Recommend component
    - recommendVideos(): 사용자의 선호 취향으로 등록된 벡터를 기반으로 동영상의 장르 벡터 간 코사인 유사도를 계산하여 내림차순으로 정렬한 다음, 상위 5개 동영상을 추천한다.
    - addArchives(): 사용자의 선호 취향을 아카이브로 저장하고, 가장 최근에 저장된 취향을 기준으로 추천시스템을 가동한다.
- `BA02`: Update component
    - addCommentOnVideo(): 영상에 사용자가 댓글과 평점을 기입한다.
    - addLikeByVideoId(): 영상에 사용자가 좋아요를 등록한다.
    - addBookmarkByVideoId(): 사용자가 영상을 북마크에 등록한다.
- `BA03`: Get component
    - getAllBookmarkByUser(): 사용자가 북마크를 단 동영상 목록을 조회한다.
    - getLikedVideos(): 사용자가 좋아요를 단 동영상 목록을 조회한다.
    - getAllComments(): 사용자가 댓글과 평점을 남긴 동영상 목록을 조회한다.

## 4.2 Dynamic Perspective

### 4.2.1 State diagram

- `Before Login` 상태에서 Login 을 통해 `After Login`상태가 될 수 있다. Login 시 입력받은 ID와 password가 모두 일치해야 Login 상태가 될 수 있다. ID가 DB에 없거나 password가 불일치할 경우 Login은 성사되지 않는다.
- `After Login` 상태에서는 Logout을 통해 `Before Login` 상태로 되돌아간다.

![image](https://github.com/user-attachments/assets/a37bfbe1-f64b-416d-bd6e-460ec6c23b12)

### 4.2.2 Sequence diagram

클라이언트의 request에 따라 서버가 응답하고 DB에 접근하여 데이터를 처리할 때 수행되는 로직이다.

![image](https://github.com/user-attachments/assets/ebec4218-8d65-4cc5-bb2b-d352c0ffe5ed)

- 클라이언트는 프론트엔드의 UI를 통해 interface를 마주하고, 이를 매개로 백엔드에게 요청을 전송한다.
- 백엔드는 프론트엔드를 통해 받은 클라이언트의 요청을 데이터베이스에 접근하여 처리한다.
- 데이터베이스에서 처리된 작업은 다시 백엔드에 의해 프론트엔드에게 전송된다.
- 프론트엔드는 전송받은 서버의 응답을 UI를 통해 클라이언트에게 보여준다.

### 4.2.3 Module

1. Signup
    - 클라이언트로부터 사용자의 정보를 입력받아 새로운 계정을 생성하고 이를 DB에 저장한다.
    
    ![image](https://github.com/user-attachments/assets/657f41f9-bc18-43c6-89eb-2ed3d149f04a)
    
2. Login
    - 클라이언트로부터 loginID와 password를 입력받아 사용자에게 인가 여부를 판단하는 로그인을 진행한다.
    - 로그인에 성공하면 서버의 유저의 상태를 업데이트한다.
    ![image](https://github.com/user-attachments/assets/a82d78ca-b64f-4c1b-9ff7-6b7d1efd7deb)
    
3. Get video list
    - 사용자가 비디오 리스트(bookmarked, liked, comment…)를 요청하면 서버로부터 해당되는 비디오 리스트를 받아온다.
    
    ![image](https://github.com/user-attachments/assets/5ca13228-66a4-4d20-ba6d-f4b7fdb71410)
    
4. Play video
    - 클라이언트가 재생하고자 하는 동영상을 선택하면 백엔드에서 해당 동영상의 정보를 마지막 재생 시점을 포함하여 DB에서 가져온다.
    - 선택된 동영상의 조회수 및 사용자의 최근 시청 기록을 변경한다.
    - 마지막 시청 시점부터 동영상을 재생하고, 클라이언트가 종료하면 마지막 시청 시점을 다시 변경한다.
    
    ![image](https://github.com/user-attachments/assets/ffb5a6be-1105-41a6-bed7-6283bc01ebea)
    
5. Logout
    - 로그인이 되어 있던 상태에서 로그인이 되기 전 상태로 되돌아간다.

***

# 5. Data Design

![image](https://github.com/user-attachments/assets/92d8ed83-9223-4e1d-872c-1bafb892d506)

## Video 스키마

| 필드명 | 설명 |
| --- | --- |
| title | 비디오 제목 |
| subtitle | 비디오 제작자 또는 부제목 |
| description | 비디오에 대한 설명 |
| thumb | 비디오 썸네일 이미지 |
| source | 비디오 파일의 URL |
| bookmark | 북마크된 횟수 |
| like | 좋아요한 횟수 |
| views | 비디오 조회수를 나타냅니다. |
| genderViews | 성별에 따른 조회수 |

### 기능 요구사항 (Functional Requirements)

1. **미디어 정보 저장**
    - `FR03-1`: All Videos 화면에 재생 가능한 비디오 목록을 표시한다. (title, subtitle, thumbnail)
    - `FR03-2`: 비디오 정보는 썸네일 이미지와 제목, 부제목(subtitle)을 포함하여 나열된다.
    

---

### **User 스키마**

| 필드명 | 설명 |
| --- | --- |
| username | 사용자의 이름(실명) |
| password | 사용자의 암호화된 비밀번호. |
| gender | 사용자의 성별을 나타내며 여성, 남성 중 하나여야 한다. |
| age | 사용자의 나이. 0 이상의 범위를 갖는다. |
| comments | 사용자가 등록한  댓글의 Object 배열 |
| nickname | 사용자의 닉네임 |
| loginId | 로그인할 때 사용하는 ID |
| role | USER, ADMIN 중 택1 |
| latestArchiveId | 가장 최근에 저장한 아카이브의 Id |

### 기능 요구사항 (Functional Requirements)

1. **회원가입**
    - `FR01-1`: 사용자 정보를 입력받고 사용자 계정을 발급한다. (username,  loginId, password, age, gender)
2. **로그인**
    - `FR02-1`: 사용자 로그인 ID와 비밀번호를 입력받아 등록 여부를 확인하고 로그인한다.

---

### **UserGenre 스키마**

| 필드명 | 설명 |
| --- | --- |
| userId | User 테이블을 참조하는 외래키 |
| genreVectorString | 장르 취향 벡터를 문자열로 저장 |

### 기능 요구사항 (Functional Requirements)

1. **추천시스템**
    - `FR04-1`: genreVectorSrting에 저장된 문자열을 파싱하여 Vector 클래스로 인식한 다음, 비디오의 장르 취향 벡터와 코사인 유사도 계산한다.

---

### **VideoGenre 스키마**

| 필드명 | 설명 |
| --- | --- |
| videoId | Video 테이블을 참조하는 외래키 |
| genreVectorString | 장르 취향 벡터를 문자열로 저장 |

### 기능 요구사항 (Functional Requirements)

1. **추천시스템**
    - `FR04-1`: genreVectorSrting에 저장된 문자열을 파싱하여 Vector 클래스로 인식한 다음, 사용자의 장르 취향 벡터와 코사인 유사도 계산한다.

---

### Archive 스키마

| 필드명 | 설명 |
| --- | --- |
| userId | User테이블을 참조하는 외래키 |
| userGenreId | UserGenre테이블을 참조하는 외래키 |

### 기능 요구사항 (Functional Requirements)

1. 아카이브 저장
    - `FR05-1`: user가 userGenre를 등록하면, 해당 userId와 userGenreId를 조합하여 아카이브로 저장한다.
2. 아카이브 삭제
    - `FR05-2`: user가 아카이브를 삭제하면 해당 조합의 userId와 userGenreId 조합의 아카이브를 삭제한다.

---

### Like 스키마

| 필드명 | 설명 |
| --- | --- |
| userId | User테이블을 참조하는 외래키 |
| videoId | Video 테이블을 참조하는 외래키 |

### 기능 요구사항 (Functional Requirements)

1. 좋아요 추가
    - `FR06-1`: user가 특정 video에 좋아요를 등록하면, 해당되는 userId와 videoId 조합을 Like 스키마에 저장한다.
2. 좋아요 삭제
    - `FR06-2`: user가 특정 video에 좋아요를 취소하면, 해당 조합의 userId와 videoId 조합의 좋아요를 삭제한다

---

### Comment 스키마

| 필드명 | 설명 |
| --- | --- |
| rating | 1~5점 범위의 별점 평가 |
| content | 댓글 내용 |
| userId | 댓글을 남기는 사용자의 id (User 테이블을 참조하는 외래키) |
| videoId | 사용자가 댓글을 남기는 비디오의 id (Video 테이블을 참조하는 외래키) |

### 기능 요구사항 (Functional Requirements)

1. 댓글 작성
    - `FR07-1`: user가 특정 video에 댓글과 평점을 남기면, 해당 조합의 userId와 videoId와 함께 Comment 스키마에 저장된다.
2. 댓글 삭제
    - `FR07-2`: user가 특정 video에 댓글과 평점을 지우면, 해당 조합의 userId와 videoId와 함께 Comment 스키마에서 삭제된다.

---

### Bookmark 스키마

| 필드명 | 설명 |
| --- | --- |
| userId | User테이블을 참조하는 외래키 |
| videoId | Video 테이블을 참조하는 외래키 |

### 기능 요구사항 (Functional Requirements)

1. 북마크에 비디오 추가
    - `FR08-1`: user가 video를 bookmark 목록에 추가하면, 해당 userId와 videoId 조합을 Bookmark 스키마에 저장한다.
2. 북마크에서 비디오 제거
    - `FR08-2`: user가 video를 bookmark 목록에서 삭제하면 해당 조합의 userId와 videoId 조합의 북마크를 삭제한다.

***

# 6. Architecture/Design Decisions

## Frontend

- **비디오 플레이어 탭 구현: `TabLayout` 활용**
    - **결정 이유**:
        - **사용자 경험 향상**: 별도의 화면으로 이동하지 않고 탭 내에서 직접 영상을 재생하여 편의성을 높임
        - **효율적 화면 전환**: 화면 간 이동의 불편함을 줄여 사용자와의 상호작용을 매끄럽게 유지
    - **고려 사항 및 대안**:
        - **탭 내장 플레이어**: 제한된 화면 공간으로 인해 사용자 시청 경험에 제약이 있을 수 있음
        - **별도 재생 화면**: 전체 화면 비디오 재생을 지원하지 않아 인터랙션 중심의 사용자 경험을 우선시 함

- **시각화 도구 변경: **`nivo`**에서 **`Chart.js`**로 전환
    - **결정 이유**:
        - **호환성 문제 해결:** webOS와 Enact 프레임워크가 일부 React API를 지원하지 않아 nivo 사용 시 렌더링 오류가 발생함. Chart.js는 이러한 호환성 문제를 해결하고 안정적으로 동작함.
        - **안정된 시각화 성능**: Chart.js는 높은 호환성과 안정적인 성능을 제공하여 프로젝트 요구사항을 충족함.
    - **고려 사항 및 대안**:
        - **시각적 다양성의 제한**: nivo가 제공하는 풍부한 시각적 표현력과 유연성을 Chart.js가 모두 충족하지 못할 가능성이 있음.
        - **기술적 변경 작업**: 기존 **nivo** 기반으로 작성된 코드와 데이터 구조를 Chart.js에 맞게 수정해야 하는 추가 작업이 필요함.
    
- **라우팅 방식 변경: **`BrowserRouter`**에서 **`HashRouter`**로 전환
    - **결정 이유**:
        - **호환성 문제 해결**: webOS와 Enact 환경에서 BrowserRouter를 사용할 때 화면 로드 오류가 발생함. HashRouter는 클라이언트 측 라우팅을 지원해 이러한 문제를 해결함.
        - **TV 화면 최적화**: TV와 같은 특수 환경에서 더 나은 호환성과 안정적인 화면 로딩을 제공하기 위해 HashRouter를 채택함.
    - **고려 사항 및 대안**:
        - **URL 구조의 변화**: HashRouter 사용으로 URL이 해시(#) 기반 구조로 바뀌어 URL 가독성이 다소 떨어질 수 있음.
        - **History API 제한**: BrowserRouter에서 제공하던 History API의 일부 기능을 사용할 수 없게 됨.

## Backend

- **추천시스템 구현**
    - **컴포넌트:** `Vector` 클래스 새로 정의
        
        ```java
        private static final List<String> *ALL_GENRES* = Arrays.*asList*(        
        	"Action", "Comedy", "Drama", "SF", "Romance", 
        	"Thriller", "Horror", "Animation", "Crime", "Adventure"
        );
        ```
        
    - 미리 지정해놓은 위와 같은 10개의 장르 리스트 중 해당하면 1.0, 해당되지 않으면 0.0의 값을 갖는 벡터로 취향 수집
    - **10개의 장르 선정 근거:** 네이버 영화 및 구글 위키 백과 등 다양한 참고 자료에서 공통되게 나타나는 장르들로 선정 (ex. “[1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0]” 인 경우 Action, SF, Adventure라는 장르에 해당된다는 뜻)
    - 비디오 장르 벡터와 사용자 취향 장르 벡터 간 코사인 유사도를 계산하여 유사도가 높은 순으로 상위 5개의 비디오를 추천
        - 왜 코사인 유사도? → 코사인 함수의 특성으로 인해 -1~1 사이의 값만을 가짐 + 두 벡터 간 유사한 정도를 수치로 나타내기에 적합
        
        ![image](https://github.com/user-attachments/assets/b2ae348a-0f4c-4ea9-8415-256a1f2d2a70)
        
    - **벡터 요소 가중치:** 사용자가 추후에 평점을 남기면, 그 평점을 남긴 영화 장르에 대해 +,- 가중치를 더함
    - **가중치 산정방법:** 1~5점 범위의 별점에서 2.5를 뺀 다음 2.5로 나누어 정규화 → 낮은 별점에 대해서는 선호도 (-), 높은 별점에 대해서는 선호도 (+)를 적용하기 위해
    
- **사용자 취향 저장소 - “아카이브”**
    - **구현 이유:** 사용자 취향의 변경에 따라 달라지는 유의미한 추천시스템을 구현하기 위해 취향 삭제와 추가를 가능하게 해주는 아카이브 기능을 구현.
    - **추천시스템에 적용되는 방안:** 아래와 같은 SQL문을 통해 사용자가 갖고 있는 아카이브 중 가장 최근에 저장된 아카이브를 추출한 다음, 그 아카이브를 기준으로 코사인 유사도를 계산
        
        ```java
        @Query("SELECT a FROM Archive a WHERE a.user = :user ORDER BY a.updatedAt DESC LIMIT 1")
        Optional<Archive> findLatestArchiveByUser(@Param("user") User user);
        ```
        
    - 아카이브 삭제 시, 최신(Latest) 아카이브도 동시에 업데이트

- **디렉토리 구조 - “도메인형 패키지 구조”**
    
    
    ![image](https://github.com/user-attachments/assets/a0694d96-117f-49f5-ad3d-089239e6c795)
    
    - **도메인 기반으로 코드 가독성 향상**
        - 기능별로 디렉토리를 나누는 대신 도메인(예: `user`, `video`, `bookmark`) 중심으로 구조화하면 코드의 목적과 맥락이 명확해짐
    - **관심사의 분리 (Separation of Concerns)**
        - 도메인 단위로 엔티티, 서비스, 컨트롤러 등을 구성하면 서로 다른 도메인 간의 의존성을 최소화할 수 있어 유지보수성과 확장성이 증가함
        - 특정 도메인의 기능 수정 시 다른 도메인에 영향을 줄 가능성이 감소
    - **확장성과 모듈화 용이**
        - 도메인별로 독립적인 모듈처럼 관리할 수 있어, 프로젝트가 커져도 새로운 기능이나 도메인을 추가하기가 용이
        - 특정 도메인을 독립된 서비스로 분리하거나, 마이크로서비스 아키텍처로 전환하기에 적합

- **서버 배포**
    - **EC2 선택(장점):** 다양한 참고 자료가 존재하고 구현 난이도가 낮다는 점을 고려해 EC2 인스턴스를 통해 배포함
    - **단점:** 비용 절감을 위해 저비용 인스턴스를 선택했으나, 인스턴스 크기가 작아 동시에 많은 요청이 발생할 경우 서버의 안정성이 떨어질 수 있음
    - **도커(Docker) 사용**
        - **선택 이유**:
            
            Docker를 선택한 이유는 애플리케이션의 배포 및 실행 환경을 표준화하여 운영체제나 환경 차이에 따른 문제를 최소화할 수 있기 때문 + 컨테이너화된 애플리케이션은 이식성이 뛰어나며, 팀 내 협업과 테스트 환경 구성에도 유리함.
            
        - **장점**:
            1. **환경 일관성**: 개발, 테스트, 배포 환경 간의 차이를 제거하여 일관성을 제공
            2. **이식성**: 컨테이너 이미지 하나로 어디서든 동일한 애플리케이션을 실행할 수 있음
            3. **경량성**: VM보다 가볍고, 실행 속도가 빠르며 시스템 리소스 사용이 적음
            4. **빠른 배포**: 이미지 기반 배포로 빠르게 배포 및 롤백이 가능함
            5. **확장성**: 마이크로서비스 아키텍처와 잘 어울리며, 필요에 따라 컨테이너를 손쉽게 추가하거나 제거 가능함
            6. **커뮤니티 및 레퍼런스**: Docker Hub에 다양한 이미지와 레퍼런스가 제공되어 빠르게 활용할 수 있음
    ***
  #Rest API#
    
    ### Request
    
    | ID | URL | HOST | METHOD |
    | --- | --- | --- | --- |
    | BA01-1 | /api/users | http://localhost:8080 | POST |
    
    ### Parameter
    
    | Name | Type | Description | Required |
    | --- | --- | --- | --- |
    | username | string | 사용자명 |  |
    | email | string | 이메일 |  |
    | password | string | 비밀번호 |  |
    | profilePicture | Number(1~15) | 프로필 사진 번호 |  |
    | gender | string(female,male,other) | 성별 |  |
    | age | Number | 나이 |  |
    
    ### Response
    
    | Status code | Name | Type | Description |
    | --- | --- | --- | --- |
    | 200 |  | Object[] | 생성한 사용자의 정보 객체 |
    | 400 | message | string | 중복된 이메일 에러메시지 |
    | 403 | message | string | 이미 로그인한 상태 에러메시지 |
    | 500 | message | string | 서버 오류 에러메시지 |

# 1. 사용자 정보 저장 및 접근하기

사용자 정보 저장 및 접근하기는 클라이언트로부터 받은 사용자 정보를 데이터베이스에 저장 및 수정하거나 요청된 사용자 정보를 반환하는 API입니다.

## 1.1. Create user

데이터베이스에 저장 요청 받은 새로운 사용자 정보를 데이터베이스에 저장하고 사용자 정보를 응답합니다.

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA01-1 | /api/v1/user/signup | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| username | string | 사용자명 | TRUE |
| email | string | 이메일 | TRUE |
| password | string | 비밀번호 | TRUE |
| profilePicture | Number(1~15) | 프로필 사진 번호 | TRUE |
| gender | string(female,male,other) | 성별 | TRUE |
| age | Number | 나이 | TRUE |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 |  | Object[] | 생성한 사용자의 정보 객체 |
| 400 | message | string | 중복된 이메일 에러메시지 |
| 403 | message | string | 이미 로그인한 상태 에러메시지 |
| 500 | message | string | 서버 오류 에러메시지 |

## 1.2. Login

회원가입을 완료한 유저가 로그인을 시도할 때, 올바른 ID와 PW가 입력되었는지 확인하고 로그인을 성공하면 JWT토큰을 발급하고 유저의 정보를 반환합니다.

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA01-2 | /login | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| username | string | 사용자명 | TRUE |
| email | string | 이메일 | TRUE |
| password | string | 비밀번호 | TRUE |
| profilePicture | Number(1~15) | 프로필 사진 번호 | TRUE |
| gender | string(female,male,other) | 성별 | TRUE |
| age | Number | 나이 | TRUE |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 |  | Object[] | 생성한 사용자의 정보 객체 |
| 400 | message | string | 중복된 이메일 에러메시지 |
| 403 | message | string | 이미 로그인한 상태 에러메시지 |
| 500 | message | string | 서버 오류 에러메시지 |

## 1.3 Onboarding

회원가입이 완료된 이후 필수적으로 진행되는 사용자 정보 수집 단계입니다. 

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA01-3 | /api/v1/user/onboarding/{userId} | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| username | string | 사용자명 |  |
| nickname | string | 닉네임(별명) |  |
| gender | string(female,male,other) | 성별 |  |
| age | Number | 나이 |  |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | UserResponseDTO | Object[] | 생성한 사용자의 정보 객체
userId, loginId, nickname, username, gender, age |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다` |
| 500 | message | string | 서버 오류 에러메시지 |

## 1.4 Gather Genre Preference

온보딩 단계에서 유저의 장르에 대한 선호도를 수집합니다. 총 10개의 영화 장르 중 최대 4개를 선택하게 합니다. 10개의 영화 장르는 아래와 같습니다.

```java
private static final List<String> *ALL_GENRES* = Arrays.*asList*(        
	"Action", "Comedy", "Drama", "SF", "Romance", 
	"Thriller", "Horror", "Animation", "Crime", "Adventure", "War"
);
```

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA01-4 | /api/v1/user-genre/onboarding/{userId} | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| genrePreferences | `Map<String, Double>` | 위에 기재된 `ALL_GENRES` 중 입력하고자 하는 장르만 기재해도 가능. (즉, 반드시 각 10개 장르에 대해 모두 기입 안해도 됨)
장르 이름이 `ALL_GENRES` 에 없으면 저절로 누락함. 별도로 값을 기입하지 않은 장르에 대해서는 Default값인 0.0으로 저장됨.

유저가 선호하는 장르로 선택하면 `1.0` , 선택 안하면 `0.0` 으로 온보딩 단계에서 수집. | {
    "genrePreferences": {
        "Action": 1.0,
        "Romance":1.0,
        "Adventure":1.0
    }
} |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | UserResponseDTO | Object[] | 생성한 사용자의 정보 객체
userId, loginId, nickname, username, gender, age |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다` |
| 500 | message | string | 서버 오류 에러메시지 |

## 1.5 Recommend

기본 회원 정보와 온보딩 과정에서 수집한 유저의 취향 정보를 토대로 개인 맞춤형 비디오 5개를 추천합니다. 

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA01-5 | /api/v1/user/recommend/{userId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | List<VideoResponseDTO> | Object[] | 추천받은 비디오의 정보 객체의 리스트
videoId, title, subtitle, source, thumbnail, tagList, likes, comments, views, doesLike |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다` |
| 500 | message | string | 서버 오류 에러메시지 |

# 2. 아카이브

## 2.1 아카이브 추가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA02-1 | /api/v1/archive/{userId} | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| userGenreId | Long | 아카이브에 추가할 해당 유저의 userGenre의 ID   |  |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | ArchiveDTO | Object[] | 성공적으로 추가된 아카이브의 정보
Long archiveId;
Long userId;
List<String> userGenreList; LocalDateTime updatedAt; |
| 403 | message | string | 해당 회원 데이터와 취향 벡터 데이터가 이미 존재하는 충돌 에러메시지
`이미 해당 조합의 아카이브가 존재합니다.` |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다`
회원의 취향 벡터 부재 에러메시지
`이미 해당 조합의 아카이브가 존재합니다.` |
| 500 | message | string | 서버 오류 에러메시지 |

## 2.2 아카이브 삭제

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA02-2 | /api/v1/archive/{userId}/{archiveId} | http://localhost:8080 | DELETE |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | ArchiveDTO | Object[] | 성공적으로 삭제된 아카이브의 정보
Long archiveId;
Long userId;
List<String> userGenreList; LocalDateTime updatedAt; |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다`
아카이브 부재 에러메시지
`해당 아카이브는 존재하지 않습니다` |
| 500 | message | string | 서버 오류 에러메시지 |

## 2.3  아카이브 수정

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA02-3 | /api/v1/archive/{userId}/{archiveId} | http://localhost:8080 | PATCH |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| userGenreVector | string | 사용자명 | 수정 결과로 원하는 벡터 (사전에 설정한 10개의 영화 장르에 맞춰 값을 입력)
”userGenreVector”: “[1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0]” |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | ArchiveDTO | Object[] | 성공적으로 수정된 아카이브의 정보
Long archiveId;
Long userId;
List<String> userGenreList; LocalDateTime updatedAt; |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다`
아카이브 부재 에러메시지
`해당 아카이브는 존재하지 않습니다` |
| 500 | message | string | 서버 오류 에러메시지 |

## 2.4 유저의 전체 아카이브 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA02-4 | /api/v1/archive/{userId}/all | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<ArchiveResponseDTO>` | Object[] | 유저의 모든 아카이브를 조회하여 리스트로 반환 (`ArchiveResponseDTO`는 아래와 같음)
Long archiveId;
Long userId;
List<String> preferenceList;
LocalDateTime updatedAt; |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다` |
| 500 | message | string | 서버 오류 에러메시지 |

## 2.5 유저의 특정 아카이브 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA02-5 | /api/v1/archive/history/{userId}/{historyId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 |  | Object[] | 생성한 사용자의 정보 객체 |
| 400 | message | string | 해당 회원과 아카이브 조합 부재 에러메시지
`현재 유저는 해당 archiveId를 갖는 archive를 소유하고 있지 않습니다.` |
| 404 | message | string | 회원 데이터 부재 에러메시지
`존재하지 않는 회원입니다`
아카이브 부재 에러메시지
`해당 아카이브는 존재하지 않습니다` |
| 500 | message | string | 서버 오류 에러메시지 |

# 3. 비디오 정보 저장 및 접근하기

## 3.1 비디오 전체 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-1 | /api/v1/video | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<VideoResponseDTO>` | Object[] | 전체 비디오 목록을 조회 (`VideoResponseDTO`는 아래와 같음)
Long videoId;
String title;
String subtitle;
String source;
String thumbnail;
String description;
List<String> tagList;
Integer likes;
Integer comments;
Integer views;
Boolean doesLike; |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.2 특정 비디오 정보 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-2 | /api/v1/video/{videoId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Video` | Object | videoId에 해당되는 비디오 반환 |
| 400 | message | string | 중복된 이메일 에러메시지 |
| 403 | message | string | 이미 로그인한 상태 에러메시지 |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.3 비디오에 달린 댓글 전체 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-3 | /api/v1/video/{videoId}/comment | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<Comment>` | Object[] | videoId에 해당되는 비디오와 매핑되는 모든 Comment를 리스트로 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.4 조회수 상위 5개 비디오 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-4 | /api/v1/video/view-top5 | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<VideoResponseDTO>`  | Object[] | 조회수(views) 상위 5개의 비디오 목록을 조회 (`VideoResponseDTO`는 아래와 같음)
Long videoId;
String title;
String subtitle;
String source;
String thumbnail;
String description;
List<String> tagList;
Integer likes;
Integer comments;
Integer views;
Boolean doesLike; |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.5 비디오 조회 증가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-5 | /api/v1/video/{videoId}/view | http://localhost:8080 | POST |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `VideoResponseDTO` | Object | 생성한 비디오의 정보 객체 |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.6 비디오 시청기록 추가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-6 | /api/v1/video/{videoId}/history | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| timestamp | int | 마지막 시청 시점의 타임스탬프  |  |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 |  | Boolean | 마지막 시청 시점 추가 성공 시 True 반환, 실패 시 False 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 3.7 비디오 시청기록 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA03-7 | /api/v1/video/{videoId}/history | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `VideoHistory` | Object | videoId에 해당되는 비디오의 기록 (`VideoHistory`는 아래와 같음)
Long id;
User user;
Video video;
int timestamp; |
| 500 | message | string | 서버 오류 에러메시지 |

# 4. 북마크 정보 저장 및 조회하기

## 4.1 북마크 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA04-1 | /api/v1/bookmark/{videoId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Bookmark` | Object | videoId에 해당되는 비디오 (`Bookmark`는 아래와 같음)
Long id;
User user;
Video video; |
| 500 | message | string | 서버 오류 에러메시지 |

## 4.2 북마크 추가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA04-2 | /api/v1/bookmark/{videoId} | http://localhost:8080 | POST |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | videoId에 해당되는 비디오가 북마크에 포함되면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 4.3 북마크 제거

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA04-3 | /api/v1/bookmark/{videoId} | http://localhost:8080 | DELETE |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | videoId에 해당되는 비디오가 북마크에서 제거되면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

# 5. 댓글 정보 저장 및 조회하기

## 5.1 댓글 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA05-1 | /api/v1/comment/{videoId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<Comment>` | Object[] | videoId에 해당되는 비디오에 달린 댓글 목록을 리스트로 반환 (`Comment`는 아래와 같음)
Long id;
int rating;
String content;
User user;
Video video; |
| 500 | message | string | 서버 오류 에러메시지 |

## 5.2 댓글 추가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA05-2 | /api/v1/comment/{videoId} | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| rating | int | 평점 (최소 1~최대 5) |  |
| content | string | 댓글 내용 |  |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | videoId에 해당되는 비디오에 댓글이 정상적으로 추가되면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 5.3 댓글 삭제

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA05-3 | /api/v1/comment/{commentId} | http://localhost:8080 | DELETE |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | commentId에 해당되는 댓글이 제거되면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 5.4 댓글 수정

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA05-4 | /api/v1/comment/{userId}/{commentId} | http://localhost:8080 | POST |

### Parameter

| Name | Type | Description | Required |
| --- | --- | --- | --- |
| rating | int | 평점 (최소 1~최대 5) |  |
| content | string | 댓글 내용 |  |

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | commentId에 해당되는 댓글이 정상적으로 변경되면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

# 6. 좋아요 정보 저장 및 조회하기

## 6.1 좋아요 누른 비디오 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA06-1 | /api/v1/like/{videoId} | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Like` | Object | videoId에 해당되는 비디오에 달린 좋아요를 반환 (`Like`는 아래와 같음)
Long id;
User user;
Video video; |
| 500 | message | string | 서버 오류 에러메시지 |

## 6.2 좋아요 추가

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA06-2 | /api/v1/like/{userId}/{videoId} | http://localhost:8080 |  |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | message | string | `Like added successfully` |
| 500 | message | string | 서버 오류 에러메시지 |

## 6.3 좋아요 삭제

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA06-3 | /api/v1/like/{videoId} | http://localhost:8080 | DELETE |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `Boolean` | Object | videoID에 해당되는 비디오의 좋아요가 감소하면 True, 아니면 False를 반환 |
| 500 | message | string | 서버 오류 에러메시지 |

## 6.4 사용자가 속한 타겟 그룹의 좋아요 내림차순 결과 조회

### Request

| ID | URL | HOST | METHOD |
| --- | --- | --- | --- |
| BA06-4 | /api/v1/comment/{userId}/target-group | http://localhost:8080 | GET |

### Parameter

없음

### Response

| Status code | Name | Type | Description |
| --- | --- | --- | --- |
| 200 | `List<VideoResponseDTO>` | Object[] | 타겟 그룹의 좋아요 상위 5개  비디오 목록을 조회 (`VideoResponseDTO`는 아래와 같음)
Long videoId;
String title;
String subtitle;
String source;
String thumbnail;
String description;
List<String> tagList;
Integer likes;
Integer comments;
Integer views;
Boolean doesLik |
| 500 | message | string | 서버 오류 에러메시지 |
