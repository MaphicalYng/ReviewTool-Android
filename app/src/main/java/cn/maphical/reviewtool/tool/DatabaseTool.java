package cn.maphical.reviewtool.tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.entity.Task;

public class DatabaseTool extends SQLiteOpenHelper {
    public DatabaseTool(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建任务表
        db.execSQL("CREATE TABLE task(" +
                "_id integer primary key autoincrement, " + // 0
                "subject_name varchar(64), " + // 1
                "title varchar(128), " + // 2
                "description text, " + // 3
                "date varchar(32), " + // 4
                "time varchar(32), " + // 5
                "during_min integer, " + // 6
                "is_exam integer, " + // 7
                "location varchar(256)" + // 8
                ");");
        // 创建科目表
        db.execSQL("CREATE TABLE subject(" +
                "_id integer primary key autoincrement, " + // 0
                "subject_name varchar(64), " +// 1
                "study_time integer" + // 2
                ");");
        // 创建用户表
        db.execSQL("CREATE TABLE user(" +
                "_id integer primary key autoincrement, " + // 0
                "username varchar(64), " + // 1
                "special varchar(64)" + // 2
                ");");
        // 创建锁表
        db.execSQL("CREATE TABLE lock(" +
                "_id integer, " +// 0
                "plan_lock integer, " +// 1
                "subject_lock integer, " +// 2
                "exam_lock integer" +// 3
                ");");

        // 初始化锁表
        db.execSQL("INSERT INTO lock values(0, 0, 0, 0);");

        /*
        // 插入测试数据
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"数据结构", "数据结构考试", "数据结构要考试了！", "2019-06-01", "15:00:00", "110", "1", "计算机学科楼101"});
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"西班牙语", "西班牙语考试", "西班牙语要考试了！", "2019-06-02", "17:00:00", "110", "1", "文科楼309"});
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"西班牙语", "复习西班牙语", "西班牙语要好好复习！", "2019-05-24", "17:00:00", "60", "0", "自习教室403"});
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 获得锁。
     */
    public HashMap<String, Boolean> getLock() {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询数据
        Cursor result = db.query("lock",
                null, null, null, null, null, null
        );
        result.moveToFirst();
        HashMap<String, Boolean> re = new HashMap<>();

        re.put("plan_lock", result.getInt(1) == 1);
        re.put("subject_lock", result.getInt(2) == 1);
        re.put("exam_lock", result.getInt(3) == 1);

        result.close();

        return re;
    }

    /**
     * 设置锁。
     * @param value 值。
     */
    public void setLock(String type, boolean value) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 修改数据
        if ("plan_lock".equals(type)) {
            db.execSQL("UPDATE lock SET plan_lock=? where _id=0;", new String[] {value? "1" : "0"});
        }
        if ("subject_lock".equals(type)) {
            db.execSQL("UPDATE lock SET subject_lock=? where _id=0;", new String[] {value? "1" : "0"});
        }
        if ("exam_lock".equals(type)) {
            db.execSQL("UPDATE lock SET exam_lock=? where _id=0;", new String[] {value? "1" : "0"});
        }
    }

