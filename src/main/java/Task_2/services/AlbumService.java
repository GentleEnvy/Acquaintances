package Task_2.services;

import Task_2.models.Album;
import Task_2.models.Photo;
import Task_2.models.User;


public class AlbumService {
    private static final PhotoService photoService = new PhotoService();

    Album create(User user) {
        return new Album(user);
    }

    public Photo getPhoto(Album album, String url) {
        return album.getPhotos().get(url);
    }

    public Photo addPhoto(Album album, String url) {
        if (album.getPhotos().containsKey(url)) {
            return getPhoto(album, url);
        }
        var photo = photoService.create(url);
        album.getPhotos().put(photo.getUrl(), photo);
        return photo;
    }

    public void removePhoto(Album album, Photo photo) {
        album.getPhotos().remove(photo.getUrl());
        if (photo.getUrl().equals(album.getAvatarUrl())) {
            album.setAvatarUrl(null);
        }
    }

    public void setAvatar(Album album, String avatarUrl) {
        if (getPhoto(album, avatarUrl) == null) {
            addPhoto(album, avatarUrl);
        }
        album.setAvatarUrl(avatarUrl);
    }

    public Photo getAvatar(Album album) {
        if (album.getAvatarUrl() == null) {
            return null;
        }
        return getPhoto(album, album.getAvatarUrl());
    }
}
