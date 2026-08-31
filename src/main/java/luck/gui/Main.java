package luck.gui;

import java.nio.file.Path;
import java.nio.file.Files;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import luck.command.CommandContext;
import luck.command.CommandHandler;
import luck.exception.LuckException;
import luck.model.Task;
import luck.model.TaskList;
import luck.model.TripInfo;
import luck.storage.TaskStorage;
import luck.storage.TripInfoStorage;
import luck.service.WeatherService;

/** Displays Luck's travel-planning dashboard and chat interface. */
public class Main extends Application {
    private final TaskList taskList = new TaskList();
    private final ListView<String> itineraryView = new ListView<>();
    private final ListView<String> chatView = new ListView<>();
    private final TextField chatInput = new TextField();
    private final Label statusLabel = new Label();
    private final Label weatherResult = new Label("No weather request yet.");
    private final Label tripSummary = new Label();
    private TabPane tabs;
    private Tab weatherTab;
    private CommandHandler commandHandler;
    private CommandContext commandContext;
    private TaskStorage storage;
    private TripInfoStorage tripInfoStorage;
    private final WeatherService weatherService = new WeatherService();
    private final java.util.List<TripInfo> trips = new java.util.ArrayList<>();
    private String activeCommand = "";

    /** Creates and displays the travel planner window. */
    @Override
    public void start(Stage stage) {
        initialiseApplication();
        stage.setScene(new Scene(createRoot(), 1100, 650));
        stage.setTitle("Luck Travel Planner");
        stage.show();
        refreshItinerary();
        addChatMessage("Luck", "Welcome! Tell me what you want to plan for your trip.");
    }

    /** Loads stored tasks and prepares the command handler. */
    private void initialiseApplication() {
        storage = new TaskStorage(Path.of("data", "luck.txt"));
        tripInfoStorage = new TripInfoStorage(Path.of("data", "trip-info.properties"));
        trips.addAll(tripInfoStorage.loadAll());
        storage.ensureFileExists();
        for (Task task : storage.loadTasks()) {
            try {
                taskList.add(task);
            } catch (LuckException exception) {
                statusLabel.setText(exception.getMessage());
            }
        }
        commandContext = new CommandContext(taskList, storage,
                new GuiConsoleUI(message -> {
                    addChatMessage("Luck", message);
                    if (activeCommand.startsWith("weather ")) {
                        weatherResult.setText(message);
                    }
                }));
        commandHandler = new CommandHandler(commandContext);
    }

