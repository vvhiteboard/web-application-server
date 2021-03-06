package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import util.HttpResponseUtils;
import util.UserUtils;

import java.util.Map;

/**
 * Created by 77loo on 2017-03-05.
 */
public class UserRegisterService extends AbstractController {
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> userParametars = UserUtils.parseLoginParams(request.getBody());
        register(userParametars);
        response.setRedirect("/index.html");
    }

    private void register(Map<String, String> parameters) {
        User user = UserUtils.createUser(parameters);
        DataBase.addUser(user);
    }
}
