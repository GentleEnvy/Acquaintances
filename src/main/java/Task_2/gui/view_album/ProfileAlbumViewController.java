package Task_2.gui.view_album;

import Task_2.gui.profile.ProfileController;
import Task_2.models.Album;
import Task_2.models.Photo;
import Task_2.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import static Task_2.gui.utils.Utils.normalizeImageViewSize;


public class ProfileAlbumViewController
    extends BaseAlbumViewController
{
    public static FXMLLoader getLoader() {
        return new FXMLLoader(
            ProfileAlbumViewController.class.getResource("profile_album_view.fxml")
        );
    }

    public static void show(ProfileController profileController, User profile) {
        FXMLLoader albumLoader = ProfileAlbumViewController.getLoader();
        try {
            Pane albumPane = albumLoader.load();
            albumLoader.<ProfileAlbumViewController>getController().initialize(
                profileController,
                profile
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(albumPane));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final double MAX_WIDTH = 300;
    private static final double MAX_HEIGHT = 300;

    private ProfileController profileController;
    private User profile;

    @FXML
    private FlowPane imagesFlowPane;

    public void initialize(ProfileController profileController, User profile) {
        this.profileController = profileController;
        this.profile = profile;

        baseInit();

        imagesFlowPane.getChildren().add(createAddImageTextArea());
    }

    @Override
    protected FlowPane getImagesFlowPane() {
        return imagesFlowPane;
    }

    @Override
    protected Album getAlbum() {
        return dataBaseService.getAlbum(
            profileController.getController().getDataBase(),
            profile
        );
    }

    @Override
    protected VBox createImageVBox(Photo photo) {
        var vBox = new VBox();
        var imageView = createImageView(photo);
        if (imageView == null) {
            return null;
        }
        vBox.getChildren().addAll(
            imageView,
            createSettingsGridPane(photo, imageView.getFitWidth())
        );
        imageMap.put(photo, vBox);
        return vBox;
    }

    private ImageView createImageView(Photo photo) {
        Image image;
        if (imageMap.containsKey(photo)) {
            image = ((ImageView) imageMap.get(photo).getChildren().get(0)).getImage();
        } else {
            try {
                image = new Image(photo.getUrl());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        var imageView = new ImageView(image);
        normalizeImageViewSize(imageView, MAX_WIDTH, MAX_HEIGHT);
        return imageView;
    }

    private GridPane createSettingsGridPane(Photo photo, double width) {
        var gridPane = new GridPane();
        gridPane.setPrefWidth(width);
        ColumnConstraints likesColumnConstraints = new ColumnConstraints();
        likesColumnConstraints.setPercentWidth(20);
        ColumnConstraints setAvatarColumnConstraints = new ColumnConstraints();
        setAvatarColumnConstraints.setPercentWidth(40);
        ColumnConstraints deleteColumnConstraints = new ColumnConstraints();
        deleteColumnConstraints.setPercentWidth(40);
        gridPane.getColumnConstraints().addAll(
            likesColumnConstraints,
            setAvatarColumnConstraints,
            deleteColumnConstraints
        );
        Label likesLabel = createLikesLabel(photo);
        Button setAvatarButton = createSetAvatarButton(photo);
        FlowPane setAvatarFlowPane = new FlowPane(setAvatarButton);
        setAvatarFlowPane.setAlignment(Pos.CENTER);
        Button deleteButton = createDeleteButton(photo);
        FlowPane deleteFlowPane = new FlowPane(deleteButton);
        deleteFlowPane.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setColumnIndex(likesLabel, 0);
        GridPane.setColumnIndex(setAvatarFlowPane, 1);
        GridPane.setColumnIndex(deleteFlowPane, 2);
        gridPane.getChildren().addAll(
            likesLabel,
            setAvatarFlowPane,
            deleteFlowPane
        );
        return gridPane;
    }

    private Label createLikesLabel(Photo photo) {
        var label = new Label("❤ " + photo.getLikes().size());
        label.setFont(new Font(16));
        label.setTextFill(Color.RED);
        return label;
    }

    private Button createSetAvatarButton(Photo photo) {
        var button = new Button("аватар");
        button.setFont(new Font(15));
        button.setOnAction(__ -> {
            var album = dataBaseService.getAlbum(
                profileController.getController().getDataBase(),
                profile
            );
            if (photo.getUrl().equals(album.getAvatarUrl())) {
                album.setAvatarUrl(null);
            } else {
                albumService.setAvatar(album, photo.getUrl());
            }
            initialize(profileController, profile);
            profileController.getController().update();
        });
        return button;
    }

    private Button createDeleteButton(Photo photo) {
        var button = new Button("удалить");
        button.setStyle("-fx-background-color: #e34f4f");
        button.setFont(new Font(15));
        button.setOnAction(__ -> {
            var album = dataBaseService.getAlbum(
                profileController.getController().getDataBase(),
                profile
            );
            boolean isUpdate = photo.getUrl().equals(album.getAvatarUrl());
            albumService.removePhoto(album, photo);
            if (isUpdate) {
                profileController.getController().update();
            }
            imagesFlowPane.getChildren().remove(imageMap.get(photo));
        });
        return button;
    }

    private TextArea createAddImageTextArea() {
        var textArea = new TextArea();
        textArea.setPromptText("url фото");
        textArea.setWrapText(true);
        textArea.setPrefSize(MAX_WIDTH, MAX_HEIGHT);
        textArea.setFont(new Font(18));
        textArea.textProperty().addListener(
            (__, ___, ____) -> textArea.setStyle("-fx-border-color: transparent")
        );
        textArea.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                imagesFlowPane.getChildren().remove(textArea);
                var album = dataBaseService.getAlbum(
                    profileController.getController().getDataBase(),
                    profile
                );
                String text = textArea.getText();
                textArea.setText("");
                text = text.replaceAll("\n", "");
                var photo = albumService.addPhoto(album, text);
                if (photo != null) {
                    VBox imageVBox = createImageVBox(photo);
                    if (imageVBox == null) {
                        textArea.setStyle("-fx-border-color: red");
                    } else {
                        imagesFlowPane.getChildren().add(imageVBox);
                    }
                } else {
                    textArea.setStyle("-fx-border-color: red");
                }
                imagesFlowPane.getChildren().add(textArea);
            }
        });
        return textArea;
    }
}
