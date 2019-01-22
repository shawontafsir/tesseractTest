package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class Controller {
    private Main main;
    private Image image;
    RubberBandSelection rubberBandSelection;
    Rectangle selection;


    private String url="";

    @FXML
    private TextArea resultArea;
    @FXML
    public ImageView imageView;

    public void setMain(Main main) {
        this.main = main;
    }

    public String ocr(String path){
        Tesseract tesseract = new Tesseract();
        String text = "";
        try {
            tesseract.setDatapath("E:/code/intellij/tesseractProject/tessdata");
            text = tesseract.doOCR(new File(path));
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return text;
    }

    public String ocr(String path, java.awt.Rectangle r){
        Tesseract tesseract = new Tesseract();
        String text = "";
        try {
            tesseract.setDatapath("E:/code/intellij/tesseractProject/tessdata");

            text = tesseract.doOCR(new File(path), r);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return text;
    }

    @FXML
    public void printResult(ActionEvent actionEvent) {
        String text = ocr(url);
        resultArea.setText(text);
    }

    @FXML
    public void uploadImage(ActionEvent actionEvent) throws IOException {
        url = "E:/code/intellij/tesseractProject/images/vrkIj.png";
        System.out.println(url);
        FileInputStream input = new FileInputStream(url);
        image = new Image(input);
        imageView.setImage(image);
        //Group group = new Group();
        //group.getChildren().add(imageView);
        //rubberBandSelection = new RubberBandSelection(group);
    }

    @FXML
    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".bmp", "*.bmp"),
                new FileChooser.ExtensionFilter(".png", "*.png"),
                new FileChooser.ExtensionFilter(".tif", "*.tif")
        );

        File file = fileChooser.showOpenDialog(null);
        if(file!=null){
            try {
                url = file.toURI().toURL().toExternalForm();
                System.out.println(url);
                image = new Image(url);
                imageView.setImage(image);

                url = url.substring(6);
                System.out.println(url);
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    public void startDrag(MouseEvent mouseEvent) {

    }

    public void endDrag(DragEvent dragEvent) {
        System.out.println("1");
        PixelReader reader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(reader, 0,0,10,10);
        System.out.println("3");
        image = writableImage;
        System.out.println("4");
        imageView.setImage(image);
        System.out.println("5");
    }

    public void manualDrag(ActionEvent actionEvent) throws IOException {
        System.out.println(selection);
        java.awt.Rectangle r = new java.awt.Rectangle((int)selection.getX(), (int)selection.getY(), (int)selection.getWidth(), (int)selection.getHeight());
        System.out.println(r);
        String result = ocr(url, r);
        resultArea.setText(result);
    }

    /**
     * Drag rectangle with mouse cursor in order to get selection bounds
     */
    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        javafx.scene.shape.Rectangle rect;

        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new javafx.scene.shape.Rectangle( 0,0,0,0);
            rect.setStroke(javafx.scene.paint.Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            //@Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);


                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            //@Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            //@Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove rectangle
                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
                /*
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);
                */

            }
        };
        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }

}
