package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loopin on 2017-01-30.
 */
public class HttpRequest {
    private Map<String, String> requestLine;
    private Map<String, String> header;
    private String body;

    private Map<String, String> queryString;

    public String getMethod() {
        return requestLine.get("method");
    }

    public void setMethod(String method) {
        this.requestLine.put("method", method);
    }

    public String getVersion() {
        return this.requestLine.get("version");
    }

    public void setVersion(String version) {
        this.requestLine.put("version", version);
    }

    public String getPath() {
        return this.requestLine.get("path");
    }

    public void setPath(String path) {
        this.requestLine.put("path", path);
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(Map<String, String> requestLine) {
        this.requestLine = requestLine;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public void setQueryString(Map<String, String> queryString) {
        this.queryString = queryString;
    }
}
