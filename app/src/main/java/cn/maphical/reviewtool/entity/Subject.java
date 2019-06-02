package cn.maphical.reviewtool.entity;

/**
 * 用于表示一个科目实体。
 */
public class Subject {

    // 科目名称
    private String subjectName;

    public Subject(String name) {
        subjectName = name;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
