package pojos;

public class Account {
    private long id;
    private String name;
    private String token;
    private String esindex;

    // Default Constructor for Jackson
    public Account() {}

    public Account(String name, String token, String esindex) {
        this.name = name;
        this.token = token;
        this.esindex = esindex;
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

    public String getEsindex() {
        return esindex;
    }

    public String toString() {
        return getId() + ", " + getName() + ", " + getToken() + ", " + getEsindex();
    }
}
