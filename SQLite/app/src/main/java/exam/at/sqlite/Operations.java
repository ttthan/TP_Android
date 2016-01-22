package exam.at.sqlite;

import java.util.List;

/**
 * Created by TTTH on 1/2/2016.
 */
public interface Operations  {

    public void addStudent(Student student);

    public Student getStudent(int id);

    public List<Student> getAllStudent();

    public int getStudentCount();

    public int updateStudent(Student student);

    public void deleteStudent(Student student);

    public void deleteAllStudent();
}
