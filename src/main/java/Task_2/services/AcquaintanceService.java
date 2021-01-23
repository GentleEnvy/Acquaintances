package Task_2.services;

import Task_2.models.Acquaintance;
import Task_2.models.Relation;
import Task_2.models.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class AcquaintanceService {
    private static final RelationService relationService = new RelationService();
    private static final UserService userService = new UserService();

    public Collection<User> getUsers(Acquaintance acquaintance) {
        return acquaintance.getUsers().values();
    }

    User addUser(Acquaintance acquaintance) {
        var user = userService.create(getLastId(acquaintance) + 1);
        acquaintance.getUsers().put(user.getId(), user);
        return user;
    }

    public int getLastId(Acquaintance acquaintance) {
        int lastId = 0;
        for (var user : acquaintance.getUsers().values()) {
            if (user.getId() > lastId) {
                lastId = user.getId();
            }
        }
        return lastId;
    }

    void removeUser(Acquaintance acquaintance, User user) {
        for (var relation : getUserRelations(acquaintance, user)) {
            removeRelation(
                acquaintance,
                user,
                getUser(acquaintance, relation.getObjectId())
            );
        }
        acquaintance.getUsers().remove(user.getId());
    }

    public User getUser(Acquaintance acquaintance, int id) {
        return acquaintance.getUsers().get(id);
    }

    public Relation addRelation(Acquaintance acquaintance, User subject, User object) {
        if (subject == object) {
            return null;
        }

        var relation = getRelation(acquaintance, subject, object);
        if (relation != null) {
            return relation;
        }

        if (getUser(acquaintance, subject.getId()) == null) {
            acquaintance.getUsers().put(subject.getId(), subject);
        }
        if (getUser(acquaintance, object.getId()) == null) {
            acquaintance.getUsers().put(object.getId(), object);
        }

        relation = relationService.create(subject, object);
        acquaintance.getRelations().put(
            relationService.toString(subject, object),
            relation
        );
        return relation;
    }

    public void removeRelation(Acquaintance acquaintance, User subject, User object) {
        var relation = getRelation(acquaintance, subject, object);
        if (relation != null) {
            for (var relationType : relation.getRelationTypes()) {
                if (relationType.isMutual) {
                    relationService.removeRelationType(
                        relation,
                        acquaintance,
                        relationType
                    );
                }
            }
            acquaintance.getRelations().remove(relationService.toString(
                subject,
                object
            ));
        }
    }

    public Relation getRelation(Acquaintance acquaintance, User subject, User object) {
        return acquaintance.getRelations().get(relationService.toString(
            subject,
            object
        ));
    }

    public Set<Relation> getUserRelations(Acquaintance acquaintance, User subject) {
        Set<Relation> relations = new HashSet<>();
        for (var object : acquaintance.getUsers().values()) {
            var relation = getRelation(acquaintance, subject, object);
            if (relation != null) {
                relations.add(relation);
            }
        }
        return relations;
    }
}


