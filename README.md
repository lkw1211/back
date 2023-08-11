# Boardgaming
온라인 보드게임 플랫폼
<br>
<br>

## 진행상황
- [x] 오목
- [ ] 체스
- [ ] 바둑
- [ ] 장기
<br>

## 실행환경
![jdk](https://img.shields.io/badge/openjdk-17.0.7-brightgreen.svg)
<br>
<br>

## 실행방법
### 1. Git 다운로드
```c
git clone https://github.com/lkw1211/boardgaming-back.git
```
<br>

### 2. security 설정파일 생성

** 표시된 부분 알맞게 변경 후 파일 생성

#### api/src/resources/application-secret.yml

```c
spring:
  config:
    activate:
      on-profile: "dev"

properties:
  server:
    allowedOrigins:
      http://localhost:3000
  data:
    mysql:
      host: **ip 또는 url**
      port: **포트**
      database: **데이터베이스명**
      username: **유저명**
      password: **비밀번호**
    redis:
      host: **ip 또는 url**
      port: **포트**
  cookie:
    cookieDomain: 
  jwt:
    secret-key: **비밀 키**
  mail:
    host: **메일서버 smtp 주소**
    port: **포트**
    username: **유저이름**
    password: **비밀번호**
```

#### file/src/resources/application-secret.yml

```c
spring:
  config:
    activate:
      on-profile: "dev"

properties:
  server:
    allowedOrigins:
      http://localhost:3000
  data:
    mysql:
      host: **ip 또는 url**
      port: **포트**
      database: **데이터베이스명**
      username: **유저명**
      password: **비밀번호**
    redis:
      host: **ip 또는 url**
      port: **포트**
  cookie:
    cookieDomain:
  jwt:
    secret-key: **비밀 키**
  file:
    root: file/src/main/resources/static
```

#### gomoku_ws/src/resources/application-secret.yml

```c
spring:
  config:
    activate:
      on-profile: "dev"

properties:
  server:
    allowedOrigins:
      http://localhost:3000
  data:
    mysql:
      host: **ip 또는 url**
      port: **포트**
      database: **데이터베이스명**
      username: **유저명**
      password: **비밀번호**
    redis:
      host: **ip 또는 url**
      port: **포트**
  cookie:
    cookieDomain:
  jwt:
    secret-key: **비밀 키**
```
<br>

### 3. 빌드

프로젝트 루트에서

```c
./gradlew clean build
```
<br>

### 4. 실행

#### 1) 기본 api서버 실행
```c
java -jar ./api/build/libs/api-0.0.1-SNAPSHOT.jar
```

#### 2) 파일 서버 실행
```c
java -jar ./file/build/libs/file-0.0.1-SNAPSHOT.jar
```

#### 3) 오목 api, ws서버 실행
```c
java -jar ./gomoku_ws/build/libs/gomoku_ws-0.0.1-SNAPSHOT.jar
```

