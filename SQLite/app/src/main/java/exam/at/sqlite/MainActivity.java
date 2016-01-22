package exam.at.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StringBuffer detail = new StringBuffer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        student = (TextView)findViewById(R.id.student);
        DBHandler handler = new DBHandler(this);
        handler.deleteAllStudent();
        handler.addStudent(new Student(1, "AAA", "aaa"));
        handler.addStudent(new Student(2, "BBB", "bbb"));
        List<Student> studentList = handler.getAllStudent();
        for (Student student : studentList){
            detail.append("Id: "+student.getId()+" ,First Name: " + student.getFirstName() + " ,Last Name: " + student.getLastName()).append("\n");
        }
        student.setText(detail.toString());
    }
}
