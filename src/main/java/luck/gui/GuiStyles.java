package luck.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Provides shared styling and resource configuration for the JavaFX GUI. */
public final class GuiStyles {
    private GuiStyles() {
    }

    /** Applies the bundled travel illustration as the application background. */
    public static void applyTravelBackground(BorderPane root) {
        Image image = new Image(GuiStyles.class.getResourceAsStream("/travel.png"));
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        root.setBackground(new Background(background));
    }

    /** Makes input text and placeholder text readable on dark panels. */
    public static void styleInput(Control input) {
        input.setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #d6e4ec;"
                + " -fx-control-inner-background: rgba(0, 20, 35, 0.92);");
    }

    /** Applies high-contrast styling to a GUI content panel. */
    public static VBox stylePanel(VBox panel) {
        panel.setPadding(new Insets(14));
        panel.setStyle("-fx-background-color: rgba(0, 35, 55, 0.88);"
                + "-fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.45);"
                + "-fx-border-radius: 10;");
        panel.getChildren().stream()
                .filter(child -> child instanceof Label)
                .forEach(child -> child.setStyle("-fx-text-fill: white;"));
        return panel;
    }
}
