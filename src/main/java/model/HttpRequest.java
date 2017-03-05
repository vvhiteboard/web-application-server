package model;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loopin on 2017-01-30.
 */
public class HttpRequest {
    private Map<String, String> requestLine;
    private Map<String, String> header;
    private String bodyContents;

    private Map<String, String> parameters;
    private Map<String, String> cookies;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        HttpRequestUtils.parseHttpRequest(this, requestReader);
    }

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

    public String getBodyContents() {
        return bodyContents;
    }

    public void setBodyContents(String bodyContents) {
        this.bodyContents = bodyContents;
    }

    public Map<String, String> getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(Map<String, String> requestLine) {
        this.requestLine = requestLine;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
