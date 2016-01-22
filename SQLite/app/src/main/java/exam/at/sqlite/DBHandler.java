package exam.at.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTTH on 1/2/2016.
 */
public class DBHandler extends SQLiteOpenHelper implements Operations {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "demoDB";
    private static final String TABLE_NAME = "student";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, student.getFirstName());
        values.put(KEY_LASTNAME, student.getLastName());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public Student getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        cursor.moveToFirst();
        Student student = new Student(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2));
        return student;
    }

    @Override
    public List<Student> getAllStudent() {
        List<Student> studentList = new ArrayList<Student>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do {
                Student student = new Student();
                student.setId(Integer.parseInt(cursor.getString(0)));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                studentList.add(student);
            }while (cursor.moveToNext());
        }
        return studentList;
    }

    @Override
    public int getStudentCount() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    @Override
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, student.getFirstName());
        values.put(KEY_LASTNAME, student.getLastName());
        return db.update(TABLE_NAME, values, KEY_ID + "= ?", new  String[] {String.valueOf(student.getId())});
    }

    @Override
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(student.getId()) });
        db.close();
    }


    @Override
    public void deleteAllStudent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


}
