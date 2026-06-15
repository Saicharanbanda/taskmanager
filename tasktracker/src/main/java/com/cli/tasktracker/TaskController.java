package com.cli.tasktracker;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    // ADD THIS NEW METHOD BELOW:
    @PostMapping("/tasks/add")
    public String webAddTask(@RequestParam("description") String description) {
        // 1. Call your existing service logic to compute ID and save to file
        taskService.addTask(description);

        // 2. Tell the browser to instantly reload the /tasks page to see the new addition
        return "redirect:/tasks";
    }
}