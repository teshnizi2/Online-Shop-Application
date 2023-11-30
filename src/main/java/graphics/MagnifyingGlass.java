package graphics;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MagnifyingGlass extends Pane {
    private ImageView bgImageView;
    private Image image;
    private DoubleProperty magnification = new SimpleDoubleProperty();
    private DoubleProperty GLASS_SIZE = new SimpleDoubleProperty();
    private DoubleProperty GLASS_CENTER = new SimpleDoubleProperty();
    private DoubleProperty centerX = new SimpleDoubleProperty();
    private DoubleProperty centerY = new SimpleDoubleProperty();
    private DoubleProperty factor = new SimpleDoubleProperty();
    private DoubleProperty viewportCenterX = new SimpleDoubleProperty();
    private DoubleProperty viewportCenterY = new SimpleDoubleProperty();
    private DoubleProperty viewportSize = new SimpleDoubleProperty();
    private ImageView magGlass = new ImageView();
    private Group glassGroup = new Group();

    public MagnifyingGlass(Pane pane, ImageView imageView) {
        viewportCenterX.bind(centerX.multiply(factor));
        viewportCenterY.bind(centerY.multiply(factor));
        viewportSize.bind(GLASS_SIZE.multiply(factor).multiply(magnification));

        image = imageView.getImage();
        bgImageView = imageView;

        setupBgImageView();
        setupFactor();
        setupGLASS_SIZE();
        magnification.setValue(1.5);

        setupMagGlass();
        setupGlassGroup();
        bgImageView.requestFocus();
        this.getChildren().addAll(glassGroup);

        /*pane.getChildren().addAll(this);*/
    }

    public void adjustMagnification(final double amount) {
        double newValue = magnification.get() + amount / 4;
        if (newValue < .5) {
            newValue = .5;
        } else if (newValue > 10) {
            newValue = 10;
        }
        magnification.setValue(newValue);
    }

    private void setupGLASS_SIZE() {
        DoubleBinding db = new DoubleBinding() {
            {
                super.bind(bgImageView.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return bgImageView.boundsInLocalProperty().get().getWidth() / 4;
            }
        };
        GLASS_SIZE.bind(db);
        GLASS_CENTER.bind(GLASS_SIZE.divide(2));
    }

    private void setupFactor() {
        DoubleBinding db = new DoubleBinding() {
            {
                super.bind(image.heightProperty(), bgImageView.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return image.heightProperty().get() / bgImageView.boundsInLocalProperty().get().getHeight();
            }
        };

        factor.bind(db);
    }

    private void setupBgImageView() {
        bgImageView.setOnMouseMoved(me -> {
            centerX.setValue(me.getX());
            centerY.setValue(me.getY());
        });
        bgImageView.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.EQUALS || ke.getCode() == KeyCode.PLUS) {
                adjustMagnification(1.0);
            } else if (ke.getCode() == KeyCode.MINUS) {
                adjustMagnification(-1.0);
            }
        });

        bgImageView.setOnScroll(me -> adjustMagnification(me.getDeltaY() / 40));

        bgImageView.setOnMouseClicked(me -> {
            if (me.getButton() != MouseButton.PRIMARY) {
                magGlass.setSmooth(magGlass.isSmooth());
            }
            bgImageView.requestFocus();
        });
    }

    private void setupMagGlass() {
        magGlass.setImage(image);
        magGlass.setPreserveRatio(true);
        magGlass.fitWidthProperty().bind(GLASS_SIZE);
        magGlass.fitHeightProperty().bind(GLASS_SIZE);
        magGlass.setSmooth(true);
        ObjectBinding ob = new ObjectBinding() {
            {
                super.bind(viewportCenterX, viewportSize, viewportCenterY);
            }

            @Override
            protected Object computeValue() {
                return new Rectangle2D(viewportCenterX.get() - viewportSize.get() / 2, (viewportCenterY.get() - viewportSize.get() / 2), viewportSize.get(), viewportSize.get());
            }
        };
        magGlass.viewportProperty().bind(ob);
        Circle clip = new Circle();
        clip.centerXProperty().bind(GLASS_CENTER);
        clip.centerYProperty().bind(GLASS_CENTER);
        clip.radiusProperty().bind(GLASS_CENTER.subtract(5));
        magGlass.setClip(clip);

    }

    private void setupGlassGroup() {
        glassGroup.translateXProperty().bind(centerX.subtract(GLASS_CENTER));
        glassGroup.translateYProperty().bind(centerY.subtract(GLASS_CENTER));

        Text text = new Text();
        text.xProperty().bind(GLASS_CENTER.multiply(1.5));
        text.yProperty().bind(GLASS_SIZE);
        text.textProperty().bind(Bindings.concat("x", magnification));
        Circle circle = new Circle();
        circle.centerXProperty().bind(GLASS_CENTER);
        circle.centerYProperty().bind(GLASS_CENTER);

        circle.radiusProperty().bind(GLASS_CENTER.subtract(2));
        circle.setStroke(Color.GREEN);
        circle.setStrokeWidth(3);
        circle.setFill(null);
        glassGroup.getChildren().addAll(magGlass, text, circle);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(4);
        glassGroup.setEffect(dropShadow);
        glassGroup.setMouseTransparent(true);
    }
}