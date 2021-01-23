package Task_2.search_engine.filters;

import Task_2.models.User;


abstract public class BaseFilter {
    abstract public boolean match(User user);
}
