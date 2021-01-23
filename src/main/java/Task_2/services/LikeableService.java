package Task_2.services;

import Task_2.models.Like;
import Task_2.models.Likeable;
import Task_2.models.User;


abstract class LikeableService<LikeableType extends Likeable> {
    public final Like like(LikeableType likeable, User user) {
        var like = getLike(likeable, user);
        if (like == null) {
            like = new Like(user);
            likeable.getLikes().put(user.getId(), like);
        }
        return like;
    }

    public final void unlike(LikeableType likeable, User user) {
        likeable.getLikes().remove(user.getId());
    }

    public final Like getLike(LikeableType likeable, User user) {
        return likeable.getLikes().get(user.getId());
    }
}
