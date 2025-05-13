
# 개발 환경
### 항목 - 내용 

- #### IDE    - IntelliJ IDEA Ultimate
- #### Java     -     17
 - #### Spring Boot -  3.4.5
- ####  DB         -   SQLite
- ####  SQL Mapper -   MyBatis
- ####  Build Tool  -  Maven
- #### OAuth2      -  Spring Security OAuth2 Client
- #### JWT       -    0.12.3
- #### TEST      -    JUnit5
- #### API 테스트 도구- Postman

--- 
# 테스트 방법


## 1. 회원가입
- 호출 API - [POST] http://localhost:8080/api/users/signup

- 호출 시 필수 항목 - userId(String), password(String), name(String), email(String)

- Body (JSON)
- 
                    {
                        "userId": "",
                        "password": "",
                        "name": "",
                        "email": ""
                    }

## 2. 로그인
- 호출 API - [POST] http://localhost:8080/api/users/login
  
- 호출 시 필수 항목 - userId(String), password(String)
  
- Body (JSON)
- 
                    {
                        "userId": "",
                        "password": ""
                    }

## 3. 정보조회
- 호출 API - [GET] http://localhost:8080/api/users/me

- 호출 시 필수 항목 - Headers  Bearer {access_token}
  
- Headers
- 
                     Authorization: Bearer {access_token}

## 4. 정보수정
- 호출 API - [PUT] http://localhost:8080/api/users/me

- 호출 시 필수 항목 - Headers  Bearer {access_token},
  
  userId(String), password(String), name(String), email(String)
  
- Headers
  
                     Authorization: Bearer {access_token}
- Body (JSON)
  
                    {
                        "userId": "",
                        "password": "",
                        "name": "",
                        "email": ""
                    }

## 5. 회원삭제
- 호출 API - [DELETE] http://localhost:8080/api/users/me
  
- 호출 시 필수 항목 - Headers  Bearer {access_token}, userId(String)
  
- Headers
  
                     Authorization: Bearer {access_token}
- Body (JSON)
  
                    {
                        "userId": ""
                    }

## 6. 네이버 로그인
- 웹 페이지 | 호출 API - http://localhost:8080/oauth2/authorization/naver
- 웹 페이지 로그인 시 access_token 발급됨


## 7. TODO 생성
- 호출 API - [POST] http://localhost:8080/api/todos
 
- 호출 시 필수 항목 - Headers  Bearer {access_token},
  
  title(String), description(String), completed(boolean)
  
- Headers
  
                     Authorization: Bearer {access_token}
- Body (JSON)
  
                    {
                        "title": "",
                        "description": "",
                        "completed": boolean
                    }

## 8. TODO 조회
- 호출 API - [GET] http://localhost:8080/api/todos
  
- 호출 시 필수 항목 - Headers  Bearer {access_token}
  
- Headers
  
                     Authorization: Bearer {access_token}

## 9. TODO ID 조회
- 호출 API - [GET] http://localhost:8080/api/todos/{id}
  
- 호출 시 필수 항목 - Headers  Bearer {access_token}
  
- Headers
  
                     Authorization: Bearer {access_token}


## 10. TODO 수정
- 호출 API - [PUT] http://localhost:8080/api/todos/{id}
  
- 호출 시 필수 항목 - Headers  Bearer {access_token},
  
  title(String), description(String), completed(boolean)

- Headers
  
                     Authorization: Bearer {access_token}
- Body (JSON)
 
                    {
                        "title": "",
                        "description": "",
                        "completed": boolean
                    }

## 11. TODO 삭제
- 호출 API - [DELETE] http://localhost:8080/api/todos/{id}
  
- 호출 시 필수 항목 - Headers  Bearer {access_token}
  
- Headers
  
                     Authorization: Bearer {access_token}


## 12. TODO 검색
- 호출 API - [PUT] http://localhost:8080/api/todos/search?title={제목}
  
- 호출 시 필수 항목 - Headers  Bearer {access_token}, Params ?title={}
  
- Params          : title=검색할 제목(String, required)
- Headers
                     Authorization: Bearer {access_token}

---
---

# API 명세

## 공통

- Request   잘못된 토큰 형식
- Response "토큰이 비어있거나 형식이 잘못되었습니다." 401 Unauthorized

- Request   토큰 시간 만료
- Response "토큰 시간이 소멸되었습니다." 401 Unauthorized


## 사용자관련 API
# signup
- API명           : 회원가입 API
- Method          : POST
- URL             : http://localhost:8080/api/users/signup
- Content-Type    : application/json
- Request Body    :
 
                    {
                        "userId": "TEST_ID_1",
                        "password": "12345",
                        "name": "일번아이디",
                        "email": "TEST@naver.com"
                    }
