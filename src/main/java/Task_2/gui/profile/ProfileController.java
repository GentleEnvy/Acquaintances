package Task_2.gui.profile;

import Task_2.gui.Controller;
import Task_2.gui.utils.InterestsView;
import Task_2.gui.view_album.ProfileAlbumViewController;
import Task_2.models.Interest;
import Task_2.models.Photo;
import Task_2.models.User;
import Task_2.services.AlbumService;
import Task_2.services.DataBaseService;
import Task_2.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import static Task_2.gui.utils.Utils.getNumber;


public class ProfileController {
    private static final String defaultAvatarUrl =
        "https://sun9-49.userapi.com/c854424/v854424587/17a6/Ilm7SNg-ycU.jpg";

    private final DataBaseService dataBaseService = new DataBaseService();
    private final AlbumService albumService = new AlbumService();
    private final UserService userService = new UserService();


    private Controller controller;
    private InterestsView interestsView;

    private Boolean isMale = null;

    @FXML
    private ImageView avatarImageView;
    @FXML
    private Label idLabel;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField patronymicTextField;
    @FXML
    private TextField dayTextField;
    @FXML
    private TextField monthTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private Button genderButton;
    @FXML
    private VBox interestsVBox;
    @FXML
    private Button deleteButton;

    public static FXMLLoader getLoader() {
        return new FXMLLoader(ProfileController.class.getResource("profile.fxml"));
    }

    @FXML
    public void initialize(Controller controller) {
        this.controller = controller;
        User profile = controller.getProfile();
        if (profile == null) {
            initAdmin();
        } else {
            initFromProfile();
            initListeners();
        }
    }

    public Controller getController() {
        return controller;
    }

    private void initAdmin() {
        idLabel.setText("Admin");
        avatarImageView.setImage(new Image(defaultAvatarUrl));
        interestsVBox.getChildren().add(new InterestsView(() -> {}));
    }

    private void initFromProfile() {
        User profile = controller.getProfile();
        Photo avatar = albumService.getAvatar(dataBaseService.getAlbum(
            controller.getDataBase(),
            profile
        ));
        if (avatar == null) {
            avatarImageView.setImage(new Image(defaultAvatarUrl));
        } else {
            avatarImageView.setImage(new Image(avatar.getUrl()));
        }
        idLabel.setText(String.valueOf(profile.getId()));
        lastNameTextField.setText(profile.getLastName());
        firstNameTextField.setText(profile.getFirstName());
        patronymicTextField.setText(profile.getPatronymic());
        if (profile.getBirthday() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(profile.getBirthday());
            dayTextField.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            monthTextField.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            yearTextField.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        }
        if (profile.isMale() != null) {
            isMale = profile.isMale();
            genderButton.setText(profile.isMale() ? "мужчина" : "женщина");
        }
        interestsView = new InterestsView(() -> {
            profile.getInterests().clear();
            for (Interest selectedInterest : interestsView.getSelectedInterests()) {
                userService.addInterest(profile, selectedInterest);
            }
        });
        interestsView.setSelect(new HashSet<>(profile.getInterests()));
        interestsVBox.getChildren().clear();
        interestsVBox.getChildren().add(interestsView);
    }

    private void initListeners() {
        avatarImageView.setOnMouseClicked(__ -> ProfileAlbumViewController.show(
            this, controller.getProfile()
        ));
        lastNameTextField.textProperty().addListener(
            (__, ___, lastName) -> controller.getProfile().setLastName(
                lastName.equals("") ? null : lastName
            )
        );
        firstNameTextField.textProperty().addListener(
            (__, ___, firstName) -> controller.getProfile().setFirstName(
                firstName.equals("") ? null : firstName
            )
        );
        patronymicTextField.textProperty().addListener(
            (__, ___, patronymic) -> controller.getProfile().setPatronymic(
                patronymic.equals("") ? null : patronymic
            )
        );
        dayTextField.textProperty().addListener(
            (__, ___, ____) -> updateBirthday(controller.getProfile())
        );
        monthTextField.textProperty().addListener(
            (__, ___, ____) -> updateBirthday(controller.getProfile())
        );
        yearTextField.textProperty().addListener(
            (__, ___, ____) -> updateBirthday(controller.getProfile())
        );
        genderButton.setOnAction(__ -> changeGender(controller.getProfile()));
        deleteButton.setOnAction(__ -> {
            dataBaseService.removeUser(controller.getDataBase(), controller.getProfile());
            controller.setProfile(null);
        });
    }

    private void updateBirthday(User profile) {
        var calendar = Calendar.getInstance();
        if (profile.getBirthday() != null) {
            calendar.setTime(profile.getBirthday());
        }

        Integer day = getNumber(dayTextField);
        Integer month = getNumber(monthTextField);
        Integer year = getNumber(yearTextField);
        if (day == null) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }

        calendar = new GregorianCalendar(year, month - 1, day);
        profile.setBirthday(calendar.getTime());
    }

    private void changeGender(User profile) {
        if (isMale == null) {
            isMale = true;
            genderButton.setText("мужчина");
        } else if (isMale) {
            isMale = false;
            genderButton.setText("женщина");
        } else {  // is female
            isMale = null;
            genderButton.setText("не указан");
        }
        profile.setMale(isMale);
    }
}
