package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    Stage stage;
    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        // or   Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));

        controller = loader.getController();
        controller.setMain(this);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
        stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }

//    private void crop( Bounds bounds) {
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Image");
//
//        File file = fileChooser.showSaveDialog(stage);
//        if (file == null)
//            return;
//
//        int width = (int) bounds.getWidth();
//        int height = (int) bounds.getHeight();
//
//        SnapshotParameters parameters = new SnapshotParameters();
//        parameters.setFill(Color.TRANSPARENT);
//        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));
//
//        WritableImage wi = new WritableImage( width, height);
//        controller.imageView.snapshot(parameters, wi);
//
//        // save image
//        // !!! has bug because of transparency (use approach below) !!!
//        // --------------------------------
////        try {
////          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
////      } catch (IOException e) {
////          e.printStackTrace();
////      }
//
//
//        // save image (without alpha)
//        // --------------------------------
//        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
//        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
//
//        Graphics2D graphics = bufImageRGB.createGraphics();
//        graphics.drawImage(bufImageARGB, 0, 0, null);
//
//        try {
//
//            ImageIO.write(bufImageRGB, "jpg", file);
//
//            System.out.println( "Image saved to " + file.getAbsolutePath());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        graphics.dispose();
//
//    }
//
//    /**
//     * Drag rectangle with mouse cursor in order to get selection bounds
//     */
//    public static class RubberBandSelection {
//
//        final DragContext dragContext = new DragContext();
//        Rectangle rect;
//
//        Group group;
//
//
//        public Bounds getBounds() {
//            return rect.getBoundsInParent();
//        }
//
//        public RubberBandSelection( Group group) {
//
//            this.group = group;
//
//            rect = new Rectangle( 0,0,0,0);
//            rect.setStroke(Color.BLUE);
//            rect.setStrokeWidth(1);
//            rect.setStrokeLineCap(StrokeLineCap.ROUND);
//            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
//
//            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
//            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
//            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
//
//        }
//
//        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
//
//            //@Override
//            public void handle(MouseEvent event) {
//
//                if( event.isSecondaryButtonDown())
//                    return;
//
//                // remove old rect
//                rect.setX(0);
//                rect.setY(0);
//                rect.setWidth(0);
//                rect.setHeight(0);
//
//                group.getChildren().remove( rect);
//
//
//                // prepare new drag operation
//                dragContext.mouseAnchorX = event.getX();
//                dragContext.mouseAnchorY = event.getY();
//
//                rect.setX(dragContext.mouseAnchorX);
//                rect.setY(dragContext.mouseAnchorY);
//                rect.setWidth(0);
//                rect.setHeight(0);
//
//                group.getChildren().add( rect);
//
//            }
//        };
//
//        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
//
//            //@Override
//            public void handle(MouseEvent event) {
//
//                if( event.isSecondaryButtonDown())
//                    return;
//
//                double offsetX = event.getX() - dragContext.mouseAnchorX;
//                double offsetY = event.getY() - dragContext.mouseAnchorY;
//
//                if( offsetX > 0)
//                    rect.setWidth( offsetX);
//                else {
//                    rect.setX(event.getX());
//                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
//                }
//
//                if( offsetY > 0) {
//                    rect.setHeight( offsetY);
//                } else {
//                    rect.setY(event.getY());
//                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
//                }
//            }
//        };
//
//
//        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
//
//            //@Override
//            public void handle(MouseEvent event) {
//
//                if( event.isSecondaryButtonDown())
//                    return;
//
//                // remove rectangle
//                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
//                /*
//                rect.setX(0);
//                rect.setY(0);
//                rect.setWidth(0);
//                rect.setHeight(0);
//
//                group.getChildren().remove( rect);
//                */
//
//            }
//        };
//        private static final class DragContext {
//
//            public double mouseAnchorX;
//            public double mouseAnchorY;
//
//
//        }
//    }
}
