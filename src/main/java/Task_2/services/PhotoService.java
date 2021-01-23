package Task_2.services;

import Task_2.models.Photo;

import java.net.MalformedURLException;
import java.net.URL;


public class PhotoService
    extends LikeableService<Photo>
{
    Photo create(String url) {
        try {
            new URL(url);
            return new Photo(url);
        } catch (MalformedURLException __) {
            return null;
        }
    }
}
