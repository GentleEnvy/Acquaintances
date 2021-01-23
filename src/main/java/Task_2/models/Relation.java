package Task_2.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Relation {
    private int subjectId;
    private int objectId;
    private Set<RelationType> relationTypes;

    @SuppressWarnings("unused")  // for jackson
    private Relation() {}

    public Relation(User subject, User object) {
        this.subjectId = subject.getId();
        this.objectId = object.getId();
        relationTypes = new HashSet<>();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getObjectId() {
        return objectId;
    }

    public Set<RelationType> getRelationTypes() {
        return relationTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Relation relation = (Relation) o;
        return subjectId == relation.subjectId &&
            objectId == relation.objectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, objectId);
    }
}
