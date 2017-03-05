package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import org.apache.commons.lang.StringUtils;
import util.HttpRequestUtils;
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
        Map<String, String> bodyParam = HttpRequestUtils.parseQueryString(request.getBodyContents());
        User user = UserUtils.createUser(bodyParam);
        User findedUser = DataBase.findUserById(user.getUserId());

        if(StringUtils.equals(findedUser.getUserId(), user.getUserId()) && StringUtils.equals(findedUser.getPassword(), user.getPassword())){
            response.setCookie("logined", "true");
            HttpResponseUtils.setRedirect(response, "/index.html");
        }
        else {
            response.setCookie("logined", "false");
            HttpResponseUtils.setRedirect(response, "/user/login_failed.html");
        }
    }
}
