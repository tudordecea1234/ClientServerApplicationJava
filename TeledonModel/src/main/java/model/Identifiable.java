package model;

public interface Identifiable<Tid> {
    Tid getId();
    void setId(Tid id);
}
