package Task_2.models;


abstract public class Indexed {
    private int id;

    protected Indexed() {}

    public Indexed(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Indexed object = (Indexed) o;
        return id == object.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
