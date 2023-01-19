# CampMoA
---
- 프론트 및 JSP 관련 공부 자료
https://www.notion.so/invite/64788b6e1a4fadcbf4de2798c2a51c72c87f85f3
- spring boot 및 DB & java 공부 자료
https://velog.io/@hyukkk90
---
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
  - 1. 문자인증 API 사용.          (완료)
  - 2. 회원가입시 정규식 & 암호화 단방향 암호화 처리. (SHA512 사용) 
  - 3. 이메일 중복검사.            (완료)
  - 4. 정규식 및 유효성 검사       (완료)

  <img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212598647-83572376-e114-4509-a4d9-9a045a643ef2.png">
  <img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212599093-dcd10a69-7435-4e35-b6ba-4bac1c79c049.png">
   
  ---
  
* 로그인

<img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212782817-61d37c9c-e247-4d34-9305-ecb4cd39a243.png">
<img width="500" alt="image" src="https://user-images.githubusercontent.com/109578385/212785961-415b5e0b-3813-498a-8c74-221eea50960f.png">

  - 이메일 기억하기 ( 미구현 :: cookie 를 생성하여 유저 아이디 관련 정보를 저장 시킬 예정.)
  - 비밀번호 찾기 ( 미구현 :: 이메일 인증을 통한 찾기 기능 예정)
  - 이메일 찾기 ( 미구현  :: 이메일 인증을 통한 찾기 기능 예정)

  - 로그인 성공의 경우 session 으로 해당 유저의 정보를 올려두었습니다.
  - 로그인 성공 실패 여부에 관한 Ajax 처리

* 로그인 시 마이페이지 비밀번호 수정.
  - 1. 세션을 올려 로그인된 유저의 정보를 통해 회원 정보 수정기능.
    - session 으로 올려놓은 로그인 유저의 정보를 이용하여 로그인 확인 여부에 따라 로그인 로그아웃 으로 바뀌도록 만들었습니다.
    - 
![image](https://user-images.githubusercontent.com/109578385/212901118-7aa6836a-4c06-4857-a437-7ab0bced9c68.png)
![image](https://user-images.githubusercontent.com/109578385/212901224-664e1270-0d9f-4869-98b4-94332858e8fd.png)


  - 비밀번호 수정 기능 추가.  ( 완료. )
    - 현재비밀번호를 다시 입력하여 회원가입된 비밀번호와 같은지 비교 후 같다면 새로운 비밀번호 설정을 할수 있도록 하였습니다.

![image](https://user-images.githubusercontent.com/109578385/212901658-8ac4b1ab-e0c0-4238-b631-027f8b6b3041.png)
![image](https://user-images.githubusercontent.com/109578385/212901810-eb03ab5f-2e8e-4e98-98e6-7ee9a20b46e4.png)



* 게시판
  - 글 작성.     (완료)
  - 글 읽기.     (완료)
  - 글 수정.     (완료)
  - 글 삭제.     (완료)
  - 좋아요       (완료)
  - 페이징 처리.   (완료)

  - 댓글 기능 구현 예정.  ( 진행 예정. )
![image](https://user-images.githubusercontent.com/109578385/212902180-f4525c08-b85a-428d-b9d6-d3f71cf6826e.png)
![image](https://user-images.githubusercontent.com/109578385/212902416-8350e27f-6f5c-40f7-87fe-fb1e3ea25320.png)
![image](https://user-images.githubusercontent.com/109578385/212902497-2e7526c0-ed5c-40cf-a342-e95e8bc8ca13.png)



* 동행 구하기 게시판(진행중인 상태)
  - CKediter API 사용.
  - 동행 글 작성. (완료)

  ![image](https://user-images.githubusercontent.com/109578385/212903277-2f55cb60-b97e-4ae2-888e-08da2c31ea10.png)

  - 동행글 게시판 (완료)
    - 페이징 처리가 아닌 더보기 기능 구현. (완료)
  ![image](https://user-images.githubusercontent.com/109578385/212903633-1f108385-8c3b-456e-b95e-ce1bacc4d7bb.png)

  - 동행 글 상세보기 읽기. ( 완료 )
  - 동행 글 수정. ( 완료 )
  - 동행 글 삭제.( 완료 )
  - 동행 신청. ( 완료 )
  ![image](https://user-images.githubusercontent.com/109578385/213459298-b15dd7ed-66c2-42bb-9ec0-f212beec9b49.png)
  - 로그인 된 유저 본인이 동행신청 불가능 하도록 처리하였습니다.
  
  ![image](https://user-images.githubusercontent.com/109578385/213459696-14483b32-d0b4-433d-81ba-215b05a82beb.png)
  ![image](https://user-images.githubusercontent.com/109578385/213459820-415dc419-a2fa-4eab-8a7a-e6d6baf42377.png)
  ![image](https://user-images.githubusercontent.com/109578385/213459882-57583a65-0643-449b-bc07-9d3233922233.png)
  - 로그인된 유저가 와 게시글 작성자가 다른 경우, 글 수정,삭제 불가능 하도록 처리하였습니다.
  - 다른 유저가 동행 신청 클릭시 버튼 toggle 식으로 css, javaScript Ajax 처리를 하였습니다.

  - 댓글 남기기 ( 미구현 )

