package com.example.importexceldemo.Repositories;

import com.example.importexceldemo.Entities.StudentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {
    @Override
    List<StudentEntity> findAll();
}