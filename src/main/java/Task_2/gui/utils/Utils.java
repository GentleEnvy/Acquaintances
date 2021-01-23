package Task_2.gui.utils;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Utils {
    public static Integer getNumber(TextField textField) {
        Integer number = null;
        try {
            number = Integer.parseInt(textField.getText());
            textField.setStyle("-fx-border-color: transparent");
        } catch (NumberFormatException e) {
            if (textField.getText().equals("")) {
                textField.setStyle("-fx-border-color: transparent");
            } else {
                textField.setStyle("-fx-border-color: red");
            }
        }
        return number;
    }

    public static void normalizeImageViewSize(
        ImageView imageView,
        double max_width,
        double max_height
    ) {
        Image image = imageView.getImage();
        double width = image.getWidth();
        double height = image.getHeight();
        if (width > max_width) {
            double kHeight = height / width;
            width = max_width;
            height = width * kHeight;
            if (height > max_height) {
                double kWidth = width / height;
                height = max_height;
                width = height * kWidth;
            }
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
}
