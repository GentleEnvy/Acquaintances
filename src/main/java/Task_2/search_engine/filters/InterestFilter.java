package Task_2.search_engine.filters;

import Task_2.models.Interest;
import Task_2.models.User;
import Task_2.services.InterestsService;


public class InterestFilter
    extends BaseFilter
{
    private final InterestsService interestsService = new InterestsService();

    private final Interest interest;

    public InterestFilter(Interest interest) {
        this.interest = interest;
    }

    @Override
    public boolean match(User user) {
        for (Interest interest : user.getInterests()) {
            if (interestsService.equals(interest, this.interest)) {
                return true;
            }
        }
        return false;
    }
}
