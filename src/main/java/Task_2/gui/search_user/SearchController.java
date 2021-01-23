package Task_2.gui.search_user;

import Task_2.gui.Controller;
import Task_2.gui.utils.InterestsView;
import Task_2.models.DataBase;
import Task_2.models.Interest;
import Task_2.models.User;
import Task_2.search_engine.SearchUserEngine;
import Task_2.search_engine.filters.*;
import Task_2.services.AcquaintanceService;
import Task_2.services.DataBaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.*;

import static Task_2.gui.utils.Utils.getNumber;


public class SearchController {
    private final DataBaseService dataBaseService = new DataBaseService();
    private final AcquaintanceService acquaintanceService = new AcquaintanceService();

    private final SearchUserEngine searchUserEngine = new SearchUserEngine();

    private Controller controller;
    private UserButtonFactory userButtonFactory;
    private InterestsView interestsView;
    private RelationView relationView;

    private RadioButton genderSelect = null;

    @FXML
    public VBox usersVBox;
    @FXML
    private RadioButton anyRadioButton;
    @FXML
    private RadioButton allRadioButton;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField patronymicTextField;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField beforeTextField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private VBox interestsVBox;
    @FXML
    private VBox relationsVBox;

    @FXML
    public void initialize(Controller controller) {
        this.controller = controller;
        userButtonFactory = new UserButtonFactory(this);
        interestsView = new InterestsView(this::update);
        relationView = new RelationView(this);

        interestsVBox.getChildren().addAll(
            createSetMyInterestsButton(),
            interestsView
        );
        relationsVBox.getChildren().add(relationView);
        init();
        update();
    }

    public static FXMLLoader getLoader() {
        return new FXMLLoader(SearchController.class.getResource("search.fxml"));
    }

    public Controller getController() {
        return controller;
    }

    private void init() {
        maleRadioButton.setOnMouseClicked(__ -> genderClickedEvent(maleRadioButton));
        femaleRadioButton.setOnMouseClicked(__ -> genderClickedEvent(femaleRadioButton));
    }

    private void genderClickedEvent(RadioButton radioButton) {
        if (genderSelect == radioButton) {
            radioButton.setSelected(false);
            genderSelect = null;
        } else {
            genderSelect = radioButton;
        }
        update();
    }

    @FXML
    public void update() {
        usersVBox.getChildren().clear();
        List<User> filteredUsers;
        Set<BaseFilter> filters = createFilters();
        if (filters.isEmpty()) {
            filteredUsers = new LinkedList<>(
                acquaintanceService.getUsers(controller.getDataBase().getAcquaintance())
            );
        } else {
            if (anyRadioButton.isSelected()) {  // any
                filteredUsers = searchUserEngine.searchAnyUsers(
                    controller.getDataBase().getAcquaintance(),
                    createFilters(),
                    null
                );
            } else if (allRadioButton.isSelected()) {  // all
                filteredUsers = searchUserEngine.searchAllUsers(
                    controller.getDataBase().getAcquaintance(),
                    createFilters(),
                    null
                );
            } else {  // not selected
                throw new RuntimeException("The type of search is not selected");
            }
        }

        filteredUsers.remove(controller.getProfile());
        for (User filteredUser : filteredUsers) {
            usersVBox.getChildren().add(userButtonFactory.create(filteredUser));
        }
    }

    private Button createSetMyInterestsButton() {
        Button setMyInterestsButton = new Button("Установить мои интересы");
        setMyInterestsButton.setOnAction(__ -> {
            Set<Interest> selectInterests = new HashSet<>();
            User profile = controller.getProfile();
            if (profile != null) {
                for (var interest : Interest.values()) {
                    if (profile.getInterests().contains(interest)) {
                        selectInterests.add(interest);
                    }
                }
            }
            interestsView.setSelect(selectInterests);
        });
        setMyInterestsButton.setStyle("-fx-font-size: 16");
        return setMyInterestsButton;
    }

    private Set<BaseFilter> createFilters() {
        Set<BaseFilter> filters = new HashSet<>();

        addFilter(filters, createIdFilter());
        addFilter(filters, createNameFilter());
        addFilter(filters, createAgeFilter());
        addFilter(filters, createGenderFilter());
        for (var interestFilter : createInterestFilters()) {
            addFilter(filters, interestFilter);
        }
        for (var relationFilter : createRelationFilters()) {
            addFilter(filters, relationFilter);
        }

        return filters;
    }

    private void addFilter(Set<BaseFilter> filters, BaseFilter filter) {
        if (filter != null) {
            filters.add(filter);
        }
    }

    private IdFilter createIdFilter() {
        Integer id = getNumber(idTextField);
        if (id == null) {
            return null;
        }
        return new IdFilter(id);
    }

    private NameFilter createNameFilter() {
        String lastName = lastNameTextField.getText();
        String firstName = firstNameTextField.getText();
        String patronymic = patronymicTextField.getText();
        if (lastName.equals("") && firstName.equals("") && patronymic.equals("")) {
            return null;
        }
        return new NameFilter(lastName, firstName, patronymic);
    }

    private AgeFilter createAgeFilter() {
        Integer from = getNumber(fromTextField);
        Integer before  = getNumber(beforeTextField);
        if (from == null && before == null) {
            return null;
        }
        return new AgeFilter(from, before);
    }

    private GenderFilter createGenderFilter() {
        boolean isMale;
        if (maleRadioButton.isSelected()) {  // male
            isMale = true;
        } else if (femaleRadioButton.isSelected()) {  // female
            isMale = false;
        } else {  // not selected
            return null;
        }
        return new GenderFilter(isMale);
    }

    private Set<InterestFilter> createInterestFilters() {
        Set<InterestFilter> interestFilters = new HashSet<>();
        for (Interest selectedInterest : interestsView.getSelectedInterests()) {
            interestFilters.add(new InterestFilter(selectedInterest));
        }
        return interestFilters;
    }

    private Set<RelationFilter> createRelationFilters() {
        Set<RelationFilter> relationFilters = new HashSet<>();
        for (var relationType : relationView.getSelectedRelations()) {
            relationFilters.add(new RelationFilter(
                controller.getDataBase().getAcquaintance(),
                controller.getProfile(),
                relationType
            ));
        }
        return relationFilters;
    }

    @FXML
    private void addUserEvent() {
        User newUser = dataBaseService.addUser(controller.getDataBase());
        controller.setProfile(newUser);
    }
}
