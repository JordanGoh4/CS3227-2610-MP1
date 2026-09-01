package luck.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import luck.model.Task;
import luck.model.TaskList;

/** Displays the active trip's itinerary and provides task deletion. */
public class ItineraryPanel extends VBox {
    private final ListView<String> itineraryView = new ListView<>();

    /** Creates an itinerary panel that delegates deletion to the supplied action. */
    public ItineraryPanel(Runnable deleteAction) {
        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteAction.run());
        itineraryView.setStyle("-fx-control-inner-background: rgba(0, 20, 35, 0.92);"
                + "-fx-text-background-color: white;");
        getChildren().addAll(new javafx.scene.control.Label("My itinerary"),
                itineraryView, deleteButton);
        setSpacing(8);
        GuiStyles.stylePanel(this);
    }

    /** Replaces the displayed tasks with those in the supplied task list. */
    public void refresh(TaskList taskList) {
        itineraryView.setItems(FXCollections.observableArrayList(
                taskList.getAll().stream().map(Task::toString).toList()));
    }

    /** Returns the selected task index, or -1 when no task is selected. */
    public int getSelectedIndex() {
        return itineraryView.getSelectionModel().getSelectedIndex();
    }
}
