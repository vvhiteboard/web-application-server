package model;

import org.junit.Test;
import org.mockito.Mock;
import util.HttpRequestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by 77loo on 2017-03-05.
 */
public class HttpRequestTest {
    private String testDirectory="./src/test/resources/";

    @Mock
    HttpRequestUtils httpRequestUtils;

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        assertEquals(request.getMethod(), "GET");
        assertEquals(request.getPath(), "/user/create");
        assertEquals(request.getVersion(), "HTTP/1.1");
    }

    @Test
    public void test() {

    }
}
