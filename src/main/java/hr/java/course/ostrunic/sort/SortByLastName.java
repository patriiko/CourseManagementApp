package hr.java.course.ostrunic.sort;

import hr.java.course.ostrunic.abstracts.SystemUser;

import java.util.Comparator;

public class SortByLastName<T extends SystemUser> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return o1.getLastName().compareTo(o2.getLastName());
    }
}
