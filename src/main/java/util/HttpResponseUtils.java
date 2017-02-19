package util;

import model.HttpResponse;
import model.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by loopin on 2017-02-20.
 */
public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static HttpResponse createHttpResponse(HttpStatusCode code) {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(code);
        switch (code) {
            case OK :
                response.setHeader("Contents-Type", "text/html; charset=utf-8");
                break;
            case FOUND:
                response.setHeader("Location", "http://localhost:8080/index.html");
                break;
        }
        return response;
    }

    public static void setResponseStream(DataOutputStream responseStream, HttpResponse response) {
        try {
            byte[] body = response.getBody();
            responseStream.writeBytes(response.toString());
            responseStream.writeBytes("\r\n");
            responseStream.write(body, 0, body.length);
            responseStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void setResponseBody(HttpResponse response, byte[] body) {
        response.setBody(body);
        response.setHeader("Content-Length", Integer.toString(body.length));
    }
}
