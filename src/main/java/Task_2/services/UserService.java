package Task_2.services;


import Task_2.models.*;

import java.util.Date;


public class UserService {
    private static final InterestsService interestsService = new InterestsService();

    User create(int id) {
        return new User(id);
    }

    public Integer getAge(User user) {
        if (user.getBirthday() == null) {
            return null;
        }
        Date now = new Date();
        long timeBetween = now.getTime() - user.getBirthday().getTime();
        double yearsBetween = timeBetween / 3.15576e+10;
        return  (int) Math.floor(yearsBetween);
    }

    public void addInterest(User user, Interest interest) {
        user.getInterests().add(interest);
        user.getInterests().addAll(interestsService.getAllSubInterests(interest));
    }

    public void removeInterest(User user, Interest interest) {
        user.getInterests().remove(interest);
        user.getInterests().removeAll(interestsService.getAllSubInterests(interest));
    }
}
