package util;

import model.User;

import java.util.Map;

/**
 * Created by 77loo on 2017-02-05.
 */
public class UserUtils {
    public static User createUser(Map<String, String> param) {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        return user;
    }
}
