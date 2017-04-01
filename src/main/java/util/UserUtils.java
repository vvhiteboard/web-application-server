package util;

import model.HttpSession;
import model.User;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by 77loo on 2017-02-05.
 */
public class UserUtils {
    public static User createUser(Map<String, String> param) {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        return user;
    }

    public static boolean isLogout(String sessionId) {
        HttpSession session = SessionUtils.getSession(sessionId);
        return session == null || session.getAttribute("user") == null;
    }

    public static Map<String, String> parseLoginParams(String body) {
        return ParseUtils.parseBySeparator(body, "&");
    }
}
