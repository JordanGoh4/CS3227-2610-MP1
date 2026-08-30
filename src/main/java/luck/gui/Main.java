package luck.gui;

import java.nio.file.Path;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import luck.storage.TaskStorage;

/** Displays Luck's travel-planning dashboard and chat interface. */
public class Main extends Application {
    private final TaskList taskList = new TaskList();
    private final ListView<String> itineraryView = new ListView<>();
    private final ListView<String> chatView = new ListView<>();
    private final TextField chatInput = new TextField();
    private final Label statusLabel = new Label("Ready to plan your next trip.");
    private final Label weatherResult = new Label("No weather request yet.");
    private TabPane tabs;
    private Tab weatherTab;
    private CommandHandler commandHandler;
    private TaskStorage storage;

    /** Creates and displays the travel planner window. */
    @Override
    public void start(Stage stage) {
        storage = new TaskStorage(Path.of("data", "luck.txt"));
        storage.ensureFileExists();
        for (Task task : storage.loadTasks()) {
            try {
                taskList.add(task);
            } catch (LuckException exception) {
                statusLabel.setText(exception.getMessage());
            }
        }
        commandHandler = new CommandHandler(new CommandContext(taskList, storage,
                new GuiConsoleUI(message -> {
                    addChatMessage("Luck", message);
                    weatherResult.setText(message);
                })));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.setTop(new VBox(4, new Label("Luck Travel Planner"),
                new Label("Plan destinations, organise your itinerary, and chat with Luck.")));
        tabs = new TabPane();
        tabs.getTabs().add(createTab("Itinerary", createItineraryPanel()));
        weatherTab = createTab("Weather", createWeatherPanel());
        tabs.getTabs().add(weatherTab);
        tabs.getTabs().add(createTab("Trip Info", createTripInfoPanel()));
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        HBox mainContent = new HBox(12, tabs, createChatPanel());
        HBox.setHgrow(tabs, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(mainContent.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);
        root.setCenter(mainContent);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(14, 0, 0, 0));

        stage.setScene(new Scene(root, 980, 620));
        stage.setTitle("Luck Travel Planner");
        stage.show();
        refreshItinerary();
        addChatMessage("Luck", "Welcome! Tell me what you want to plan for your trip.");
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
        return new VBox(12, heading, description, weatherResult, example);
    }

    /** Creates the initial trip-information panel for future travel features. */
    private VBox createTripInfoPanel() {
        Label heading = new Label("Trip information");
        Label description = new Label("Your destination and travel dates will appear here.");
        Label note = new Label("This section is ready for future flight, hotel, and map features.");
        return new VBox(12, heading, description, note);
    }

    /** Creates the itinerary dashboard with refresh and delete actions. */
    private VBox createItineraryPanel() {
        Label heading = new Label("My itinerary");
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshItinerary());
        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteSelectedTask());
        itineraryView.setPrefWidth(390);
        return new VBox(8, heading, itineraryView, new HBox(8, refreshButton, deleteButton));
    }

    /** Creates the chat history, input box, and send button. */
    private VBox createChatPanel() {
        Label heading = new Label("Chat with Luck");
        chatView.setPrefHeight(470);
        chatInput.setPromptText("Try: find hotel or event visit Kyoto / from: 2026-09-10 / to: 2026-09-15");
        chatInput.setOnAction(event -> sendChatMessage());
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendChatMessage());
        HBox inputRow = new HBox(8, chatInput, sendButton);
        HBox.setHgrow(chatInput, Priority.ALWAYS);
        return new VBox(8, heading, chatView, inputRow);
    }

    /** Sends a chat command to the existing command handler. */
    private void sendChatMessage() {
        String input = chatInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        addChatMessage("You", input);
        chatInput.clear();
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
