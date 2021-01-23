package Task_2.search_engine.filters;

import Task_2.models.User;
import Task_2.services.UserService;


public class AgeFilter
    extends BaseFilter
{
    private final UserService userService = new UserService();

    private final int from;
    private final int before;

    public AgeFilter(Integer from, Integer before) {
        this.from = from == null ? 0 : from;
        this.before = before == null ? Integer.MAX_VALUE : before;
    }

    @Override
    public boolean match(User user) {
        Integer age = userService.getAge(user);
        if (age == null) {
            return false;
        }
        return age >= from && age <= before;
    }
}
