package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Task;

import java.util.ArrayList;
import java.util.List;

public class GetTasksThread extends DatabaseThread implements Runnable {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }

    @Override
    public void run() {
        tasks = super.getTasksThread();
    }
}
