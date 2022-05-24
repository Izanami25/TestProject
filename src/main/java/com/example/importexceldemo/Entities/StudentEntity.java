package com.example.importexceldemo.Entities;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String student_no;
    public String first_name;
    public String last_name;
    public int age;
    public String address;
}