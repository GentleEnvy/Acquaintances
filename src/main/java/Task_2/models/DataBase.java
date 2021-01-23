package Task_2.models;

import java.util.HashMap;
import java.util.Map;


public class DataBase {
    private final Map<Integer, Album> albums;
    private final Acquaintance acquaintance;

    public DataBase() {
        albums = new HashMap<>();
        acquaintance = new Acquaintance();
    }

    public Map<Integer, Album> getAlbums() {
        return albums;
    }

    public Acquaintance getAcquaintance() {
        return acquaintance;
    }
}
