package util;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param queryString은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키 값은 name1=value1; name2=value2 형식임
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

    public static HttpRequest parseHttpRequest(InputStream in) throws IOException {
        BufferedReader requestStream = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        HttpRequest httpRequest = new HttpRequest();

        httpRequest.setRequestLine(parseRequestLine(requestStream));
        httpRequest.setHeader(parseRequestHeader(requestStream));
        // TODO : POST가 지원되면 parseRequestBody도 작성해야함

        return httpRequest;
    }

    public static Map<String, String> parseRequestLine(BufferedReader requestStream) throws IOException {
        Map<String, String> requestLine = new HashMap<>();
        String[] requestLineValues = requestStream.readLine().split(" ");

        requestLine.put("method", requestLineValues[0]);
        requestLine.put("path", requestLineValues[1]);
        requestLine.put("version", requestLineValues[2]);
        return requestLine;
    }

    public static Map<String, String> parseRequestHeader(BufferedReader requestStream) throws IOException {
        // parse http requestHeader first line ( request line )
        Map<String, String> requestHeader = new HashMap<>();

        String requestHeaderLine = requestStream.readLine();
        while (requestHeaderLine != null && !requestHeaderLine.isEmpty()) {
            Pair keyValue = getKeyValue(requestHeaderLine, ": ");
            if (keyValue == null) {
                log.debug("invalid Http Header : {}\r\n", requestHeaderLine);
                requestHeaderLine = requestStream.readLine();
                continue;
            }
            requestHeader.put(keyValue.getKey(), keyValue.getValue());
            requestHeaderLine = requestStream.readLine();
        }
        return requestHeader;
    }

    public static Pair parseRequestHeader(String header) {
        return getKeyValue(header, ": ");
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
