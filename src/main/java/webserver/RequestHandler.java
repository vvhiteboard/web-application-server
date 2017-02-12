package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.UserUtils;

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

            if ("/user/create".equals(httpRequest.getPath())) {
                if ("GET".equals(httpRequest.getMethod())) {
                    User user = UserUtils.createUser(httpRequest.getQueryString());
                    log.debug("userId : {}, password : {}, name : {}, email : {}",
                            user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                } else if ("POST".equals(httpRequest.getMethod())) {
                    Map<String, String> bodyParam = HttpRequestUtils.parseQueryString(httpRequest.getBody());
                    User user = UserUtils.createUser(bodyParam);
                    log.debug("userId : {}, password : {}, name : {}, email : {}",
                            user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                }
                response302Header(responseStream, "localhost:8080/index.html");
            }
            else {
                log.debug("body : {}", httpRequest.getBody());
                byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
                response200Header(responseStream, body.length);
                responseBody(responseStream, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://"+ redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
