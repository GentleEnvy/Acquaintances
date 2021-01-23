package Task_2.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class User
    extends Indexed
{
    private String lastName;
    private String firstName;
    private String patronymic;
    private Boolean male;
    private Date birthday;

    private Set<Interest> interests;

    @SuppressWarnings("unused")  // for jackson
    private User() {}

    public User(int id) {
        super(id);
        interests = new HashSet<>();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = "".equals(lastName) ? null : lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = "".equals(firstName) ? null : firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = "".equals(patronymic) ? null : patronymic;
    }

    public Boolean isMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    @Override
    public String toString() {
        return String.format(
            "%d: %s %s %s",
            getId(),
            lastName == null ? "" : lastName,
            firstName == null ? "" : firstName,
            patronymic == null ? "" : patronymic
        );
    }
}
