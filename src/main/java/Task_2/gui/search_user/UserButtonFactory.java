package Task_2.gui.search_user;

import Task_2.gui.view_album.UserAlbumViewController;
import Task_2.models.Relation;
import Task_2.models.RelationType;
import Task_2.models.User;
import Task_2.services.AcquaintanceService;
import Task_2.services.RelationService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;


class UserButtonFactory {
    private final AcquaintanceService acquaintanceService = new AcquaintanceService();
    private final RelationService relationService = new RelationService();

    private final SearchController searchController;

    public UserButtonFactory(SearchController searchController) {
        this.searchController = searchController;
    }

    public GridPane create(User user) {
        var gridPane = new GridPane();
        gridPane.setPrefWidth(searchController.usersVBox.getWidth());
        ColumnConstraints nameColumnConstraints = new ColumnConstraints();
        nameColumnConstraints.setPercentWidth(30);
        ColumnConstraints buttonsColumnConstraints = new ColumnConstraints();
        buttonsColumnConstraints.setPercentWidth(70);
        gridPane.getColumnConstraints().addAll(
            nameColumnConstraints,
            buttonsColumnConstraints
        );
        gridPane.setStyle("-fx-border-color: black");
        Label nameLabel = createNameLabel(user);
        HBox buttonsHBox = createButtonsHBox(user);
        GridPane.setColumnIndex(nameLabel, 0);
        GridPane.setColumnIndex(buttonsHBox, 1);
        gridPane.getChildren().addAll(
            nameLabel,
            buttonsHBox
        );
        return gridPane;
    }

    private HBox createButtonsHBox(User user) {
        var hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setStyle("-fx-padding: 0 15 0 0");
        hBox.getChildren().addAll(
            createRelationPane(user),
            createAlbumButton(user),
            createSetProfileButton(user)
        );
        return hBox;
    }

    private Label createNameLabel(User user) {
        Label nameLabel = new Label(getName(user));
        nameLabel.setFont(new Font(17));
        return nameLabel;
    }

    private Button createAlbumButton(User user) {
        var button = new Button("альбом");
        button.setFont(new Font(15));
        button.setOnAction(__ -> UserAlbumViewController.show(searchController, user));
        return button;
    }

    private Button createSetProfileButton(User user) {
        var button = new Button("выбрать профиль");
        button.setFont(new Font(15));
        button.setOnAction(__ -> searchController.getController().setProfile(user));
        return button;
    }

    private Pane createRelationPane(User user) {
        double heightItem = 35;
        double width = 200;

        var controller = searchController.getController();
        User profile = controller.getProfile();

        Relation relation;
        if (profile == null) {
            relation = null;
        } else {
            relation = acquaintanceService.getRelation(
                controller.getDataBase().getAcquaintance(),
                profile,
                user
            );
        }
        var pane = new HBox();
        ListView<HBox> listView = new ListView<>();
        listView.setStyle("-fx-font-size: 16");
        listView.setPrefWidth(width);
        for (var relationType : RelationType.values()) {
            var hBox = new HBox();
            hBox.setSpacing(3);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPrefHeight(heightItem);
            var checkBox = new CheckBox();
            if (relation == null) {
                checkBox.setSelected(false);
            } else {
                checkBox.setSelected(relation.getRelationTypes().contains(relationType));
            }
            checkBox.selectedProperty().addListener((__, ___, select) -> {
                if (profile != null) {
                    if (select) {
                        Relation newRelation = acquaintanceService.addRelation(
                            controller.getDataBase().getAcquaintance(),
                            profile,
                            user
                        );
                        relationService.addRelationType(
                            newRelation,
                            controller.getDataBase().getAcquaintance(),
                            relationType
                        );
                    } else {
                        acquaintanceService.removeRelation(
                            controller.getDataBase().getAcquaintance(),
                            profile,
                            user
                        );
                    }
                }
            });
            hBox.getChildren().addAll(
                checkBox, new Label(relationType.toString())
            );
            listView.getItems().add(hBox);
        }
        Label relations = new Label("отношения");
        relations.setStyle("-fx-background-color: #cde0df; -fx-background-radius: 7");
        relations.setFont(new Font(16));
        relations.setPrefWidth(width);
        relations.setPrefHeight(heightItem);
        relations.setAlignment(Pos.CENTER);
        pane.setOnMouseMoved(mouseEvent -> {
            pane.getChildren().clear();
            listView.setPrefHeight(heightItem * listView.getItems().size() * 1.26);
            pane.getChildren().add(listView);
        });
        pane.setOnMouseExited(mouseEvent -> {
            pane.getChildren().clear();
            pane.getChildren().add(relations);
        });
        pane.getChildren().add(relations);
        return pane;
    }

    private String getName(User user) {
        String lName = user.getLastName();
        String fName = user.getFirstName();
        String patron = user.getPatronymic();
        return String.format(
            "%d: %s %s %s",
            user.getId(),
            lName == null ? "-" : lName,
            fName == null ? "-" : fName,
            patron == null ? "-" : patron
        );
    }
}
