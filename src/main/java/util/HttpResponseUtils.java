package util;

import model.HttpResponse;
import model.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by loopin on 2017-02-20.
 */
public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void setRedirect(HttpResponse response, String location) {
        response.setStatusCode(HttpStatusCode.FOUND);
        response.setHeader("Location", location);
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

    public static void setResource(HttpResponse response, String path) throws IOException {
        String contentType;
        if (path.endsWith(".css")) {
            contentType = "text/css; charset=utf-8";
        } else {
            contentType = "text/html; charset=utf-8";
        }

        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

        response.setStatusCode(HttpStatusCode.OK);
        response.setHeader("Content-Type", contentType);
        response.setBody(body);
    }
}
