package com.cli.tasktracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    // The name of the file created in your project directory
    private final String FILE_PATH = "tasks.json";
    private final ObjectMapper objectMapper;

    // Constructor: Configures the JSON tool to understand LocalDateTime timestamps
    public TaskRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // Logic for loading tasks from the hard drive
    public List<Task> readTasks() {
        File file = new File(FILE_PATH);

        // If the file doesn't exist yet, return an empty list safely
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            // Read JSON text and translate it back into Java Task objects
            return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            System.err.println("Error reading JSON file. Starting with an empty list: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Logic for rewriting the JSON file with updated task lists
    public void saveTasks(List<Task> tasks) {
        try {
            // writerWithDefaultPrettyPrinter keeps the JSON file cleanly formatted and readable
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), tasks);
        } catch (IOException e) {
            System.err.println("Failed to save tasks to file: " + e.getMessage());
        }
    }
}