package Task_2.services;

import Task_2.models.Album;
import Task_2.models.DataBase;
import Task_2.models.User;


public class DataBaseService {
    private static final AcquaintanceService acquaintanceService =
        new AcquaintanceService();
    private static final AlbumService albumService = new AlbumService();

    public DataBase create() {
        return new DataBase();
    }

    public User addUser(DataBase dataBase) {
        User user = acquaintanceService.addUser(dataBase.getAcquaintance());
        Album album = albumService.create(user);
        dataBase.getAlbums().put(user.getId(), album);
        return user;
    }

    public void removeUser(DataBase dataBase, User user) {
        acquaintanceService.removeUser(dataBase.getAcquaintance(), user);
        dataBase.getAlbums().remove(user.getId());
    }

    public Album getAlbum(DataBase dataBase, User user) {
        Album album = dataBase.getAlbums().get(user.getId());
        if (album == null) {
            album = albumService.create(user);
            dataBase.getAlbums().put(user.getId(), album);
        }
        return album;
    }
}
