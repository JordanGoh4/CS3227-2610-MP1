package luck.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import luck.exception.LuckException;

/** Converts amounts between currencies using the latest available rate. */
public class CurrencyService {
    private static final String CONVERSION_PATTERN = "(?i)\\s*([0-9]+(?:\\.[0-9]+)?)\\s+([A-Z]{3})\\s+to\\s+([A-Z]{3})\\s*";
    private static final String RATE_PATTERN = "\\\"rate\\\"\\s*:\\s*([0-9.]+)";
    private final HttpClient httpClient;

    /** Creates a currency service using the default HTTP client. */
    public CurrencyService() {
        this(HttpClient.newHttpClient());
    }

    /** Creates a currency service using the supplied HTTP client. */
    CurrencyService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /** Converts an amount from one ISO currency code to another. */
    public String convert(String input) throws LuckException {
        Matcher matcher = Pattern.compile(CONVERSION_PATTERN).matcher(input == null ? "" : input);
        if (!matcher.matches()) {
            throw new LuckException("Use: currency <amount> <FROM> to <TO>, e.g. currency 100 USD to JPY.");
        }
        BigDecimal amount = new BigDecimal(matcher.group(1));
        String from = matcher.group(2).toUpperCase();
        String to = matcher.group(3).toUpperCase();
        try {
            URI uri = URI.create("https://api.frankfurter.dev/v2/rate/" + from + "/" + to);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new LuckException("That currency pair is not available.");
            }
            Matcher rateMatcher = Pattern.compile(RATE_PATTERN).matcher(response.body());
            if (!rateMatcher.find()) {
                throw new LuckException("The currency response was incomplete.");
            }
            BigDecimal result = amount.multiply(new BigDecimal(rateMatcher.group(1)))
                    .setScale(2, RoundingMode.HALF_UP);
            return amount + " " + from + " = " + result + " " + to;
        } catch (IOException exception) {
            throw new LuckException("Unable to retrieve exchange rates right now.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new LuckException("The currency request was interrupted.");
        }
    }
}
