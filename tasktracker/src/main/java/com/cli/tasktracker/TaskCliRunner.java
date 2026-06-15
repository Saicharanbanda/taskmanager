package com.cli.tasktracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class TaskCliRunner implements CommandLineRunner {

    private final TaskService taskService;

    // Constructor: Spring automatically injects the TaskService here
    public TaskCliRunner(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Edge Case: If the user didn't type any commands
        if (args.length == 0) {
            printUsage();
            return;
        }

        // The first word typed is always the action command
        String command = args[0];

        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.err.println("Error: Please provide a description. Example: add \"Buy groceries\"");
                } else {
                    taskService.addTask(args[1]);
                }
                break;

            case "mark-in-progress":
                if (args.length < 2) {
                    System.err.println("Error: Missing Task ID");
                } else {
                    int id = Integer.parseInt(args[1]);
                    taskService.updateStatus(id, "in-progress");
                }
                break;

            case "mark-done":
                if (args.length < 2) {
                    System.err.println("Error: Missing Task ID");
                } else {
                    int id = Integer.parseInt(args[1]);
                    taskService.updateStatus(id, "done");
                }
                break;

            case "list":
                // If there's a second argument (like "done"), pass it. Otherwise, pass null.
                String filter = (args.length > 1) ? args[1] : null;
                taskService.listTasks(filter);
                break;

            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("  add \"[description]\"       - Add a new task");
        System.out.println("  mark-in-progress [id]     - Set task status to in-progress");
        System.out.println("  mark-done [id]            - Set task status to done");
        System.out.println("  list                      - List all tasks");
        System.out.println("  list [todo|in-progress|done] - List filtered tasks\n");
    }
}