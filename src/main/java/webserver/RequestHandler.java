package webserver;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatusCode;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.UserUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader requestReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream responseStream = new DataOutputStream(out);

            HttpRequest httpRequest = HttpRequestUtils.parseHttpRequest(requestReader);
            log.debug("Method : {}, Path : {}, Version : {}", httpRequest.getMethod(),
                    httpRequest.getPath(), httpRequest.getVersion());

            Map<String, String> headers = httpRequest.getHeader();
            headers.keySet().forEach(key -> log.debug("key : {}, value : {}", key, headers.get(key)));

            // 회원가입
            if ("/user/create".equals(httpRequest.getPath())) {
                if ("GET".equals(httpRequest.getMethod())) {
                    User user = UserUtils.createUser(httpRequest.getQueryString());
                    log.debug("userId : {}, password : {}, name : {}, email : {}",
                            user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                    DataBase.addUser(user);
                } else if ("POST".equals(httpRequest.getMethod())) {
                    Map<String, String> bodyParam = HttpRequestUtils.parseQueryString(httpRequest.getBody());
                    User user = UserUtils.createUser(bodyParam);
                    log.debug("userId : {}, password : {}, name : {}, email : {}",
                            user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                    DataBase.addUser(user);
                }
                HttpResponse response = HttpResponseUtils.createHttpResponse(HttpStatusCode.FOUND);
                response.setHeader("Location", "/index.html");
                HttpResponseUtils.setResponseStream(responseStream, response);
            } // 로그인
            else if ("/user/login".equals(httpRequest.getPath())) {
                Map<String, String> bodyParam = HttpRequestUtils.parseQueryString(httpRequest.getBody());
                User user = UserUtils.createUser(bodyParam);
                User findedUser = DataBase.findUserById(user.getUserId());

                HttpResponse response = HttpResponseUtils.createHttpResponse(HttpStatusCode.FOUND);
                //TODO : StringUtils 작동이 안됨...
                //StringUtils(findedUser.getPassword(), user.getPassword())
                if (findedUser != null && findedUser.getPassword().equals(user.getPassword())) {
                    response.setCookie("logined", "true");
                    response.setHeader("Location", "/index.html");
                } else {
                    response.setCookie("logined", "false");
                    response.setHeader("Location", "/user/login_failed.html");
                }
                HttpResponseUtils.setResponseStream(responseStream, response);
            }
            else if ("/user/list".equals(httpRequest.getPath())) {
                if ( "true".equals(httpRequest.getCookie("logined"))) {
                    // 요구사항 6
                }

            }
            else {
                log.debug("body : {}", httpRequest.getBody());
                byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());

                HttpResponse response = HttpResponseUtils.createHttpResponse(HttpStatusCode.OK);
                HttpResponseUtils.setResponseBody(response, body);
                HttpResponseUtils.setResponseStream(responseStream, response);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
