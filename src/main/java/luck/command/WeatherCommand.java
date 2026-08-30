package luck.command;

import luck.exception.LuckException;
import luck.service.WeatherService;

/** Handles weather requests for a destination. */
public class WeatherCommand implements Command {
    private final CommandContext context;
    private final WeatherService weatherService;

    /** Creates a weather command with the shared command context. */
    public WeatherCommand(CommandContext context) {
        this.context = context;
        weatherService = new WeatherService();
    }

    /** Retrieves and displays current weather or a forecast for the destination. */
    @Override
    public void execute(String arguments) throws LuckException {
        String input = arguments.trim();
        boolean forecast = input.toLowerCase().endsWith(" forecast");
        String destination = forecast
                ? input.substring(0, input.length() - " forecast".length()).trim() : input;
        context.getUi().printMessage(forecast
                ? weatherService.getForecast(destination)
                : weatherService.getCurrentWeather(destination));
    }
}
