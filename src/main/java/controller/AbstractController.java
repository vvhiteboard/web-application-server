package controller;

import model.HttpRequest;
import model.HttpResponse;
import org.apache.commons.lang.StringUtils;
import util.HttpResponseUtils;

/**
 * Created by 77loo on 2017-03-05.
 */
public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (StringUtils.equals(httpRequest.getMethod(), "GET")) {
            doGet(httpRequest, httpResponse);
        }
        else if ( StringUtils.equals(httpRequest.getMethod(), "POST")) {
            doPost(httpRequest, httpResponse);
        }
        else {
            httpResponse.setRedirect("/index.html");
        }
    }
    public void doGet(HttpRequest request, HttpResponse response) {}
    public void doPost(HttpRequest request, HttpResponse response) {}
}
