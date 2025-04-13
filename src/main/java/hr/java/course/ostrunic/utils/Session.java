package hr.java.course.ostrunic.utils;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.LongProperty;

public class Session {
    private static Session instance;
    private LongProperty loggedInUserId;

    private Session() {
        loggedInUserId = new SimpleLongProperty();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public long getLoggedInUserId() {
        return loggedInUserId.get();
    }

    public LongProperty loggedInUserIdProperty() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(Long loggedInUserId) {
        this.loggedInUserId.set(loggedInUserId);
    }
}
