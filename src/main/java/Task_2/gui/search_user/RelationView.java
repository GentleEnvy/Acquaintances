package Task_2.gui.search_user;

import Task_2.gui.Controller;
import Task_2.models.Acquaintance;
import Task_2.models.Relation;
import Task_2.models.RelationType;
import Task_2.models.User;
import Task_2.services.AcquaintanceService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class RelationView
    extends VBox
{
    private final AcquaintanceService acquaintanceService = new AcquaintanceService();

    private final Map<RelationType, CheckBoxTreeItem<String>> relationTreeViewMap =
        new HashMap<>();
    private final Set<RelationType> selectedRelations = new HashSet<>();

    private CheckBoxTreeItem<String> root;

    public RelationView(SearchController searchController) {
        setAlignment(Pos.CENTER);
        getChildren().addAll(
            createSetMyRelationButton(searchController),
            createRelationTreeView(searchController)
        );
    }

    public Set<RelationType> getSelectedRelations() {
        return selectedRelations;
    }

    private Button createSetMyRelationButton(SearchController searchController) {
        Button setMyRelationButton = new Button("Найти всех знакомых");
        setMyRelationButton.setOnAction(actionEvent -> {
            var controller = searchController.getController();
            User profile = controller.getProfile();
            var acquaintance = controller.getDataBase().getAcquaintance();
            Set<Relation> relations = new HashSet<>();
            if (profile != null) {
                relations = acquaintanceService.getUserRelations(
                    acquaintance,
                    profile
                );
            }
            Set<RelationType> relationTypes = new HashSet<>();
            for (var relation : relations) {
                relationTypes.addAll(relation.getRelationTypes());
            }
            for (var relationType : RelationType.values()) {
                if (relationTypes.contains(relationType)) {
                    CheckBoxTreeItem<String> item = relationTreeViewMap.get(relationType);
                    item.setSelected(true);
                }
            }
            root.setExpanded(true);
            searchController.update();
        });
        setMyRelationButton.setStyle("-fx-font-size: 16");
        return setMyRelationButton;
    }

    private TreeView<String> createRelationTreeView(SearchController searchController) {
        TreeView<String> interestsTreeView = new TreeView<>();
        interestsTreeView.setRoot(createRoot(searchController));
        interestsTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        interestsTreeView.setStyle("-fx-font-size: 16");
        return interestsTreeView;
    }

    private TreeItem<String> createRoot(SearchController searchController) {
        CheckBoxTreeItem<String> relations = new CheckBoxTreeItem<>("Отношения");
        for (var relationType : RelationType.values()) {
            relations.getChildren().add(createItem(relationType, searchController));
        }
        root = relations;
        return relations;
    }

    private CheckBoxTreeItem<String> createItem(
        RelationType relationType,
        SearchController searchController
    ) {
        CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(relationType.toString());
        item.selectedProperty().addListener((__, ___, isSelect) -> {
            if (isSelect) {
                selectedRelations.add(relationType);
            } else {
                selectedRelations.remove(relationType);
            }
            searchController.update();
        });
        relationTreeViewMap.put(relationType, item);
        return item;
    }
}
