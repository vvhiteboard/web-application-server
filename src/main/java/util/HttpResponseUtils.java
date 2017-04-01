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
}
