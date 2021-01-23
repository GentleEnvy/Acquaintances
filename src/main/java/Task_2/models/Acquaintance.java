package Task_2.models;

import java.util.*;


public class Acquaintance {
    private final Map<Integer, User> users;
    private final Map<String, Relation> relations;

    public Acquaintance() {
        users = new HashMap<>();
        relations = new HashMap<>();
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<String, Relation> getRelations() {
        return relations;
    }
}
