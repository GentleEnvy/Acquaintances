package Task_2.gui.utils;

import Task_2.gui.search_user.SearchController;
import Task_2.models.Interest;
import Task_2.models.User;
import Task_2.services.InterestsService;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


public class InterestsView
    extends TreeView<String>
{
    private final InterestsService interestsService = new InterestsService();

    private final Map<Interest, CheckBoxTreeItem<String>> interestTreeViewMap =
        new HashMap<>();
    private final Set<Interest> selectedInterests = new HashSet<>();

    private CheckBoxTreeItem<String> root;

    public InterestsView(UpdateFunction updateFunction) {
        setRoot(createRoot(updateFunction));
        setCellFactory(CheckBoxTreeCell.forTreeView());
        setStyle("-fx-font-size: 16");
    }

    public Map<Interest, CheckBoxTreeItem<String>> getInterestTreeViewMap() {
        return interestTreeViewMap;
    }

    public Set<Interest> getSelectedInterests() {
        return selectedInterests;
    }

    public void setSelect(Set<Interest> selectInterests) {
        for (var interest : Interest.values()) {
            CheckBoxTreeItem<String> item = interestTreeViewMap.get(interest);
            if (item != null) {
                item.setSelected(false);
                item.setExpanded(true);
            }
        }
        root.setExpanded(true);

        for (Interest selectInterest : selectInterests) {
            CheckBoxTreeItem<String> item = interestTreeViewMap.get(selectInterest);
            if (item != null) {
                item.setSelected(true);
            }
        }
    }

    private TreeItem<String> createRoot(UpdateFunction updateFunction) {
        CheckBoxTreeItem<String> interests = new CheckBoxTreeItem<>("Интересы");
        Set<Interest> roots = interestsService.getRoots();
        for (Interest root : roots) {
            interests.getChildren().add(createItem(root, updateFunction));
        }
        root = interests;
        return interests;
    }

    private CheckBoxTreeItem<String> createItem(
        Interest interest,
        UpdateFunction updateFunction
    ) {
        CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(interest.toString());
        for (Interest subInterest : interest.subInterests) {
            item.getChildren().add(createItem(subInterest, updateFunction));
        }
        if (interest.subInterests.isEmpty()) {
            item.selectedProperty().addListener((__, ___, isSelect) -> {
                if (isSelect) {
                    selectedInterests.add(interest);
                } else {  // not selected
                    selectedInterests.remove(interest);
                }
                updateFunction.update();
            });
        }
        interestTreeViewMap.put(interest, item);
        return item;
    }

    @FunctionalInterface
    public interface UpdateFunction {
        void update();
    }
}
