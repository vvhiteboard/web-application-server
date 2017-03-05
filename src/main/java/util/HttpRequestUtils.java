package util;

import model.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param request: URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static void parseQueryString(HttpRequest request) {
        request.setParameters(ParseUtils.parseBySeparator(request.getQueryString(), "&"));
    }

    /**
     * @param request : 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static void parseCookies(HttpRequest request) {
        String cookies = request.getHeaderValueByKey("Cookie");
        request.setCookies(ParseUtils.parseBySeparator(cookies, ";"));
    }

    public static HttpRequest parseHttpRequest(HttpRequest httpRequest, BufferedReader requestReader) throws IOException {

        parseRequestLine(httpRequest, requestReader);
        parseRequestHeader(httpRequest, requestReader);
        parseCookies(httpRequest);

        if (StringUtils.equals(httpRequest.getMethod(), "GET")) {
            parseQueryString(httpRequest);
        }
        else if (StringUtils.equals(httpRequest.getMethod(), "POST")) {
            parseRequestBody(httpRequest, requestReader);
        }

        return httpRequest;
    }

    // 만약 첫 라인을 동일하게 Header로 설정하면 Header 마라미터로 method나 version이라는 값을
    // 제대로 파싱하지 못할 수도 있다
    public static void parseRequestLine(HttpRequest request, BufferedReader requestStream) throws IOException {
        String[] requestLineValues = requestStream.readLine().split(" ");

        request.setMethod(requestLineValues[0]);
        request.setVersion(requestLineValues[2]);

        String[] requestUrl = requestLineValues[1].split("\\?");
        request.setPath(requestUrl[0]);
        if (requestUrl.length > 1) {
            request.setQueryString(requestUrl[1]);
        }
    }

    public static void parseRequestHeader(HttpRequest request, BufferedReader requestStream) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String requestHeaderLine;

        while ((requestHeaderLine = requestStream.readLine()) != null && !requestHeaderLine.isEmpty()) {
            ParseUtils.Pair keyValue = ParseUtils.getKeyValue(requestHeaderLine, ": ");
            if (keyValue == null) {
                log.debug("invalid Http Header : {}\r\n", requestHeaderLine);
                continue;
            }
            requestHeader.put(keyValue.getKey(), keyValue.getValue());
        }
        request.setHeader(requestHeader);
    }

    public static void parseRequestBody(HttpRequest request, BufferedReader requestStream) throws IOException {
        int bodySize = Integer.parseInt(request.getHeaderValueByKey("Content-Length"));
        char[] bodyStream = new char[bodySize];
        requestStream.read(bodyStream, 0, bodySize);
        request.setBody(new String(bodyStream));
    }
}
