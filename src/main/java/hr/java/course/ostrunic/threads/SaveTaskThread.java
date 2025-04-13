package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Task;

public class SaveTaskThread extends DatabaseThread implements Runnable {
    private final Task task;

    public SaveTaskThread(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        super.saveTaskThread(task);
    }
}
