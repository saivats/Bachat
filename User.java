/**
 * User class stores basic profile information for the student.
 */
public class User {
    private String name;
    private String city;

    /**
     * Creates a new user object.
     *
     * @param name user name
     * @param city user city
     */
    public User(String name, String city) {
        this.name = name;
        this.city = city;
    }

    /**
     * Returns the user's name.
     *
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name new user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user's city.
     *
     * @return city of the user
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the user's city.
     *
     * @param city new city name
     */
    public void setCity(String city) {
        this.city = city;
    }
}
