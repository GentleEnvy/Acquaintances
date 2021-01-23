package Task_2.models;


public class Like
    extends Indexed
{
    @SuppressWarnings("unused")  // for jackson
    private Like() {}

    public Like(User user) {
        super(user.getId());
    }
}
