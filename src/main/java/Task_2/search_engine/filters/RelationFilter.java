package Task_2.search_engine.filters;

import Task_2.models.Acquaintance;
import Task_2.models.Relation;
import Task_2.models.RelationType;
import Task_2.models.User;
import Task_2.services.AcquaintanceService;


public class RelationFilter
    extends BaseFilter
{
    private final AcquaintanceService acquaintanceService = new AcquaintanceService();

    private final Acquaintance acquaintances;
    private final User targetUser;
    private final RelationType relationType;

    public RelationFilter(
        Acquaintance acquaintances,
        User targetUser,
        RelationType relationType
    ) {
        this.acquaintances = acquaintances;
        this.targetUser = targetUser;
        this.relationType = relationType;
    }

    @Override
    public boolean match(User user) {
        Relation relation = acquaintanceService.getRelation(
            acquaintances,
            targetUser,
            user
        );
        if (relation == null) {
            return false;
        }
        return relation.getRelationTypes().contains(relationType);
    }
}
