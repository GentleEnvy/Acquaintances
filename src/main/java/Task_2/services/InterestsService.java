package Task_2.services;

import Task_2.models.Interest;

import java.util.HashSet;
import java.util.Set;


public class InterestsService {
    public Set<Interest> getRoots() {
        Set<Interest> roots = new HashSet<>();
        for (Interest interest : Interest.values()) {
            if (isRoot(interest)) {
                roots.add(interest);
            }
        }
        return roots;
    }

    public boolean isRoot(Interest interest) {
        for (Interest subInterest : Interest.values()) {
            if (subInterest != interest && containsSubInterest(subInterest, interest)) {
                return false;
            }
        }
        return true;
    }

    public Set<Interest> getAllSubInterests(Interest interest) {
        Set<Interest> subInterests = new HashSet<>();
        for (Interest subInterest : interest.subInterests) {
            if (subInterest.subInterests.isEmpty()) {
                subInterests.add(subInterest);
            } else {
                subInterests.addAll(getAllSubInterests(subInterest));
            }
        }
        return subInterests;
    }

    public boolean containsSubInterest(Interest targetInterest, Interest subInterest) {
        return getAllSubInterests(targetInterest).contains(subInterest);
    }

    public boolean equals(Interest interest1, Interest interest2) {
        if (interest1 == interest2) {
            return true;
        }
        return containsSubInterest(interest1, interest2) ||
            containsSubInterest(interest2, interest1);
    }
}
