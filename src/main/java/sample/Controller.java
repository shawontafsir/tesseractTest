package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class Controller {
    private Main main;
    private Image image;


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
        PixelReader reader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(reader, 0,0,300,80);
        System.out.println(image.getWidth() + "     " + image.getHeight());
        image = writableImage;
        imageView.setImage(image);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(writableImage, null);
        //BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        File file = new File("test.png");
        try {

            ImageIO.write(bufImageARGB, "png", file);

            System.out.println( "Image saved to " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        url = file.getAbsolutePath();
        System.out.println(image.getWidth() + "     " + image.getHeight());
    }

//    private void crop( Bounds bounds) {
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Image");
//
//        File file = fileChooser.showSaveDialog(main.stage);
//        if (file == null)
//            return;
//
//        int width = (int) bounds.getWidth();
//        int height = (int) bounds.getHeight();
//
//        SnapshotParameters parameters = new SnapshotParameters();
//        parameters.setFill(javafx.scene.paint.Color.TRANSPARENT);
//        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));
//
//        WritableImage wi = new WritableImage( width, height);
//        imageView.snapshot(parameters, wi);
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
//            ImageIO.write(bufImageRGB, ".jpg", file);
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
//        javafx.scene.shape.Rectangle rect;
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
//            rect.setStroke(javafx.scene.paint.Color.BLUE);
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
