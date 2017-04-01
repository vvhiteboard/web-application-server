package model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 77loo on 2017-04-01.
 */
public class HttpSession {
    String id;
    Map<String, Object> attribute = new HashMap<>();

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Map<String, Object> getAttributeAll() {
        return attribute;
    }

    public void setAttributeAll(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void removeAttribute(String key) {
        attribute.remove(key);
    }

    public void clear() {
        attribute.clear();
    }
}
