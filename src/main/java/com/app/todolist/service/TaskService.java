package com.app.todolist.service;

import com.app.todolist.model.Task;
import com.app.todolist.repository.ITaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository taskRepository;

    private HashMap<String, Object> data;


    // Obtener todas las tareas
    public List<Task> getAllTaskByUserId(int id) {
        return taskRepository.getAllTaskByUserId(id);
    }


    // Crear nueva tarea
    public ResponseEntity<Object> create(@Valid Task task, BindingResult result){
        data = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errors.add(error.getDefaultMessage());
            }
            data.put("message", "No se pudo crear la tarea.");
            data.put("error", errors);
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Task savedTask = taskRepository.save(task);

            data.put("message", "Tarea creada exitosamente");
            data.put("body", savedTask);
            return  new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("message", "No se pudo crear la tarea.");
            data.put("error", ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Editar tarea
    public ResponseEntity<Object> update(int id, @Valid Task task, BindingResult result){
        data = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errors.add(error.getDefaultMessage());
            }
            data.put("message", "No se pudo actualizar la tarea.");
            data.put("error", errors);
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Optional<Task> res = taskRepository.findById(id);

        if(!res.isPresent()){
            data.put("error", "Tarea no encontrada");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Task taskUpdate = res.get();
            taskUpdate.setDescription(task.getDescription());

            taskRepository.save(taskUpdate);

            data.put("message", "Tarea actualizada correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("message", "No se pudo actualizar la tarea.");
            data.put("error", ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Editar tarea (completada o pendiente)
    public ResponseEntity<Object> updateState(int id, Task task){
        data = new HashMap<>();

        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Optional<Task> res = taskRepository.findById(id);

        if(!res.isPresent()){
            data.put("error", "Tarea no encontrada");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Task taskUpdate = res.get();
            taskUpdate.setState(task.getState());

            taskRepository.save(taskUpdate);

            data.put("message", "Tarea actualizada correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("message", "No se pudo actualizar la tarea.");
            data.put("error", ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar tarea
    public ResponseEntity<Object> delete(int id){
        data = new HashMap<>();

        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Optional<Task> result = taskRepository.findById(id);

        if(!result.isPresent()){
            data.put("error", "Tarea no encontrada");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Task task = result.get();

            taskRepository.delete(task);

            data.put("message", "Tarea eliminada correctamente.");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("message", "Ocurrio un error al eliminar la tarea.");
            data.put("error", ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
