package com.cli.tasktracker;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    // Constructor: Spring automatically passes the TaskRepository here
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    // 1. Logic to add a task (Works for both CLI and Web Form)
    public void addTask(String description) {
        List<Task> tasks = repository.readTasks();

        // Auto-increment ID logic
        int newId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;

        Task newTask = new Task(newId, description);
        tasks.add(newTask);
        repository.saveTasks(tasks);
        System.out.println("Task added successfully (ID: " + newId + ")");
    }

    // 2. Logic to change a task's status
    public void updateStatus(int id, String status) {
        List<Task> tasks = repository.readTasks();

        // Search for the task by ID using Java Streams
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        // Edge case: Task not found
        if (task == null) {
            System.err.println("Error: Task with ID " + id + " not found.");
            return;
        }

        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        repository.saveTasks(tasks);
        System.out.println("Task " + id + " updated to status: " + status);
    }

    // 3. Logic to list and filter tasks in the terminal
    public void listTasks(String filterStatus) {
        List<Task> tasks = repository.readTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n--- Task List ---");
        for (Task t : tasks) {
            // If no filter is provided, OR if the task status matches the filter
            if (filterStatus == null || t.getStatus().equalsIgnoreCase(filterStatus)) {
                System.out.println("[ID: " + t.getId() + "] " + t.getDescription() + " | Status: " + t.getStatus());
            }
        }
        System.out.println("-----------------\n");
    }

    // 4. New Logic: Returns the actual list of data to the Web Controller
    public List<Task> getAllTasks() {
        return repository.readTasks();
    }
}