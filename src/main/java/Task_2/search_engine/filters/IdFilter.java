package Task_2.search_engine.filters;

import Task_2.models.User;


public class IdFilter
    extends BaseFilter
{
    private final Integer id;

    public IdFilter(Integer id) {
        this.id = id;
    }

    @Override
    public boolean match(User user) {
        if (id == null) {
            return true;
        }
        return user.getId() == id;
    }
}
