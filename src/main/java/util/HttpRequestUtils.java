package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.ParseUtils.*;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param request: URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseBySeparator(queryString, "&");
    }

    /**
     * @param request : 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseBySeparator(cookies, ";");
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
}
