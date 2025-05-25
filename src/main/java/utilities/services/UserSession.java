package utilities.services;

import db.User;

/**
 * Singleton service class for managing the current user's session.
 * Stores and retrieves the logged-in user's information in memory.
 */
public class UserSession {
    private static UserSession instance;

    private User currentUser;

    private UserSession() {}


    /**
     * Retrieves the singleton instance of the UserSession.
     *
     * @return the shared instance of UserSession
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Gets the current logged-in user.
     *
     * @return the current user object
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user the user to be set as the current session user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }


    /**
     * Clears the current session, effectively logging out the user.
     */
    public void clearSession() {
        this.currentUser = null;
    }
}
