package Task_2.models;


public enum RelationType {
    FRIEND("Друг", true),
    LOVER("Любовь", false),
    CLASSMATE("Одноклассник", true);

    public final String title;
    public final boolean isMutual;

    RelationType(String title, boolean isMutual) {
        this.title = title;
        this.isMutual = isMutual;
    }

    @Override
    public String toString() {
        return title;
    }
}
