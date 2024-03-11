package com.app.todolist.controller;

import com.app.todolist.model.Task;
import com.app.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/task")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping(value = "/getAll")
    public List<Task> getAll(@RequestParam int id){
        return service.getAllTaskByUserId(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(@RequestBody @Valid Task task, BindingResult result){
        return service.create(task, result);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody @Valid Task task, BindingResult result){
        return service.update(id, task, result);
    }

    @PutMapping(value = "/updateState/{id}")
    public ResponseEntity<Object> updateState(@PathVariable int id, @RequestBody Task task){
        return service.updateState(id, task);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return service.delete(id);
    }

}
