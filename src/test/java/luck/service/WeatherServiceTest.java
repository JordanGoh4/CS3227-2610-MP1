package luck.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import luck.exception.LuckException;
import org.junit.jupiter.api.Test;

/** Tests weather validation and response parsing without live network calls. */
class WeatherServiceTest {
    @Test
    void blankDestination_isRejected() {
        WeatherService service = new WeatherService(new FakeHttpClient());

        assertThrows(LuckException.class, () -> service.getCurrentWeather("  "));
    }

    @Test
    void emptyGeocodingResult_isRejected() {
        WeatherService service = new WeatherService(new FakeHttpClient("{\"results\":[]}"));

        assertThrows(LuckException.class, () -> service.validateDestination("Atlantis"));
    }

    @Test
    void currentWeatherResponse_isParsed() throws Exception {
        FakeHttpClient client = new FakeHttpClient(
                "{\"results\":[{\"latitude\":1.3,\"longitude\":103.8,\"name\":\"Singapore\"}]}",
                "{\"current\":{\"temperature_2m\":30.0,\"weather_code\":1,\"wind_speed_10m\":8.5}}");
        WeatherService service = new WeatherService(client);

        assertEquals("Singapore: 30.0°C, weather code 1, wind 8.5 km/h.",
                service.getCurrentWeather("Singapore"));
    }

    @Test
    void forecastResponse_isParsed() throws Exception {
        FakeHttpClient client = new FakeHttpClient(
                "{\"results\":[{\"latitude\":1.3,\"longitude\":103.8,\"name\":\"Singapore\"}]}",
                "{\"daily\":{\"time\":[\"2026-08-31\"],\"temperature_2m_max\":[31],"
                        + "\"temperature_2m_min\":[26],\"weather_code\":[2]}}");
        WeatherService service = new WeatherService(client);

        assertEquals("5-day forecast for Singapore:" + System.lineSeparator()
                + "\"2026-08-31\": 26°C - 31°C, code 2", service.getForecast("Singapore"));
    }

    private static class FakeHttpClient extends HttpClient {
        private final String[] responses;
        private int responseIndex;

        FakeHttpClient(String... responses) {
            this.responses = responses;
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler)
                throws IOException {
            String body = responses[responseIndex++];
            return new FakeHttpResponse<>((T) body);
        }

        @Override
        public <T> java.util.concurrent.CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request, HttpResponse.BodyHandler<T> handler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> java.util.concurrent.CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request, HttpResponse.BodyHandler<T> handler,
                HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Optional<java.net.CookieHandler> cookieHandler() {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<java.time.Duration> connectTimeout() {
            return java.util.Optional.empty();
        }

        @Override
        public java.net.http.HttpClient.Redirect followRedirects() {
            return java.net.http.HttpClient.Redirect.NEVER;
        }

        @Override
        public java.util.Optional<java.net.ProxySelector> proxy() {
            return java.util.Optional.empty();
        }

        @Override
        public javax.net.ssl.SSLContext sslContext() {
            return null;
        }

        @Override
        public javax.net.ssl.SSLParameters sslParameters() {
            return new javax.net.ssl.SSLParameters();
        }

        @Override
        public java.util.Optional<java.net.Authenticator> authenticator() {
            return java.util.Optional.empty();
        }

        @Override
        public java.net.http.HttpClient.Version version() {
            return java.net.http.HttpClient.Version.HTTP_1_1;
        }

        @Override
        public java.util.Optional<java.util.concurrent.Executor> executor() {
            return java.util.Optional.empty();
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
        public java.util.Optional<HttpResponse<T>> previousResponse() {
            return java.util.Optional.empty();
        }

        @Override
        public java.net.http.HttpHeaders headers() {
            return java.net.http.HttpHeaders.of(java.util.Map.of(), (a, b) -> true);
        }

        @Override
        public java.net.URI uri() {
            return java.net.URI.create("http://localhost");
        }

        @Override
        public java.net.http.HttpClient.Version version() {
            return java.net.http.HttpClient.Version.HTTP_1_1;
        }

        @Override
        public java.util.Optional<javax.net.ssl.SSLSession> sslSession() {
            return java.util.Optional.empty();
        }
    }
}