- Response        :

                    "회원가입 성공"                 200 OK
                    "이미 존재하는 아이디입니다."    400 Bad Request


# login
- API명           : 로그인 API
- Method          : POST
- URL             : http://localhost:8080/api/users/login
- Content-Type    : application/json
- Request Body    :
 
                    {
                        "userId": "TEST_ID_1",
                        "password": "12345"
                    }
- Response        :

                    "access_token": "{access_token}"    200 OK
                    - 헤더 Authorization : {access_token}
                    "존재하지 않는 아이디입니다."         404 Not Found
                    "비밀번호가 일치하지 않습니다."        400 Bad Request


# me
- API명           : 정보조회 API
- Method          : GET
- URL             : http://localhost:8080/api/users/me
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    {
                        "name": "일번아이디",
                        "userId": "TEST_ID_1",
                        "email": "TEST@naver.com"
                    }
  
                    "회원정보가 없습니다."    404 Not Found
                    

# me
- API명           : 정보수정 API
- Method          : PUT
- URL             : http://localhost:8080/api/users/me
- Request Body    :
 
                    {
                        "userId": "TEST_ID_1",
                        "password": "54321",
                        "name": "일번아이디수정",
                        "email": "TEST_UPDATE@naver.com"
                    }
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    "회원정보 수정 성공."     200 OK
                    "회원정보가 없습니다."    404 Not Found


# me
- API명           : 회원삭제 API
- Method          : DELETE
- URL             : http://localhost:8080/api/users/me
- Request Body    :
 
                    {
                        "userId": "TEST_ID_1"
                    }
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    "회원 삭제 성공."         200 OK
                    "회원정보가 없습니다."    404 Not Found

# naver login
- API명           : 네이버 로그인 API
- URL             : http://localhost:8080/oauth2/authorization/naver
- Callback URL    : http://localhost:8080/login/oauth2/code/naver
- Response        :

                    {
                        "access_token":"{access_token}",
                        "message":"네이버 로그인 성공"
                    }


## TODO 관련 API
# todos
- API명           : TODO 생성 API
- Method          : POST
- URL             : http://localhost:8080/api/todos
- Request Body    :
 
                    {
                        "title": "TITLE 일번",
                        "description": "DESCRIPTION 일번",
                        "completed": false
                    }
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    "업로드 성공."        200 OK

# todos
- API명           : TODO 조회 API
- Method          : GET
- URL             : http://localhost:8080/api/todos
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    {
                        "todosIdx": 1,
                        "userIdx": 2,
                        "userId": "TEST_ID_1",
                        "email": "TEST_UPDATE@naver.com"
                        "title": "TITLE 일번",
                        "description": "DESCRIPTION 일번",
                        "completed": false
                    },
                    {
                        "todosIdx": 2,
                        "userIdx": 2,
                        "userId": "TEST_ID_1",
                        "email": "TEST_UPDATE@naver.com"
                        "title": "TITLE 이번",
                        "description": "DESCRIPTION 이번",
                        "completed": true
                    }
                    200 OK
                    "항목이 없습니다."    404 Not Found

# todos
- API명           : TODO ID 조회 API
- Method          : GET
- URL             : http://localhost:8080/api/todos/1
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    {
                        "description": "DESCRIPTION 일번",
                        "completed": "false",
                        "title": "TITLE 일번",
                        "userId": "TEST_ID_1"
                    }
                    200 OK
                    "항목이 없습니다."    404 Not Found

# todos
- API명           : TODO 수정 API
- Method          : PUT
- URL             : http://localhost:8080/api/todos/1
- Request Body    :
 
                    {
                        "title": "TITLE 일번 수정",
                        "description": "DESCRIPTION 일번 수정",
                        "completed": true
                    }
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    "수정 성공"               200 OK
                    "수정할 항목이 없습니다."  400 Bad Request

# todos
- API명           : TODO 삭제 API
- Method          : DELETE
- URL             : http://localhost:8080/api/todos/1
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    "삭제 성공"               200 OK
                    "삭제할 항목이 없습니다."  400 Bad Request

# todos
- API명           : TODO 검색 API
- Method          : GET
- URL             : http://localhost:8080/api/todos/search?title=이번
- Params          : title(String, required)
- Headers         :
 
                    Authorization: Bearer {access_token}
- Response        :

                    {
                        "todosIdx": 2,
                        "userIdx": 2,
                        "userId": "TEST_ID_1",
                        "title": "TITLE 이번",
                        "description": "DESCRIPTION 이번",
                        "completed": false
                    }
                    200 OK
                    "항목이 없습니다."      404 Not Found
