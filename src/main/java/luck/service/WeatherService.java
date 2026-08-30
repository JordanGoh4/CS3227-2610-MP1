package luck.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import luck.exception.LuckException;

/** Retrieves current and forecast weather information for named destinations. */
public class WeatherService {
    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String NUMBER_PATTERN = "\\\"%s\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)";
    private static final String STRING_PATTERN = "\\\"%s\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"";
    private static final String ARRAY_PATTERN = "\\\"%s\\\"\\s*:\\s*\\[([^]]+)]";
    private final HttpClient httpClient;

    /** Creates a weather service using the default HTTP client. */
    public WeatherService() {
        httpClient = HttpClient.newHttpClient();
    }

    /** Looks up a destination and returns its current weather summary. */
    public String getCurrentWeather(String destination) throws LuckException {
        if (destination == null || destination.isBlank()) {
            throw new LuckException("Going to Narnia?");
        }
        try {
            String locationJson = get(GEOCODING_URL + "?name="
                    + URLEncoder.encode(destination.trim(), StandardCharsets.UTF_8)
                    + "&count=1&language=en&format=json");
            double latitude = number(locationJson, "latitude");
            double longitude = number(locationJson, "longitude");
            String locationName = string(locationJson, "name");
            String weatherJson = get(FORECAST_URL + "?latitude=" + latitude + "&longitude=" + longitude
                    + "&current=temperature_2m,weather_code,wind_speed_10m&temperature_unit=celsius");
            return locationName + ": " + number(weatherJson, "temperature_2m") + "°C, weather code "
                    + (int) number(weatherJson, "weather_code") + ", wind "
                    + number(weatherJson, "wind_speed_10m") + " km/h.";
        } catch (IOException | InterruptedException | IllegalArgumentException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new LuckException("Unable to retrieve weather right now.");
        }
    }

    /** Returns a five-day daily forecast for a destination. */
    public String getForecast(String destination) throws LuckException {
        if (destination == null || destination.isBlank()) {
            throw new LuckException("Please provide a destination.");
        }
        try {
            String locationJson = get(GEOCODING_URL + "?name="
                    + URLEncoder.encode(destination.trim(), StandardCharsets.UTF_8)
                    + "&count=1&language=en&format=json");
            double latitude = number(locationJson, "latitude");
            double longitude = number(locationJson, "longitude");
            String locationName = string(locationJson, "name");
            String forecastJson = get(FORECAST_URL + "?latitude=" + latitude + "&longitude=" + longitude
                    + "&daily=weather_code,temperature_2m_max,temperature_2m_min"
                    + "&forecast_days=5&temperature_unit=celsius");
            String[] dates = values(forecastJson, "time");
            String[] highs = values(forecastJson, "temperature_2m_max");
            String[] lows = values(forecastJson, "temperature_2m_min");
            String[] codes = values(forecastJson, "weather_code");
            StringBuilder result = new StringBuilder("5-day forecast for ").append(locationName).append(":");
            for (int i = 0; i < dates.length; i++) {
                result.append(System.lineSeparator()).append(dates[i]).append(": ")
                        .append(lows[i]).append("°C - ").append(highs[i]).append("°C, code ")
                        .append((int) Double.parseDouble(codes[i]));
            }
            return result.toString();
        } catch (IOException | InterruptedException | IllegalArgumentException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new LuckException("Unable to retrieve the forecast right now.");
        }
    }

    /** Sends a GET request and validates the response. */
    private String get(String url) throws IOException, InterruptedException, LuckException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new LuckException("The weather service returned an error.");
        }
        return response.body();
    }

    /** Extracts a numeric JSON field. */
    private double number(String json, String field) throws LuckException {
        Matcher matcher = Pattern.compile(String.format(NUMBER_PATTERN, field)).matcher(json);
        if (!matcher.find()) {
            throw new LuckException("That destination could not be found.");
        }
        return Double.parseDouble(matcher.group(1));
    }

    /** Extracts a string JSON field. */
    private String string(String json, String field) throws LuckException {
        Matcher matcher = Pattern.compile(String.format(STRING_PATTERN, field)).matcher(json);
        if (!matcher.find()) {
            throw new LuckException("That destination could not be found.");
        }
        return matcher.group(1);
    }

    /** Extracts values from a simple JSON array field. */
    private String[] values(String json, String field) throws LuckException {
        Matcher matcher = Pattern.compile(String.format(ARRAY_PATTERN, field)).matcher(json);
        if (!matcher.find()) {
            throw new LuckException("The forecast response was incomplete.");
        }
        return matcher.group(1).replace("\\\"", "").split("\\s*,\\s*");
    }
}
