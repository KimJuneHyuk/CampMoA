# CampMoA
CampMoA


## 캠핑 정보 공유 커뮤니티 사이트 & 캠핑 동행을 구하여 함께 캠핑을 다닐 수 있도록 만들기위한 페이지.

---
### 본 project 의 모든 css html 구현은 트립소다 등의 모든 페이지를 오마주로 만든 프로젝트 입니다.
---

### SQL (MariaDB) : IntellJ 사용. 
![image](https://user-images.githubusercontent.com/109578385/212899578-0ada9630-2502-4d5c-a953-adb6d4714f7c.png)
- 아직 미구현 기능까지 생각하여 DB 쿼리를 작성하였습니다.

---

### 구현기능 (Ajax 기능을 통한 유효성 검사 처리 및 중복 검사 처리를 하였습니다.)


* 회원가입
  - 1. 문자인증 API 사용.
  - 2. 회원가입시 정규식 & 암호화 단방향 암호화 처리. (SHA512 사용)

  <img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212598647-83572376-e114-4509-a4d9-9a045a643ef2.png">
  <img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212599093-dcd10a69-7435-4e35-b6ba-4bac1c79c049.png">
   
  ---
  
* 로그인

<img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212782817-61d37c9c-e247-4d34-9305-ecb4cd39a243.png">
<img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212785961-415b5e0b-3813-498a-8c74-221eea50960f.png">

  - 이메일 기억하기 ( 미구현 :: cookie 를 생성하여 유저 아이디 관련 정보를 저장 시킬 예정.)
  - 비밀번호 찾기 ( 미구현 )
  - 이메일 찾기 ( 미구현 )

  - 로그인 성공의 경우 session 으로 해당 유저의 정보를 올려두었습니다.
  - 로그인 성공 실패 여부에 관한 Ajax 처리

* 로그인 시 마이페이지 비밀번호 수정.
  - 1. 세션을 올려 로그인된 유저의 정보를 통해 회원 정보 수정기능.
    - session 으로 올려놓은 로그인 유저의 정보를 이용하여 로그인 확인 여부에 따라 로그인 로그아웃 으로 바뀌도록 만들었습니다.
<img width="427" alt="image" src="https://user-images.githubusercontent.com/109578385/212786654-ea760d48-ec3d-4835-bb53-3b8bc0acd792.png">
<img width="369" alt="image" src="https://user-images.githubusercontent.com/109578385/212786704-35c7b790-ed4d-4621-9fda-e60b5f7432e9.png">


  - 비밀번호 수정 기능 추가.  ( 완료. )
    - 현재비밀번호를 다시 입력하여 회원가입된 비밀번호와 같은지 비교 후 같다면 새로운 비밀번호 설정을 할수 있도록 하였습니다.


* 게시판
  - 글 작성.     (완료)
  - 글 읽기.     (완료)
  - 글 수정.     (완료)
  - 글 삭제.     (완료)
  - 좋아요       (Ajax 구현)   (완료)
  - 페이징 처리.   (완료)


  - 댓글 기능 구현 예정.  ( 진행 예정. )


* 동행 구하기 게시판(진행중인 상태)
  - CKediter API 사용.
  - 동행 글 작성.
  - 동행 글 상세보기 읽기.
  - 동행 글 수정.
  - 동행 글 삭제.
  - 동행 신청.

