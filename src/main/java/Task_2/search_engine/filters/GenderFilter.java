package Task_2.search_engine.filters;

import Task_2.models.User;


public class GenderFilter extends BaseFilter {
    private final Boolean isMale;

    public GenderFilter(Boolean isMale) {
        this.isMale = isMale;
    }

    @Override
    public boolean match(User user) {
        if (isMale == null) {
            return true;
        }
        return user.isMale() == isMale;
    }
}
