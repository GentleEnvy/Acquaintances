package Task_2.models;

import java.util.Objects;
import java.util.Set;


public enum Interest {
    FOOTBALL("Футбол"),
    HOCKEY("Хоккей"),

    SPORT("Спорт", FOOTBALL, HOCKEY),

    CLASSIC_MUSIC("Классическая музыка"),
    POP_MUSIC("Поп музыка"),
    NIGHTCORE("Nightcore"),

    MUSIC("Музыка", CLASSIC_MUSIC, POP_MUSIC, NIGHTCORE),

    MATHEMATICS("Математика"),
    PHYSICS("Физика"),

    SCIENSE("Наука", MATHEMATICS, PHYSICS);

    public final String title;
    public final Set<Interest> subInterests;

    Interest(String title, Interest... subInterests) {
        this.title = title;
        this.subInterests = Set.of(subInterests);
    }

    @Override
    public String toString() {
        return title;
    }
}
