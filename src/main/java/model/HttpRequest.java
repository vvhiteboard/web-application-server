package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loopin on 2017-01-30.
 */
public class HttpRequest {
    private String method;
    private String version;
    private String path;

    private Map<String, String> header;
    private Map<String, String> body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public void setHeaderKeyValue(String key, String value) {
        if (this.header == null) {
            this.header = new HashMap<>();
        }
        this.header.put(key, value);

    }

    public String getHeaderValueByKey(String key) {
        if (this.header == null) {
            return null;
        }
        return this.header.get(key);
    }

    public void setBodyKeyValue(String key, String value) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        this.body.put(key, value);
    }

    public String getBodyValueByKey(String key) {
        if (this.body == null) {
            return null;
        }
        return this.body.get(key);
    }
}
