package util;

import db.HttpSessions;
import model.HttpRequest;
import model.HttpSession;

import java.util.UUID;

/**
 * Created by 77loo on 2017-04-01.
 */
public class SessionUtils {
    public static HttpSession getSession(String sessionId) {
        return HttpSessions.findSession(sessionId);
    }

    public static boolean isExistSessionId(String sessionId) {
        return HttpSessions.findSession(sessionId) != null;
    }

    public static HttpSession createNewSession() {
        String newSessionId = UUID.randomUUID().toString();

        while(isExistSessionId(newSessionId)) {
            newSessionId = UUID.randomUUID().toString();
        }

        HttpSession newSession = new HttpSession(newSessionId);
        HttpSessions.addSession(newSession);

        return newSession;
    }
}
