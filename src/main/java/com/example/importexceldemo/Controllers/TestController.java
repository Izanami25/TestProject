package com.example.importexceldemo.Controllers;

import com.example.importexceldemo.Entities.StudentEntity;
import com.example.importexceldemo.Models.Student;
import com.example.importexceldemo.Repositories.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class TestController {
    @Autowired
    StudentRepository studentRepository;
    @GetMapping("/get-students")
    public List<Student> GetStudents(){
        List<Student> result = new ArrayList<>();
        List<StudentEntity> entities = studentRepository.findAll();
        if (entities != null && entities.size() > 0){
            entities.forEach(x->{
                Student item = new Student();
                item.id = x.id;
                item.studentNo = x.student_no;
                item.firstName = x.first_name;
                item.lastName = x.last_name;
                item.age = x.age;
                item.address = x.address;
                result.add(item);
            });
        }
        return result;
    }

    @PostMapping("/import-order-excel")
    public List<Student> importExcelFile(@RequestParam("file") MultipartFile files)throws IOException {
        List<Student> students = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        // Read student data form excel file sheet1.
        XSSFSheet worksheet = workbook.getSheetAt(0);
        for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
            if (index > 0) {
                XSSFRow row = worksheet.getRow(index);
                Student student = new Student();
                student.studentNo = getCellValue(row, 0);
                student.firstName = getCellValue(row, 1);
                student.lastName = getCellValue(row, 2);
                student.age = convertStringToInt(getCellValue(row, 3));
                student.address = getCellValue(row, 4);
                students.add(student);
            }
        }
        // Save to db.
        List<StudentEntity> entities = new ArrayList<>();
        if (students.size() > 0) {
            students.forEach(x->{
                StudentEntity entity = new StudentEntity();
                entity.student_no = x.studentNo;
                entity.first_name = x.firstName;
                entity.last_name = x.lastName;
                entity.age = x.age;
                entity.address = x.address;
                entities.add(entity);
            });
            studentRepository.saveAll(entities);
        }
        return students;
    }
    private int convertStringToInt(String str) {
        int result = 0;
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            return result;
        }
        result = Integer.parseInt(str);
        return result;
    }
    private String getCellValue(Row row, int cellNo) {
        DataFormatter formatter = new DataFormatter();
        Cell cell = row.getCell(cellNo);
        return formatter.formatCellValue(cell);
    }
}