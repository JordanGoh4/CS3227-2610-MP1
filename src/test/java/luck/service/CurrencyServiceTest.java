package luck.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import luck.exception.LuckException;
import org.junit.jupiter.api.Test;

/** Tests currency command validation independently of the external API. */
class CurrencyServiceTest {
    private final CurrencyService service = new CurrencyService();

    @Test
    void invalidAmount_isRejected() {
        assertThrows(LuckException.class, () -> service.convert("abc USD to JPY"));
    }

    @Test
    void missingConversionSeparator_isRejected() {
        assertThrows(LuckException.class, () -> service.convert("100 USD JPY"));
    }

    @Test
    void nullInput_isRejected() {
        assertThrows(LuckException.class, () -> new CurrencyService().convert(null));
    }

    @Test
    void validConversionResponse_isFormattedCorrectly() throws Exception {
        CurrencyService service = new CurrencyService(new FakeHttpClient("{\"rate\":1.35}"));

        assertEquals("100 USD = 135.00 JPY", service.convert("100 USD to JPY"));
    }

    /** Provides a deterministic exchange-rate response for service tests. */
    private static class FakeHttpClient extends HttpClient {
        private final String response;

        FakeHttpClient(String response) {
            this.response = response;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler)
                throws IOException {
            return new FakeHttpResponse<>((T) response);
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request, HttpResponse.BodyHandler<T> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request, HttpResponse.BodyHandler<T> handler,
                HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return Redirect.NEVER;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return new SSLParameters();
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return Version.HTTP_1_1;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }
    }

    private record FakeHttpResponse<T>(T body) implements HttpResponse<T> {
        @Override
        public int statusCode() {
            return 200;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (name, value) -> true);
        }

        @Override
        public URI uri() {
            return URI.create("http://localhost");
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }
    }
}
