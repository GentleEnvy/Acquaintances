package Task_2.gui;

import Task_2.gui.profile.ProfileController;
import Task_2.gui.search_user.SearchController;
import Task_2.models.DataBase;
import Task_2.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class Controller {
    private Pane profilePane = null;
    private Pane searchPane = null;
    private Pane editPane = null;

    private DataBase dataBase;
    private User profile = null;

    private SearchController searchController;

    @FXML
    private GridPane contentGridPane;
    @FXML
    private GridPane profileGridPane;

    @FXML
    public void initialize(DataBase dataBase) {
        this.dataBase = dataBase;
        initSearchPane();
        initProfilePane();
        update();
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public User getProfile() {
        return profile;
    }

    public void update() {
        searchController.update();
        initProfilePane();
    }

    public void initSearchPane() {
        contentGridPane.getChildren().remove(editPane);
        editPane = null;

        FXMLLoader searchLoader = SearchController.getLoader();
        try {
            searchPane = searchLoader.load();
            searchController = searchLoader.getController();
            searchController.initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentGridPane.add(searchPane, 0, 0);
    }

    public void initProfilePane() {
        profileGridPane.getChildren().remove(profilePane);
        FXMLLoader profileLoader = ProfileController.getLoader();
        try {
            profilePane = profileLoader.load();
            profileLoader.<ProfileController>getController().initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileGridPane.add(profilePane, 0, 0);
    }

    public void setProfile(User profile) {
        this.profile = profile;
        update();
    }
}
