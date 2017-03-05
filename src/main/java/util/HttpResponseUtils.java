package util;

import model.HttpResponse;
import model.HttpStatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by loopin on 2017-02-20.
 */
public class HttpResponseUtils {
    public static void setRedirect(HttpResponse response, String location) {
        response.setStatusCode(HttpStatusCode.FOUND);
        response.setHeader("Location", location);
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
