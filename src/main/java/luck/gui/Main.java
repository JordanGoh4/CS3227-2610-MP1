package luck.gui;

import java.nio.file.Path;
import java.nio.file.Files;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private final Label statusLabel = new Label();
    private TabPane tabs;
    private Tab weatherTab;
    private CommandHandler commandHandler;
    private CommandContext commandContext;
    private TaskStorage storage;
    private TripInfoStorage tripInfoStorage;
    private final WeatherService weatherService = new WeatherService();
    private WeatherPanel weatherPanel;
    private ItineraryPanel itineraryPanel;
    private ChatPanel chatPanel;
    private TripInfoPanel tripInfoPanel;
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
                        weatherPanel.setResult(message);
                    }
                }));
        commandHandler = new CommandHandler(commandContext);
    }

    /** Builds the root layout containing the tabs and chat panel. */
    private BorderPane createRoot() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        GuiStyles.applyTravelBackground(root);
        VBox titlePanel = new VBox(4, new Label("Luck Travel Planner"),
                new Label("Plan destinations, organise your itinerary, and chat with Luck."));
        root.setTop(GuiStyles.stylePanel(titlePanel));
        tabs = new TabPane();
        itineraryPanel = new ItineraryPanel(this::deleteSelectedTask);
        tabs.getTabs().add(createTab("Itinerary", itineraryPanel));
        weatherPanel = new WeatherPanel(weatherService);
        weatherTab = createTab("Weather", weatherPanel);
        tabs.getTabs().add(weatherTab);
        tripInfoPanel = new TripInfoPanel(trips, this::saveTripInfoFromPanel,
                this::clearTripFieldsFromPanel, this::loadSelectedTripFromPanel);
        tabs.getTabs().add(createTab("Trip Info", tripInfoPanel));
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        chatPanel = new ChatPanel(this::processChatMessage);
        HBox mainContent = new HBox(12, tabs, chatPanel);
        tabs.setPrefWidth(440);
        HBox.setHgrow(tabs, Priority.SOMETIMES);
        HBox.setHgrow(chatPanel, Priority.ALWAYS);
        root.setCenter(mainContent);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setStyle("-fx-background-color: rgba(0, 20, 35, 0.92);"
                + "-fx-text-fill: white;"
                + "-fx-padding: 8 12;");
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(14, 0, 0, 0));
        return root;
    }

    /** Creates a non-closable tab with the supplied content. */
    private Tab createTab(String title, VBox content) {
        Tab tab = new Tab(title, content);
        return tab;
    }

    /** Saves the trip currently entered in the extracted trip-information panel. */
    private void saveTripInfoFromPanel() {
        TripInfo tripInfo = tripInfoPanel.getTripInfo(trips.size() + 1);
        if (!tripInfo.isComplete()) {
            statusLabel.setText("Destination, start date, and end date are required.");
            return;
        }
        if (!tripInfo.hasValidDateRange()) {
            statusLabel.setText("Use valid future dates with the start date before the end date.");
            return;
        }
        try {
            weatherService.validateDestination(tripInfo.destination());
        } catch (LuckException exception) {
            statusLabel.setText(exception.getMessage());
            return;
        }
        int index = tripInfoPanel.getSelectedIndex();
        if (index < 0) {
            trips.add(tripInfo);
            tripInfoPanel.addTrip(tripInfo);
            switchToTrip(tripInfo);
        } else {
            trips.set(index, tripInfo);
            tripInfoPanel.updateSelectedTripName(tripInfo);
        }
        tripInfoStorage.saveAll(trips);
        tripInfoPanel.updateSummary(tripInfo);
        statusLabel.setText("Trip details saved.");
    }

    /** Clears the extracted trip-information panel for a new trip. */
    private void clearTripFieldsFromPanel() {
        taskList.clear();
        refreshItinerary();
        tripInfoPanel.clearForm();
    }

    /** Loads the trip selected in the extracted trip-information panel. */
    private void loadSelectedTripFromPanel() {
        int index = tripInfoPanel.getSelectedIndex();
        if (index < 0) {
            return;
        }
        TripInfo trip = trips.get(index);
        switchToTrip(trip);
        tripInfoPanel.showTrip(trip);
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
        weatherPanel.refresh(trip.destination());
    }

    /** Produces a safe file name for a trip. */
    private String slug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    /** Sends a chat command to the existing command handler. */
    private void processChatMessage(String input) {
        if (input.isEmpty()) {
            return;
        }
        addChatMessage("You", input);
        chatPanel.clearInput();
        activeCommand = input.toLowerCase();
        try {
            if (input.equalsIgnoreCase("list")) {
                addChatMessage("Luck", "Your itinerary is already shown on the left.");
                return;
            }
            boolean sessionContinues = commandHandler.handle(input);
            if (!sessionContinues) {
                addChatMessage("Luck", "Goodbye! Safe travels.");
                ((Stage) chatPanel.getScene().getWindow()).close();
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
        chatPanel.addMessage(speaker, message);
    }

    /** Refreshes the itinerary from the task model. */
    private void refreshItinerary() {
        itineraryPanel.refresh(taskList);
    }

    /** Deletes the selected itinerary task and saves the updated list. */
    private void deleteSelectedTask() {
        int selectedIndex = itineraryPanel.getSelectedIndex();
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
