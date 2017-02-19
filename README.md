# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* InputStream -> InputStreamReader -> BufferedReader로 변환한다.
* BufferedReader에서 request를 읽으며 httpRequest 객체로 파싱한다.
* 파싱한 httpRequest에서 path에 해당하는 파일을 읽어서 response에 전달한다. ( ex : index.html )

### 요구사항 2 - get 방식으로 회원가입
* url로 전달되는 queryString을 path와 분리한다 (split)
* 분리된 queryString을 파싱한다
* 파싱된 데이터에서 User 객체를 생성한다.

### 요구사항 3 - post 방식으로 회원가입
* request body로 전달된 데이터를 헤더의 "Content-Length" 값 만큼 읽는다.
* 읽은 body를 정해진 규칙에 따라 파싱한다.
* 파싱된 body에 해당하는 User 객체를 생성한다.

### 요구사항 4 - redirect 방식으로 이동
* 302 응답 코드 추가 ( response302Header 메서드 )
* Location 옵션을 index.html로 설정

### 요구사항 5 - cookie
* 전체적인 리팩토링 ( 응답하는 부분을 Utils로 추출함 )
* 쿠키 설정하는 응답코드 추가
* HttpResponse 객체와 HttpStatusCode enum형 추가

### 요구사항 6 - 사용자 목록 출력
* DataBase 객체에서 사용자 정보를 가져온다.
* response body로 전송

### 요구사항 7 - stylesheet 적용
* 응답 시 요청한 파일 확장자에 따라 content type을 설정함

### heroku 서버에 배포 후
* 