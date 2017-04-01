package model;

import com.google.common.collect.Maps;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by loopin on 2017-02-19.
 */
public class HttpResponse {
    private String version = "HTTP/1.1";
    private HttpStatusCode statusCode;

    private Map<String, String> headers;
    private Map<String, String> cookies;

    private byte[] body;
    private DataOutputStream stream;

    public HttpResponse() {
        headers = Maps.newHashMap();
        cookies = Maps.newHashMap();
    }

    public HttpResponse(OutputStream out) {
        this();
        this.stream = new DataOutputStream(out);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeaderValue(String key) {
        return headers.get(key);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getCookieValue(String key) {
        return cookies.get(key);
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void setCookie(String key, String value) {
        this.cookies.put(key, value);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
        this.headers.put("Content-Length", Integer.toString(this.body.length));
    }

    public void setResource(String path) throws IOException {
        String contentType;
        if (path.endsWith(".css")) {
            contentType = "text/css; charset=utf-8";
        } else {
            contentType = "text/html; charset=utf-8";
        }

        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        this.statusCode = HttpStatusCode.OK;
        this.headers.put("Content-Type", contentType);
        this.setBody(body);
    }

    public void setRedirect(String location) {
        this.statusCode = HttpStatusCode.FOUND;
        this.headers.put("Location", location);
    }

    public void setSessionId(String sessionId) {
        this.cookies.put("JSESSIONID", sessionId);
    }

    public void setResponseStream() throws IOException {
        byte[] body = this.body;
        this.stream.writeBytes(this.toString());
        this.stream.writeBytes("\r\n");
        this.stream.write(body, 0, body.length);
        this.stream.flush();
    }

    private String createHeaderStream() {
        StringBuilder stream = new StringBuilder("");

        Iterator<String> keys = this.headers.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            stream.append(key).append(": ").append(this.headers.get(key)).append("\r\n");
        }
        return stream.toString();
    }

    private String createCookieStream() {
        StringBuilder stream = new StringBuilder("");

        Iterator<String> keys = this.cookies.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            stream.append(key).append("=").append(this.cookies.get(key)).append("; ");
        }

        int startIndex = stream.lastIndexOf(";");
        stream.delete(startIndex, startIndex + 2);
        stream.append("\r\n");

        return stream.toString();
    }

    //TODO : toString으로 응답 스트림 만들기
    @Override
    public String toString() {
        StringBuilder responseStream = new StringBuilder();

        responseStream.append(version).append(" ");
        responseStream.append(statusCode.toString()).append("\r\n");

        if (!this.headers.isEmpty()) {
            responseStream.append(createHeaderStream());
        }

        if (!this.cookies.isEmpty()) {
            responseStream.append("Set-Cookie: ").append(createCookieStream());
        }

        return responseStream.toString();
    }
}
