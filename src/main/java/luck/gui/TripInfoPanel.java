package luck.gui;

import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import luck.model.TripInfo;

/** Provides the form for creating, editing, and selecting trips. */
public class TripInfoPanel extends VBox {
    private final ComboBox<String> tripSelector = new ComboBox<>();
    private final TextField tripName = new TextField();
    private final TextField destination = new TextField();
    private final TextField startDate = new TextField();
    private final TextField endDate = new TextField();
    private final TextField currency = new TextField();
    private final TextArea notes = new TextArea();
    private final Label tripSummary = new Label();
    /** Creates a trip-information panel using the supplied action callbacks. */
    public TripInfoPanel(List<TripInfo> trips, Runnable saveAction,
                         Runnable newTripAction, Runnable selectionAction) {
        Label heading = new Label("Trip information");
        tripSelector.setPromptText("Select a trip");
        trips.forEach(trip -> tripSelector.getItems().add(trip.name()));
        tripName.setPromptText("Trip name, e.g. Japan Holiday 2026");
        destination.setPromptText("Destination, e.g. Tokyo, Japan");
        startDate.setPromptText("Start date, e.g. 10/09/2026");
        endDate.setPromptText("End date, e.g. 15/09/2026");
        currency.setPromptText("Home currency, e.g. SGD");
        notes.setPromptText("Travel notes, e.g. vegetarian food and public transport");
        notes.setPrefRowCount(5);
        List.of(tripName, destination, startDate, endDate, currency, notes)
                .forEach(GuiStyles::styleInput);
        destination.setOnAction(event -> suggestCurrency());
        Button saveButton = new Button("Save trip details");
        Button newTripButton = new Button("New trip");
        saveButton.setOnAction(event -> saveAction.run());
        newTripButton.setOnAction(event -> newTripAction.run());
        tripSelector.setOnAction(event -> selectionAction.run());
        getChildren().addAll(heading, tripSelector, tripName, destination, startDate, endDate,
                currency, notes, new HBox(8, newTripButton, saveButton), tripSummary);
        setSpacing(10);
        GuiStyles.stylePanel(this);
    }

    /** Returns the details currently entered in the form. */
    public TripInfo getTripInfo(int nextTripNumber) {
        String name = tripName.getText().trim();
        if (name.isEmpty()) {
            name = tripSelector.getValue() == null ? "Trip " + nextTripNumber : tripSelector.getValue();
        }
        return new TripInfo(name, destination.getText().trim(), startDate.getText().trim(),
                endDate.getText().trim(), currency.getText().trim(), notes.getText().trim());
    }

    /** Returns the selected trip index, or -1 if a new trip is being created. */
    public int getSelectedIndex() {
        return tripSelector.getSelectionModel().getSelectedIndex();
    }

    /** Loads a trip's details into the form. */
    public void showTrip(TripInfo trip) {
        tripName.setText(trip.name());
        destination.setText(trip.destination());
        startDate.setText(trip.startDate());
        endDate.setText(trip.endDate());
        currency.setText(trip.currency());
        notes.setText(trip.notes());
        updateSummary(trip);
    }

    /** Clears the form and prepares it for a new trip. */
    public void clearForm() {
        tripSelector.getSelectionModel().clearSelection();
        tripName.clear();
        destination.clear();
        startDate.clear();
        endDate.clear();
        currency.clear();
        notes.clear();
        tripSummary.setText("Enter details for a new trip.");
    }

    /** Adds a newly saved trip to the selector and selects it. */
    public void addTrip(TripInfo trip) {
        tripSelector.getItems().add(trip.name());
        tripSelector.getSelectionModel().selectLast();
    }

    /** Replaces the displayed name of an edited trip. */
    public void updateSelectedTripName(TripInfo trip) {
        int index = getSelectedIndex();
        if (index >= 0) {
            tripSelector.getItems().set(index, trip.name());
        }
    }

    /** Updates the summary shown below the form. */
    public void updateSummary(TripInfo trip) {
        tripSummary.setText("Saved trip: " + trip.destination() + " ("
                + trip.startDate() + " to " + trip.endDate() + ")");
        tripSummary.setWrapText(true);
    }

    /** Suggests a common currency after the destination is confirmed. */
    private void suggestCurrency() {
        Map<String, String> currencies = Map.of(
                "japan", "JPY", "singapore", "SGD", "korea", "KRW", "south korea", "KRW",
                "united states", "USD", "usa", "USD", "uk", "GBP", "united kingdom", "GBP",
                "australia", "AUD", "thailand", "THB");
        String suggestedCurrency = currencies.get(destination.getText().trim().toLowerCase());
        if (suggestedCurrency != null) {
            currency.setText(suggestedCurrency);
        }
    }
}
