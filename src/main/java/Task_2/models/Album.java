package Task_2.models;

import java.util.HashMap;
import java.util.Map;


public class Album
    extends Indexed
{
    private Map<String, Photo> photos;
    private String avatarUrl;

    @SuppressWarnings("unused")  // for jackson
    private Album() {}

    public Album(User user) {
        super(user.getId());
        photos = new HashMap<>();
    }

    public Map<String, Photo> getPhotos() {
        return photos;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
