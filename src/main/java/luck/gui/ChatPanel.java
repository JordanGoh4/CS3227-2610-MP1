package luck.gui;

import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Displays the chat history and forwards submitted commands to the application. */
public class ChatPanel extends VBox {
    private final ListView<String> chatView = new ListView<>();
    private final TextField chatInput = new TextField();

    /** Creates a chat panel that forwards submitted input to the supplied handler. */
    public ChatPanel(Consumer<String> commandHandler) {
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
        GuiStyles.styleInput(chatInput);
        chatInput.setOnAction(event -> submitCommand(commandHandler));
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> submitCommand(commandHandler));
        HBox inputRow = new HBox(8, chatInput, sendButton);
        HBox.setHgrow(chatInput, Priority.ALWAYS);
        getChildren().addAll(heading, chatView, inputRow);
        setSpacing(8);
        GuiStyles.stylePanel(this);
    }

    /** Adds a speaker message to the chat history. */
    public void addMessage(String speaker, String message) {
        chatView.getItems().add(speaker + ": " + message);
        chatView.scrollTo(chatView.getItems().size() - 1);
    }

    /** Clears the command input field. */
    public void clearInput() {
        chatInput.clear();
    }

    /** Returns the command input field. */
    public TextField getInput() {
        return chatInput;
    }

    /** Forwards non-empty input to the command handler. */
    private void submitCommand(Consumer<String> commandHandler) {
        String input = chatInput.getText().trim();
        if (!input.isEmpty()) {
            commandHandler.accept(input);
        }
    }
}
