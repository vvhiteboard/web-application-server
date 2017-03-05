package controller;

import model.HttpRequest;
import model.HttpResponse;

/**
 * Created by 77loo on 2017-03-05.
 */
public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
