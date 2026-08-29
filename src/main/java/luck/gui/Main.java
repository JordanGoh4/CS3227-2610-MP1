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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import luck.command.CommandContext;
import luck.command.CommandHandler;
import luck.exception.LuckException;
import luck.model.Task;
import luck.model.TaskList;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;

/** Displays Luck's travel-planning dashboard and chat interface. */
public class Main extends Application {
    private final TaskList taskList = new TaskList();
    private final ListView<String> itineraryView = new ListView<>();
    private final ListView<String> chatView = new ListView<>();
    private final TextField chatInput = new TextField();
    private final Label statusLabel = new Label("Ready to plan your next trip.");
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
        commandHandler = new CommandHandler(new CommandContext(taskList, storage, new ConsoleUI()));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18));
        root.setTop(new VBox(4, new Label("Luck Travel Planner"),
                new Label("Plan destinations, organise your itinerary, and chat with Luck.")));
        root.setLeft(createItineraryPanel());
        root.setCenter(createChatPanel());
        root.setBottom(statusLabel);
        BorderPane.setMargin(root.getCenter(), new Insets(0, 0, 0, 18));
        BorderPane.setMargin(statusLabel, new Insets(14, 0, 0, 0));

        stage.setScene(new Scene(root, 980, 620));
        stage.setTitle("Luck Travel Planner");
        stage.show();
        refreshItinerary();
        addChatMessage("Luck", "Welcome! Tell me what you want to plan for your trip.");
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
        return new VBox(8, heading, chatView, new HBox(8, chatInput, sendButton));
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
            commandHandler.handle(input);
            addChatMessage("Luck", "Done — I updated your itinerary.");
            refreshItinerary();
            statusLabel.setText("Your itinerary is up to date.");
        } catch (LuckException exception) {
            addChatMessage("Luck", exception.getMessage());
            statusLabel.setText("I could not process that command.");
        }
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
