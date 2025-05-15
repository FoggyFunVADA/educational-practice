package model;

public class User {
    private static int counter = 1;
    private final int id;
    private final String name;

    public User(String name) {
        this.id = counter++;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
