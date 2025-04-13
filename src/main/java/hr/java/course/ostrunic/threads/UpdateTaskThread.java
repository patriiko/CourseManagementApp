package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Task;

public class UpdateTaskThread extends DatabaseThread implements Runnable {
    private final Task task;

    public UpdateTaskThread(Task updatedTask) {
        this.task = updatedTask;
    }

    @Override
    public void run() {
        super.updateTaskThread(task);
    }
}
