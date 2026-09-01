package luck.gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import luck.exception.LuckException;
import luck.service.WeatherService;

/** Displays weather information for the currently selected destination. */
public class WeatherPanel extends VBox {
    private final WeatherService weatherService;
    private final Label weatherResult = new Label("No weather request yet.");

    /** Creates a weather panel backed by the supplied weather service. */
    public WeatherPanel(WeatherService weatherService) {
        this.weatherService = weatherService;
        Label heading = new Label("Weather");
        Label description = new Label("Latest weather result:");
        Label example = new Label("Try it in Chat: weather Tokyo");
        weatherResult.setWrapText(true);
        getChildren().addAll(heading, description, weatherResult, example);
        setSpacing(12);
        GuiStyles.stylePanel(this);
    }

    /** Displays a message received from a weather command. */
    public void setResult(String result) {
        weatherResult.setText(result);
    }

    /** Refreshes the weather display for a destination in the background. */
    public void refresh(String destination) {
        if (destination.isBlank()) {
            setResult("Add a destination to view its weather.");
            return;
        }
        setResult("Loading weather for " + destination + "...");
        Thread weatherThread = new Thread(() -> loadWeather(destination));
        weatherThread.setDaemon(true);
        weatherThread.start();
    }

    /** Retrieves weather data and publishes the result on the JavaFX thread. */
    private void loadWeather(String destination) {
        try {
            String result = weatherService.getCurrentWeather(destination);
            Platform.runLater(() -> setResult(result));
        } catch (LuckException exception) {
            Platform.runLater(() -> setResult(exception.getMessage()));
        }
    }
}
