package webserver;

import controller.Controller;
import controller.UserListService;
import controller.UserLoginService;
import controller.UserRegisterService;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, Controller> controllerMap = new HashMap<>();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controllerMap.put("/user/create", new UserRegisterService());
        this.controllerMap.put("/user/login", new UserLoginService());
        this.controllerMap.put("/user/list", new UserListService());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);

            log.debug("Method : {}, Path : {}, Version : {}", httpRequest.getMethod(),
                    httpRequest.getPath(), httpRequest.getVersion());

            Map<String, String> headers = httpRequest.getHeader();
            headers.keySet().forEach(key -> log.debug("key : {}, value : {}", key, headers.get(key)));


            Controller controller = controllerMap.get(httpRequest.getPath());
            if (controller != null) {
                controller.service(httpRequest, httpResponse);
            } else {
                String path = httpRequest.getPath();
                HttpResponseUtils.setResource(httpResponse, path);
            }
            httpResponse.setResponseStream();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
