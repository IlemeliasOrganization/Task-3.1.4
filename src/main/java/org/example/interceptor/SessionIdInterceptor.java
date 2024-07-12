package org.example.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.util.List;

public class SessionIdInterceptor implements ClientHttpRequestInterceptor {

    private String sessionId;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (sessionId != null) {
            request.getHeaders().add("Cookie", sessionId);
        }

        ClientHttpResponse response = execution.execute(request, body);

        List<String> cookies = response.getHeaders().get("Set-Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            sessionId = cookies.get(0);
        }

        return response;
    }
}
