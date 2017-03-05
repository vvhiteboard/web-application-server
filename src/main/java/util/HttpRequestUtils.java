package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param queryString : URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies : 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        // Lambda 표현식
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static HttpRequest parseHttpRequest(HttpRequest httpRequest, BufferedReader requestReader) throws IOException {

        httpRequest.setRequestLine(parseRequestLine(requestReader));
        httpRequest.setHeader(parseRequestHeader(requestReader));
        httpRequest.setCookies(parseCookies(httpRequest.getHeaderValueByKey("Cookie")));

        if (StringUtils.equals(httpRequest.getMethod(), "GET")) {
            httpRequest.setParameters(parseQueryString(httpRequest.getRequestLine().get("queryString")));
        }
        else if ("POST".equals(httpRequest.getMethod())) {
            httpRequest.setBodyContents(parseRequestBody(requestReader, httpRequest.getHeaderValueByKey("Content-Length")));
            httpRequest.setParameters(parseQueryString(httpRequest.getBodyContents()));
        }

        return httpRequest;
    }

    // 만약 첫 라인을 동일하게 Header로 설정하면 Header 마라미터로 method나 version이라는 값을
    // 제대로 파싱하지 못할 수도 있다
    public static Map<String, String> parseRequestLine(BufferedReader requestStream) throws IOException {
        Map<String, String> requestLine = new HashMap<>();
        String[] requestLineValues = requestStream.readLine().split(" ");

        requestLine.put("method", requestLineValues[0]);
        requestLine.put("version", requestLineValues[2]);
        String[] requestURLs = requestLineValues[1].split("\\?");
        requestLine.put("path", requestURLs[0]);
        if (requestURLs.length > 1) {
            requestLine.put("queryString", requestURLs[1]);
        }
        return requestLine;
    }

    public static Map<String, String> parseRequestHeader(BufferedReader requestStream) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String requestHeaderLine;

        while ((requestHeaderLine = requestStream.readLine()) != null && !requestHeaderLine.isEmpty()) {
            Pair keyValue = getKeyValue(requestHeaderLine, ": ");
            if (keyValue == null) {
                log.debug("invalid Http Header : {}\r\n", requestHeaderLine);
                continue;
            }
            requestHeader.put(keyValue.getKey(), keyValue.getValue());
        }

        return requestHeader;
    }

    public static String parseRequestBody(BufferedReader requestStream, String contentLength) throws IOException {
        int bodySize = Integer.parseInt(contentLength);
        char[] bodyStream = new char[bodySize];
        requestStream.read(bodyStream, 0, bodySize);

        return new String(bodyStream);
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
