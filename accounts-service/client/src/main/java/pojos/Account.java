package pojos;

public class Account {
    private long id;
    private String name;
    private String token;
    private String esIndex;

    // Default Constructor for Jackson
    public Account() {}

    public Account(String name, String token, String esIndex) {
        this.name = name;
        this.token = token;
        this.esIndex = esIndex;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getEsIndex() {
        return esIndex;
    }

    public String toString() {
        return getId() + ", " + getName() + ", " + getToken() + ", " + getEsIndex();
    }
}