    /**
     * 清空数据库。
     */
    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 删除任务表数据
        db.execSQL("DELETE FROM task;");
        // 删除科目表数据
        db.execSQL("DELETE FROM subject;");
        // 删除用户表数据
        db.execSQL("DELETE FROM user;");
    }

    /**
     * 查询所有的科目。
     * @return 科目列表或null。
     */
    public Subject[] getSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询
        Cursor result = db.query("subject",
                null, null, null, null, null, null
        );

        if (result.moveToFirst()) {
            Subject[] re = new Subject[result.getCount()];
            for (int i = 0; i < result.getCount(); i++) {
                result.move(i);
                re[i] = new Subject(result.getString(1));
            }
            result.close();
            return re;
        } else {
            result.close();
            return null;
        }
    }

    /**
     * 添加科目。
     * @param subject 科目对象。
     */
    public void addSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 判断是否已有相同科目
        Cursor result = db.query("subject",
                null, null, null, null, null, null
        );
        if (result.getCount() != 0) {
            result.close();
            return;
        }

        result.close();

        // 插入数据
        db.execSQL("INSERT INTO subject values(null, ?, 0);", new String[] {subject.getSubjectName()});
    }

    /**
     * 查询所有任务。
     * @return 任务列表或null。
     */
    public Task[] getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询任务信息
        Cursor result = db.query("task",
                null, null, null, null, null, null
        );

        int length = result.getCount();

        Task[] tasks = new Task[length];

        if (result.moveToFirst()) {
            int i = 0;
            do {
                tasks[i] = new Task(
                        result.getLong(0),
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getInt(6),
                        result.getInt(7) == 1,
                        result.getString(8)
                );
                i++;
            } while (result.moveToNext());

        } else {
            tasks = null;
        }
        result.close();

        return tasks;
    }

    /**
     * 添加用户。
     * @param username 昵称
     * @param special 专业
     */
    public void addUser(String username, String special) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 添加
        db.execSQL("INSERT INTO user values(null, ?, ?)", new String[] {username, special});
    }

    /**
     * 读取用户信息。
     * @return 用户名和专业。
     */
    public String[] readUser() {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询
        Cursor result = db.query("user",
                null, null, null, null, null, null
        );

        if (result.moveToFirst()) {
            String[] re = new String[2];
            result.move(0);
            re[0] = result.getString(1);
            re[1] = result.getString(2);
            result.close();
            return re;
        } else {
            result.close();
            return null;
        }
    }

    /**
     * 获得最近的一次考试。
     * @return 考试任务。
     */
    public Task getClosedExam() throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询
        Cursor result = db.query("task",
                null, "is_exam=?", new String[] {"1"}, null, null, null
        );

        if (result.moveToFirst()) {
            Date[] dates = new Date[result.getCount()];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            int i = 0;
            do {
                dates[i] = sdf.parse(result.getString(4) + " " + result.getString(5));
                i++;
            } while (result.moveToNext());
            Date closed = dates[0];
            int closedInt = 0;
            for (int j = 0; j < dates.length; j++) {
                if (dates[j].before(closed)) {
                    closed = dates[j];
                    closedInt = j;
                }
            }
            result.moveToPosition(closedInt);
            Task re = new Task(
                    result.getLong(0),
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getInt(6),
                    result.getInt(7) == 1,
                    result.getString(8)
            );
            result.close();
            return re;
        } else {
            result.close();
            return null;
        }
    }

    /**
     * 根据科目获得考试。
     * @param subject 科目对象。
     * @return 考试任务.
     */
    public Task getExamBySubject(Subject subject) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询数据
        Cursor result = db.query("task",
                null, "subject_name=? and is_exam=1", new String[] {subject.getSubjectName()}, null, null, null
        );

        if (result.moveToFirst()) {
            Task re = new Task(
                    result.getLong(0),
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getInt(6),
                    result.getInt(7) == 1,
                    result.getString(8)
            );
            result.close();
            return re;
        } else {
            result.close();
            return null;
        }
    }

    /**
     * 像数据库中插入任务。
     * @param task 任务对象。
     */
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 插入数据
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {
                task.getSubjectName(),
                task.getTitle(),
                task.getDescription(),
                task.getDate(),
                task.getTime(),
                String.valueOf(task.getDuringMin()),
                task.isExam() ? "1" : "0",
                task.getLocation()
        });
    }

    /**
     * 删除任务。
     * @param id 任务ID。
     */
    public void deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 查询任务所属科目
        Cursor result0 = db.query("task",
                null, "_id=?", new String[] {String.valueOf(id)}, null, null, null
        );
        result0.moveToFirst();
        String subjectName = result0.getString(1);
        result0.close();

        // 删除数据
        db.execSQL("DELETE FROM task where _id=?;", new String[] {String.valueOf(id)});

        // 删除已经没有任务的科目
        Cursor result = db.query("task",
                null, "subject_name=?", new String[] {subjectName}, null, null, null
        );

        if (!result.moveToFirst()) {
            // 表示没有相关任务，删除科目
            db.execSQL("DELETE FROM subject where subject_name=?;", new String[] {subjectName});
        }
        result.close();
    }

    /**
     * 获得某一科目的复习任务数量。
     * @param subject 科目对象。
     * @return 数量。
     */
    public int getReviewTaskCountBySubject(Subject subject) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询数据
        Cursor result = db.query("task",
                null, "subject_name=? and is_exam=0", new String[] {subject.getSubjectName()}, null, null, null
        );

        int count = result.getCount();
        result.close();
        return count;
    }

    /**
     * 设置学习时间。
     */
    public void setStudyingTime(Subject subject, long sec) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 插入数据
        db.execSQL("UPDATE subject SET study_time=? where subject_name=?;", new String[] {String.valueOf(sec), subject.getSubjectName()});
    }

    /**
     * 获得学习时间。
     * @param subject 科目。
     */
    public long getStudyingTime(Subject subject) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询数据
        Cursor result = db.query("subject",
                null, "subject_name=?", new String[] {subject.getSubjectName()}, null, null, null
        );

        if (result.moveToFirst()) {
            long re = result.getLong(2);
            result.close();
            return re;
        } else {
            result.close();
            return 0;
        }
    }

    /**
     * 获得所有任务的总时长。
     * @param subject 科目
     */
    public long getAllTasksTotalMinBySubject(Subject subject) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 读取数据
        Cursor result = db.query("task",
                null, "subject_name=? and is_exam=0", new String[] {subject.getSubjectName()}, null, null, null
        );

        if (result.moveToFirst()) {
            long total = 0;
            long[] item = new long[result.getCount()];
            for (int i = 0; i < result.getCount(); i++) {
                result.moveToPosition(i);
                item[i] = result.getLong(6);
            }
            for (long i : item) {
                total = total + i;
            }
            result.close();
            return total;
        } else {
            result.close();
            return -1;
        }
    }

    public void addTestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 插入测试数据
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"数据库系统概论", "数据库考试", "数据库要考试了！", "2019-06-01", "15:00:00", "110", "1", "计算机学科楼303"});
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"国际关系", "国际关系考试", "好好考试国际关系！", "2019-06-02", "17:00:00", "110", "1", "文科楼309"});
        db.execSQL("INSERT INTO task values(null, ?, ?, ?, ?, ?, ?, ?, ?);", new String[] {"经济学", "经济学复习", "经济学好好复习！", "2019-05-24", "17:00:00", "60", "0", "自习教室205"});
    }
}
