package hr.java.course.ostrunic.genericsi;

import hr.java.course.ostrunic.enums.UserType;

import java.io.Serializable;
import java.sql.Timestamp;

public class DataChange <T extends Serializable, U extends Serializable> implements Serializable {
    private String objectName;
    private T oldValue;
    private U newValue;
    private Timestamp timestamp;
    private UserType userType;

    public DataChange(){}

    public DataChange(String objectName, T oldValue, U newValue, Timestamp timestamp, UserType userType) {
        this.objectName = objectName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timestamp = timestamp;
        this.userType = userType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }

    public U getNewValue() {
        return newValue;
    }

    public void setNewValue(U newValue) {
        this.newValue = newValue;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
