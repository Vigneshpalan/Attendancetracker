package com.example.attendancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "attendance.db";

    private static final String TABLE_SUBJECTS = "subjects";
    private static final String COLUMN_SUBJECT_ID = "subject_id";
    private static final String COLUMN_SUBJECT_NAME = "subject_name";

    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    public static final String COLUMN_SUBJECT_ID_FK = "subject_id_fk";
    public static final String COLUMN_ATTENDANCE_DATE = "attendance_date";
    public static final String COLUMN_ATTENDANCE_STATUS = "attendance_status";


    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "USERID";
    private static final String COLUMN_USERNAME = "USNAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";

    private static final String TABLE_TIMETABLE = "timetable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_PERIOD = "period";
    private static final String COLUMN_SUBJECT_T = "subject";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_USER_ID_FK = "currentuser";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createSubjectsTable = "CREATE TABLE " + TABLE_SUBJECTS +
                "(" +
                COLUMN_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SUBJECT_NAME + " TEXT," +
                COLUMN_USER_ID_FK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createSubjectsTable);


        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE +
                "(" +
                COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SUBJECT_ID_FK + " INTEGER," +
                COLUMN_ATTENDANCE_DATE + " TEXT," +
                COLUMN_ATTENDANCE_STATUS + " TEXT," +
                COLUMN_USER_ID_FK + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_SUBJECT_ID_FK + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_SUBJECT_ID + ")," +
                "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createAttendanceTable);



        String createUsersTable = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(createUsersTable);



        String createTimetableTable = "CREATE TABLE " + TABLE_TIMETABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY + " TEXT, " +
                COLUMN_PERIOD + " TEXT, " +
                COLUMN_SUBJECT_T + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_USER_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createTimetableTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    public boolean addSubject(String subjectName, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT_NAME, subjectName);
        values.put(COLUMN_USER_ID_FK, userId);

        long result = db.insert(TABLE_SUBJECTS, null, values);
        return result != -1;
    }

    public boolean addAttendance(String subjectId, String date, String status, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT_ID_FK, subjectId);
        values.put(COLUMN_ATTENDANCE_DATE, date);
        values.put(COLUMN_ATTENDANCE_STATUS, status);
        values.put(COLUMN_USER_ID_FK, userId);

        long result = db.insert(TABLE_ATTENDANCE, null, values);
        return result != -1;
    }


    public boolean deleteSubject(int subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_SUBJECTS, COLUMN_SUBJECT_ID + " = ?", new String[]{String.valueOf(subjectId)});
        return result > 0;
    }


    public List<Subject> getAllSubjects(Integer userId) {
        List<Subject> subjectList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUBJECTS+ " WHERE " + COLUMN_USER_ID_FK + " = " + userId, null);

        if (cursor.moveToFirst()) {
            do {
                int subjectId = cursor.getInt(cursor.getColumnIndex(COLUMN_SUBJECT_ID));
                String subjectName = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT_NAME));
                Subject subject = new Subject(subjectId, subjectName);
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return subjectList;
    }



    public List<Attendance> getAttendanceList(int userId, String subjectId){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Attendance> attendanceList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_SUBJECT_ID_FK + " = " + subjectId +
                " AND " + COLUMN_USER_ID_FK + " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int attendanceId = cursor.getInt(cursor.getColumnIndex(COLUMN_ATTENDANCE_ID));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_ATTENDANCE_DATE));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_ATTENDANCE_STATUS));

                // Create an instance of Attendance with the correct arguments
                attendanceList.add(new Attendance(Integer.parseInt(subjectId), date, status));

            } while (cursor.moveToNext());
        }

        cursor.close();
        return attendanceList;
    }



    // Method to retrieve attendance status for a subject and date
    public String getAttendanceStatus(long userId, String subject, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String status = "";

        String query = "SELECT " + COLUMN_ATTENDANCE_STATUS +
                " FROM " + TABLE_ATTENDANCE +
                " WHERE " + COLUMN_SUBJECT_ID_FK + " = " +
                "(SELECT " + COLUMN_SUBJECT_ID +
                " FROM " + TABLE_SUBJECTS +
                " WHERE " + COLUMN_SUBJECT_NAME + " = ?" +
                " AND " + COLUMN_USER_ID_FK + " = ?)" +
                " AND " + COLUMN_ATTENDANCE_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{subject, String.valueOf(userId), date});

        if (cursor != null && cursor.moveToFirst()) {
            int statusColumnIndex = cursor.getColumnIndex(COLUMN_ATTENDANCE_STATUS);
            status = cursor.getString(statusColumnIndex);
        }

        if (cursor != null) {
            cursor.close();
        }

        return status;
    }

    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ? AND " +
                COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        boolean isValidLogin = cursor.moveToFirst();

        cursor.close();
        return isValidLogin;
    }


    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public void insertTimetableData(int userId, String day, String period, String subject) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_PERIOD, period);
        values.put(COLUMN_SUBJECT_T, subject);

        db.insert(TABLE_TIMETABLE, null, values);
        db.close();
    }

    public List<TimetableItem> getTimetableData(int userId) {
        List<TimetableItem> timetableItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMETABLE +
                " WHERE " + COLUMN_USER_ID + " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
                    String period = cursor.getString(cursor.getColumnIndex(COLUMN_PERIOD));
                    String subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT_T));
                    String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));

                    TimetableItem item = new TimetableItem(id, day, period, subject, location);
                    timetableItems.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();

        return timetableItems;
    }

    public void clearTimetableData(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TIMETABLE, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
        }

        cursor.close();
        return userId;
    }



}
