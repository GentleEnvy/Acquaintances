package Task_2.search_engine.filters;

import Task_2.models.User;

import java.util.regex.Pattern;


public class NameFilter extends BaseFilter {
    private static final Pattern ANY_STRING = Pattern.compile(".*");
    
    private final Pattern lastName;
    private final Pattern firstName;
    private final Pattern patronymic;

    public NameFilter(String lastName, String firstName, String patronymic) {
        this.lastName = createPattern(lastName);
        this.firstName = createPattern(firstName);
        this.patronymic = createPattern(patronymic);
    }

    @Override
    public boolean match(User user) {
        String lName = user.getLastName();
        String fName = user.getFirstName();
        String patron = user.getPatronymic();
        return (lName == null || lastName.matcher(lName.toLowerCase()).matches()) &&
            (fName == null || firstName.matcher(fName.toLowerCase()).matches()) &&
            (patron == null || patronymic.matcher(patron.toLowerCase()).matches());
    }

    private static Pattern createPattern(String string) {
        if (string == null || "".equals(string)) {
            return ANY_STRING;
        }
        return Pattern.compile(String.format(
                "%s%s%s",
                ANY_STRING.pattern(),
                string.toLowerCase(),
                ANY_STRING.pattern()
        ));
    }
}
