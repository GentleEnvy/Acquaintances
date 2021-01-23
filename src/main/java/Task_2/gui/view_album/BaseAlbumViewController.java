package Task_2.gui.view_album;

import Task_2.gui.profile.ProfileController;
import Task_2.gui.search_user.SearchController;
import Task_2.models.Album;
import Task_2.models.Photo;
import Task_2.models.User;
import Task_2.services.AlbumService;
import Task_2.services.DataBaseService;
import Task_2.services.PhotoService;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;


abstract public class BaseAlbumViewController {
    protected static final double MAX_WIDTH = 300;
    protected static final double MAX_HEIGHT = 300;

    protected final DataBaseService dataBaseService = new DataBaseService();
    protected final AlbumService albumService = new AlbumService();
    protected final PhotoService photoService = new PhotoService();

    protected final Map<Photo, VBox> imageMap = new HashMap<>();

    public void baseInit() {
        FlowPane imagesFlowPane = getImagesFlowPane();
        var album = getAlbum();

        imagesFlowPane.getChildren().clear();

        Photo avatar = albumService.getAvatar(album);
        if (avatar != null) {
            VBox avatarVBox = createImageVBox(avatar);
            if (avatarVBox != null) {
                avatarVBox.setStyle("-fx-border-color: red");
                imagesFlowPane.getChildren().add(avatarVBox);
            }
        }
        for (Photo photo : album.getPhotos().values()) {
            if (photo != avatar) {
                VBox imageVBox = createImageVBox(photo);
                if (imageVBox != null) {
                    imagesFlowPane.getChildren().add(imageVBox);
                }
            }
        }
    }

    abstract protected FlowPane getImagesFlowPane();

    abstract protected Album getAlbum();

    abstract protected VBox createImageVBox(Photo photo);
}
