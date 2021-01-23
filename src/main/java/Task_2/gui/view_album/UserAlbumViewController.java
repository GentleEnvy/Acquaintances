package Task_2.gui.view_album;

import Task_2.gui.search_user.SearchController;
import Task_2.models.Album;
import Task_2.models.Photo;
import Task_2.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import static Task_2.gui.utils.Utils.normalizeImageViewSize;


public class UserAlbumViewController
    extends BaseAlbumViewController
{
    public static FXMLLoader getLoader() {
        return new FXMLLoader(
            UserAlbumViewController.class.getResource("user_album_view.fxml")
        );
    }

    public static void show(SearchController searchController, User profile) {
        FXMLLoader albumLoader = UserAlbumViewController.getLoader();
        try {
            Pane albumPane = albumLoader.load();
            albumLoader.<UserAlbumViewController>getController().initialize(
                searchController, profile
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(albumPane));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SearchController searchController;
    private User user;

    @FXML
    private FlowPane imagesFlowPane;
    @FXML
    private VBox defaultVBox;

    public void initialize(SearchController searchController, User user) {
        this.searchController = searchController;
        this.user = user;

        baseInit();

        if (imagesFlowPane.getChildren().isEmpty()) {
            imagesFlowPane.getChildren().add(defaultVBox);
        }
    }

    @Override
    protected FlowPane getImagesFlowPane() {
        return imagesFlowPane;
    }

    @Override
    protected Album getAlbum() {
        return dataBaseService.getAlbum(
            searchController.getController().getDataBase(),
            user
        );
    }

    @Override
    protected VBox createImageVBox(Photo photo) {
        var vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        var imageView = createImageView(photo);
        if (imageView == null) {
            return null;
        }
        vBox.getChildren().addAll(
            imageView,
            createLikesButton(photo)
        );
        imageMap.put(photo, vBox);
        return vBox;
    }

    private ImageView createImageView(Photo photo) {
        Image image;
        try {
            image = new Image(photo.getUrl());
        } catch (IllegalArgumentException e) {
            return null;
        }
        var imageView = new ImageView(image);
        normalizeImageViewSize(imageView, MAX_WIDTH, MAX_HEIGHT);
        return imageView;
    }

    private Button createLikesButton(Photo photo) {
        var button = new Button("❤ " + photo.getLikes().size());
        button.setFont(new Font(16));
        button.setTextFill(Color.RED);
        if (checkLike(photo)) {
            button.setStyle("-fx-background-color: #24dd24");
        } else {
            button.setStyle("-fx-background-color: #d9b0b0");
        }
        button.setOnAction(__ -> {
            User profile = searchController.getController().getProfile();
            if (checkLike(photo)) {
                photoService.unlike(photo, profile);
                button.setStyle("-fx-background-color: #d9b0b0");
            } else {
                if (profile != null) {
                    photoService.like(photo, profile);
                    button.setStyle("-fx-background-color: #24dd24");
                }
            }
            button.setText("❤ " + photo.getLikes().size());
        });
        return button;
    }

    private boolean checkLike(Photo photo) {
        User profile = searchController.getController().getProfile();
        if (profile != null) {
            var like = photoService.getLike(photo, profile);
            return like != null;
        }
        return false;
    }
}
