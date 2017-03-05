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
    private String method;
    private String version;
    private String path;
    private String queryString;

    private Map<String, String> header;
    private String body;

    private Map<String, String> parameters;
    private Map<String, String> cookies;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        HttpRequestUtils.parseHttpRequest(this, requestReader);
    }

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

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
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
