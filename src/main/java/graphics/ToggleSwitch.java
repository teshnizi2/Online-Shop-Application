package graphics;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javafx.event.EventHandler;

public class ToggleSwitch extends Parent {

    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public ToggleSwitch(int size, EventHandler<Event> eventHandler) {
        setProperties(size, eventHandler);
    }

    private void setProperties(int size, EventHandler<Event> eventHandler){
        Rectangle background = new Rectangle(size, size / 2.0);
        background.setArcWidth(size / 2.0);
        background.setArcHeight(size / 2.0);
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(size / 4.0);
        trigger.setCenterX(size / 4.0);
        trigger.setCenterY(size / 4.0);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            translateAnimation.setToX(isOn ? size - size / 2.0 : 0);
            fillAnimation.setFromValue(isOn ? Color.WHITE : Color.BLUE);
            fillAnimation.setToValue(isOn ? Color.BLUE : Color.WHITE);

            animation.play();
        });

        setOnMouseClicked( event -> {
            eventHandler.handle(event);
            switchedOn.set(!switchedOn.get());
        });
    }
}