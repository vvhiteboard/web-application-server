package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatusCode;
import model.User;
import org.apache.commons.lang.StringUtils;
import util.HttpResponseUtils;
import util.UserUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by 77loo on 2017-03-05.
 */
public class UserListService extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (UserUtils.isLogout(request.getCookies())) {
            HttpResponseUtils.setRedirect(response, "/user/login.html");
            return;
        }
        userListing(response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (UserUtils.isLogout(request.getCookies())) {
            HttpResponseUtils.setRedirect(response, "/user/login.html");
            return;
        }
        userListing(response);
    }

    private void userListing(HttpResponse response) {
        Collection<User> users = DataBase.findAll();
        StringBuilder responseBuilder = new StringBuilder("");
        responseBuilder.append("<table border='1'>");
        for (User user : users) {
            responseBuilder.append("<tr>");
            responseBuilder.append("<td>").append(user.getUserId()).append("</td>");
            responseBuilder.append("<td>").append(user.getName()).append("</td>");
            responseBuilder.append("<td>").append(user.getEmail()).append("</td>");
            responseBuilder.append("</tr>");
        }
        responseBuilder.append("</table>");
        byte[] body = responseBuilder.toString().getBytes();
        response.setStatusCode(HttpStatusCode.OK);
        response.setBody(body);
    }
}
