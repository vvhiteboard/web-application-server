package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import org.apache.commons.lang.StringUtils;
import util.HttpResponseUtils;
import util.UserUtils;

import java.util.Map;

/**
 * Created by 77loo on 2017-03-05.
 */
public class UserLoginService extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setCookie("logined", "false");
        HttpResponseUtils.setRedirect(response, "/user/login_failed.html");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> userParameters = UserUtils.parseLoginParams(request.getBody());
        User user = UserUtils.createUser(userParameters);

        if(isValidUser(user)) {
            response.setCookie("logined", "true");
            HttpResponseUtils.setRedirect(response, "/index.html");
        }
        else {
            response.setCookie("logined", "false");
            HttpResponseUtils.setRedirect(response, "/user/login_failed.html");
        }
    }

    private boolean isValidUser(User user) {
        User findedUser = DataBase.findUserById(user.getUserId());

        if (findedUser == null) {
            return false;
        }

        return (StringUtils.equals(findedUser.getUserId(), user.getUserId())) && (StringUtils.equals(findedUser.getPassword(), user.getPassword()));
    }
}
