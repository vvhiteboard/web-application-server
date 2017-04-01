package db;

import model.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 77loo on 2017-04-01.
 */
public class HttpSessions {
    private static Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public static HttpSession findSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void addSession(HttpSession session) {
        sessions.put(session.getId(), session);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
