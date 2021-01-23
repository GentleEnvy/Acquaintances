package Task_2.search_engine;

import Task_2.models.Acquaintance;
import Task_2.models.User;
import Task_2.search_engine.filters.BaseFilter;
import Task_2.services.AcquaintanceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


public class SearchUserEngine {
    private final AcquaintanceService acquaintanceService = new AcquaintanceService();

    public List<User> searchAllUsers(
        Acquaintance acquaintance,
        Collection<BaseFilter> filters,
        Comparator<User> sorter
    ) {
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (var user : acquaintanceService.getUsers(acquaintance)) {
            boolean nextUser = false;
            for (BaseFilter filter : filters) {
                if (!filter.match(user)) {
                    nextUser = true;
                    break;
                }
            }
            if (nextUser) {
                continue;
            }
            filteredUsers.add(user);
        }
        if (sorter != null) {
            filteredUsers.sort(sorter);
        }
        return filteredUsers;
    }

    public List<User> searchAnyUsers(
        Acquaintance acquaintance,
        Collection<BaseFilter> filters,
        Comparator<User> sorter
    ) {
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (var user : acquaintanceService.getUsers(acquaintance)) {
            for (BaseFilter filter : filters) {
                if (filter.match(user)) {
                    filteredUsers.add(user);
                    break;
                }
            }
        }
        if (sorter != null) {
            filteredUsers.sort(sorter);
        }
        return filteredUsers;
    }
}
