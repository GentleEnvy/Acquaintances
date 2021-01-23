package Task_2.models;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Photo
    implements Likeable
{
    private String url;
    private Map<Integer, Like> likes;

    @SuppressWarnings("unused")  // for jackson
    private Photo() {}

    public Photo(String url) {
        this.url = url;
        likes = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Map<Integer, Like> getLikes() {
        return likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }

        Photo photo = (Photo) o;

        return getUrl().equals(photo.getUrl());
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }
}
