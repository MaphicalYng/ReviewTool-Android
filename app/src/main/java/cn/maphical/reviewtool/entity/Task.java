package cn.maphical.reviewtool.entity;

public class Task {
    // 数据库中的任务ID
    private long id;
    // 任务科目
    private String subjectName;
    // 任务日期
    private String date;
    // 任务时间
    private String time;
    // 任务标题
    private String title;
    // 任务描述
    private String description;
    // 任务时长
    private int duringMin;
    // 是否是考试
    private boolean isExam;
    // 日程地点
    private String location;

    public Task(long id, String subjectName, String title, String description, String date, String time, int duringMin, boolean isExam, String location) {
        this.id = id;
        this.subjectName = subjectName;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.duringMin = duringMin;
        this.isExam = isExam;
        this.location = location;
    }
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getDuringMin() {
        return duringMin;
    }

    public boolean isExam() {
        return isExam;
    }

    public String getLocation() {
        return location;
    }
}
