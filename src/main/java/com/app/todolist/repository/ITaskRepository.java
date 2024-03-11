package com.app.todolist.repository;

import com.app.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Integer> {

      @Query(value = "SELECT * FROM tasks WHERE user_id = :id ORDER BY id ASC", nativeQuery = true)
      List<Task> getAllTaskByUserId(@Param("id") int id);


}