    /** Builds the root layout containing the tabs and chat panel. */
    private BorderPane createRoot() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        applyTravelBackground(root);
        VBox titlePanel = new VBox(4, new Label("Luck Travel Planner"),
                new Label("Plan destinations, organise your itinerary, and chat with Luck."));
        root.setTop(stylePanel(titlePanel));
        tabs = new TabPane();
        tabs.getTabs().add(createTab("Itinerary", createItineraryPanel()));
        weatherTab = createTab("Weather", createWeatherPanel());
        tabs.getTabs().add(weatherTab);
        tabs.getTabs().add(createTab("Trip Info", createTripInfoPanel()));
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        HBox mainContent = new HBox(12, tabs, createChatPanel());
        tabs.setPrefWidth(440);
        VBox chatPanel = (VBox) mainContent.getChildren().get(1);
        HBox.setHgrow(tabs, Priority.SOMETIMES);
        HBox.setHgrow(chatPanel, Priority.ALWAYS);
        root.setCenter(mainContent);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(14, 0, 0, 0));
        return root;
    }

    /** Applies the bundled travel illustration as the application background. */
    private void applyTravelBackground(BorderPane root) {
        Image image = new Image(getClass().getResourceAsStream("/travel.png"));
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        root.setBackground(new Background(background));
    }

    /** Creates a non-closable tab with the supplied content. */
    private Tab createTab(String title, VBox content) {
        Tab tab = new Tab(title, content);
        return tab;
    }

    /** Creates the initial weather placeholder for the travel planner. */
    private VBox createWeatherPanel() {
        Label heading = new Label("Weather");
        Label description = new Label("Latest weather result:");
        Label example = new Label("Try it in Chat: weather Tokyo");
        weatherResult.setWrapText(true);
        return stylePanel(new VBox(12, heading, description, weatherResult, example));
    }

    /** Creates the initial trip-information panel for future travel features. */
    private VBox createTripInfoPanel() {
        Label heading = new Label("Trip information");
        ComboBox<String> tripSelector = new ComboBox<>();
        tripSelector.setPromptText("Select a trip");
        trips.forEach(trip -> tripSelector.getItems().add(trip.name()));
        TextField tripName = new TextField();
        tripName.setPromptText("Trip name, e.g. Japan Holiday 2026");
        TextField destination = new TextField();
        destination.setPromptText("Destination, e.g. Tokyo, Japan");
        TextField startDate = new TextField();
        startDate.setPromptText("Start date, e.g. 10/09/2026");
        TextField endDate = new TextField();
        endDate.setPromptText("End date, e.g. 15/09/2026");
        TextField currency = new TextField();
        currency.setPromptText("Home currency, e.g. SGD");
        destination.setOnAction(event -> suggestCurrency(destination, currency));
        TextArea notes = new TextArea();
        notes.setPromptText("Travel notes, e.g. vegetarian food and public transport");
        notes.setPrefRowCount(5);
        styleInput(tripName);
        styleInput(destination);
        styleInput(startDate);
        styleInput(endDate);
        styleInput(currency);
        styleInput(notes);
        Button saveButton = new Button("Save trip details");
        Button newTripButton = new Button("New trip");
        saveButton.setOnAction(event -> saveTripInfo(tripSelector, tripName, destination, startDate, endDate, currency, notes));
        newTripButton.setOnAction(event -> clearTripFields(tripSelector, tripName, destination, startDate, endDate, currency, notes));
        tripSelector.setOnAction(event -> loadSelectedTrip(tripSelector, tripName, destination, startDate, endDate, currency, notes));
        if (!trips.isEmpty()) {
            tripSelector.getSelectionModel().selectFirst();
            loadSelectedTrip(tripSelector, tripName, destination, startDate, endDate, currency, notes);
        }
        return stylePanel(new VBox(10, heading, tripSelector, tripName, destination, startDate, endDate, currency, notes,
                new HBox(8, newTripButton, saveButton), tripSummary));
    }

    /** Suggests common destination currencies when the user confirms a destination. */
    private void suggestCurrency(TextField destination, TextField currency) {
        String country = destination.getText().trim().toLowerCase();
        java.util.Map<String, String> currencies = java.util.Map.of(
                "japan", "JPY", "singapore", "SGD", "korea", "KRW", "south korea", "KRW",
                "united states", "USD", "usa", "USD", "uk", "GBP", "united kingdom", "GBP",
                "australia", "AUD", "thailand", "THB");
        String suggestedCurrency = currencies.get(country);
        if (suggestedCurrency != null) {
            currency.setText(suggestedCurrency);
        }
    }

    /** Saves trip fields and updates the trip summary. */
    private void saveTripInfo(ComboBox<String> tripSelector, TextField tripName, TextField destination, TextField startDate, TextField endDate,
                              TextField currency, TextArea notes) {
        String name = tripName.getText().trim();
        if (name.isEmpty()) {
            name = tripSelector.getValue() == null ? "Trip " + (trips.size() + 1) : tripSelector.getValue();
        }
        TripInfo tripInfo = new TripInfo(name, destination.getText().trim(), startDate.getText().trim(),
                endDate.getText().trim(), currency.getText().trim(), notes.getText().trim());
        if (!tripInfo.isComplete()) {
            statusLabel.setText("Destination, start date, and end date are required.");
            return;
        }
        int index = tripSelector.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            trips.add(tripInfo);
            tripSelector.getItems().add(tripInfo.name());
            tripSelector.getSelectionModel().selectLast();
            switchToTrip(tripInfo);
        } else {
            trips.set(index, tripInfo);
        }
        tripInfoStorage.saveAll(trips);
        updateTripSummary(tripInfo);
        statusLabel.setText("Trip details saved.");
    }

    /** Loads the selected trip into the editable fields. */
    private void loadSelectedTrip(ComboBox<String> selector, TextField tripName, TextField destination, TextField startDate,
                                  TextField endDate, TextField currency, TextArea notes) {
        int index = selector.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        TripInfo trip = trips.get(index);
        switchToTrip(trip);
        tripName.setText(trip.name());
        destination.setText(trip.destination());
        startDate.setText(trip.startDate());
        endDate.setText(trip.endDate());
        currency.setText(trip.currency());
        notes.setText(trip.notes());
        updateTripSummary(trip);
    }

    /** Switches the shared command context and itinerary to the selected trip. */
    private void switchToTrip(TripInfo trip) {
        storage.saveTasks(taskList.getAll());
        Path tripPath = Path.of("data", "trips", slug(trip.name()) + ".txt");
        boolean isNewTrip = Files.notExists(tripPath);
        TaskStorage tripStorage = new TaskStorage(tripPath);
        tripStorage.ensureFileExists();
        if (isNewTrip && !taskList.isEmpty()) {
            tripStorage.saveTasks(taskList.getAll());
        }
        taskList.clear();
        for (Task task : tripStorage.loadTasks()) {
            try {
                taskList.add(task);
            } catch (LuckException exception) {
                statusLabel.setText(exception.getMessage());
            }
        }
        commandContext.setStorage(tripStorage);
        refreshItinerary();
        refreshWeather(trip.destination());
    }

    /** Refreshes the Weather tab for the selected trip destination. */
    private void refreshWeather(String destination) {
        if (destination.isBlank()) {
            weatherResult.setText("Add a destination to view its weather.");
            return;
        }
        weatherResult.setText("Loading weather for " + destination + "...");
        Thread weatherThread = new Thread(() -> {
            try {
                String result = weatherService.getCurrentWeather(destination);
                Platform.runLater(() -> weatherResult.setText(result));
            } catch (LuckException exception) {
                Platform.runLater(() -> weatherResult.setText(exception.getMessage()));
            }
        });
        weatherThread.setDaemon(true);
        weatherThread.start();
    }

    /** Produces a safe file name for a trip. */
    private String slug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    /** Clears the fields ready for a new trip. */
    private void clearTripFields(ComboBox<String> selector, TextField tripName, TextField destination, TextField startDate,
                                 TextField endDate, TextField currency, TextArea notes) {
        selector.getSelectionModel().clearSelection();
        taskList.clear();
        refreshItinerary();
        tripName.clear(); destination.clear(); startDate.clear(); endDate.clear(); currency.clear(); notes.clear();
        tripSummary.setText("Enter details for a new trip.");
    }

    /** Displays the currently saved trip details. */
    private void updateTripSummary(TripInfo tripInfo) {
        tripSummary.setText("Saved trip: " + tripInfo.destination() + " ("
                + tripInfo.startDate() + " to " + tripInfo.endDate() + ")");
        tripSummary.setWrapText(true);
    }

    /** Creates the itinerary dashboard with refresh and delete actions. */
    private VBox createItineraryPanel() {
        Label heading = new Label("My itinerary");
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshItinerary());
        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteSelectedTask());
        itineraryView.setStyle("-fx-control-inner-background: rgba(0, 20, 35, 0.92);"
                + "-fx-text-background-color: white;");
        return stylePanel(new VBox(8, heading, itineraryView, new HBox(8, refreshButton, deleteButton)));
    }

    /** Creates the chat history, input box, and send button. */
    private VBox createChatPanel() {
        Label heading = new Label("Chat with Luck");
        chatView.setPrefHeight(470);
        chatView.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(String message, boolean empty) {
                super.updateItem(message, empty);
                setText(empty ? null : message);
                setWrapText(true);
                setPrefHeight(USE_COMPUTED_SIZE);
            }
        });
        chatView.setStyle("-fx-control-inner-background: rgba(0, 20, 35, 0.92);"
                + "-fx-text-background-color: white;");
        chatInput.setPromptText("Enter a command, e.g. weather Tokyo");
        styleInput(chatInput);
        chatInput.setOnAction(event -> sendChatMessage());
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendChatMessage());
        HBox inputRow = new HBox(8, chatInput, sendButton);
        HBox.setHgrow(chatInput, Priority.ALWAYS);
        return stylePanel(new VBox(8, heading, chatView, inputRow));
    }

    /** Makes input text and placeholder text readable on the dark panels. */
    private void styleInput(javafx.scene.control.Control input) {
        input.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #d6e4ec;"
                + " -fx-control-inner-background: rgba(0, 20, 35, 0.92);");
    }

    /** Applies high-contrast styling to a GUI content panel. */
    private VBox stylePanel(VBox panel) {
        panel.setPadding(new Insets(14));
        panel.setStyle("-fx-background-color: rgba(0, 35, 55, 0.88);"
                + "-fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.45);"
                + "-fx-border-radius: 10;");
        panel.getChildren().stream()
                .filter(child -> child instanceof javafx.scene.control.Label)
                .forEach(child -> child.setStyle("-fx-text-fill: white;"));
        return panel;
    }

    /** Sends a chat command to the existing command handler. */
    private void sendChatMessage() {
        String input = chatInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        addChatMessage("You", input);
        chatInput.clear();
        activeCommand = input.toLowerCase();
        try {
            if (input.equalsIgnoreCase("list")) {
                addChatMessage("Luck", "Your itinerary is already shown on the left.");
                return;
            }
            boolean sessionContinues = commandHandler.handle(input);
            if (!sessionContinues) {
                addChatMessage("Luck", "Goodbye! Safe travels.");
                ((Stage) chatInput.getScene().getWindow()).close();
                return;
            }
            if (input.toLowerCase().startsWith("weather ")) {
                tabs.getSelectionModel().select(weatherTab);
            } else if (isTaskCommand(input)) {
                tabs.getSelectionModel().select(0);
            }
            refreshItinerary();
            statusLabel.setText("Your itinerary is up to date.");
        } catch (LuckException exception) {
            addChatMessage("Luck", exception.getMessage());
            statusLabel.setText("I could not process that command.");
        } finally {
            activeCommand = "";
        }
    }

    /** Returns whether a command should display the itinerary tab. */
    private boolean isTaskCommand(String input) {
        String command = input.split("\\s+", 2)[0].toLowerCase();
        return command.equals("todo") || command.equals("deadline") || command.equals("event")
                || command.equals("list") || command.equals("find") || command.equals("delete")
                || command.equals("mark") || command.equals("unmark");
    }

    /** Adds a speaker message to the chat history. */
    private void addChatMessage(String speaker, String message) {
        chatView.getItems().add(speaker + ": " + message);
        chatView.scrollTo(chatView.getItems().size() - 1);
    }

    /** Refreshes the itinerary from the task model. */
    private void refreshItinerary() {
        itineraryView.setItems(FXCollections.observableArrayList(
                taskList.getAll().stream().map(Task::toString).toList()));
    }

    /** Deletes the selected itinerary task and saves the updated list. */
    private void deleteSelectedTask() {
        int selectedIndex = itineraryView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            statusLabel.setText("Select an itinerary item to delete it.");
            return;
        }
        try {
            taskList.remove(selectedIndex);
            storage.saveTasks(taskList.getAll());
            refreshItinerary();
            addChatMessage("Luck", "I removed that item from your itinerary.");
        } catch (LuckException exception) {
            statusLabel.setText(exception.getMessage());
        }
    }
}
