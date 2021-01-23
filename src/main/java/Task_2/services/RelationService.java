package Task_2.services;

import Task_2.models.Acquaintance;
import Task_2.models.Relation;
import Task_2.models.RelationType;
import Task_2.models.User;
import javafx.util.Pair;


public class RelationService {
    private static final AcquaintanceService acquaintanceService =
        new AcquaintanceService();

    Relation create(User subject, User object) {
        if (subject == object) {
            return null;
        }
        return new Relation(subject, object);
    }

    public User getObject(Relation relation, Acquaintance acquaintance) {
        return acquaintanceService.getUser(acquaintance, relation.getObjectId());
    }

    public User getSubject(Relation relation, Acquaintance acquaintance) {
        return acquaintanceService.getUser(acquaintance, relation.getSubjectId());
    }

    public void addRelationType(
        Relation relation,
        Acquaintance acquaintance,
        RelationType relationType
    ) {
        relation.getRelationTypes().add(relationType);
        if (relationType.isMutual) {
            Relation mutualRelation = acquaintanceService.addRelation(
                acquaintance,
                getObject(relation, acquaintance),
                getSubject(relation, acquaintance)
            );
            mutualRelation.getRelationTypes().add(relationType);
        }
    }

    public void removeRelationType(
        Relation relation,
        Acquaintance acquaintance,
        RelationType relationType
    ) {
        relation.getRelationTypes().remove(relationType);
        if (relation.getRelationTypes().isEmpty()) {
            acquaintance.getRelations().remove(toString(relation));
        }
        if (relationType.isMutual) {
            Relation mutualRelation = acquaintanceService.getRelation(
                acquaintance,
                getObject(relation, acquaintance),
                getSubject(relation, acquaintance)
            );
            mutualRelation.getRelationTypes().remove(relationType);
            if (mutualRelation.getRelationTypes().isEmpty()) {
                acquaintance.getRelations().remove(toString(mutualRelation));
            }
        }
    }

    public String toString(User subject, User object) {
        return String.format("%d->%d", subject.getId(), object.getId());
    }

    public String toString(Relation relation) {
        return String.format("%d->%d", relation.getSubjectId(), relation.getObjectId());
    }
}
