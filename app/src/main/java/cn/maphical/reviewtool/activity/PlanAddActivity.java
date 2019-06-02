package cn.maphical.reviewtool.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.entity.Task;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class PlanAddActivity extends AppCompatActivity {

    private int taskType;
    private DatabaseTool dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        dt = new DatabaseTool(this, "main_data", null, 1);

        // 设定单选按钮改变监听事件
        RadioGroup radioGroup = findViewById(R.id.radio_plan_add_type);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onTaskTypeRadioGroupChanged(checkedId);
            }
        });
        taskType = R.id.radio_plan_add_type_review;
    }

    /**
     * 返回回调。
     */
    public void onBackButtonPressed(View view) {
        finish();
    }

    /**
     * 提交按钮回调。
     */
    public void onSubmitButtonPressed(View view) {
        // 获得各种信息
        MaterialEditText editSubject = findViewById(R.id.edit_plan_add_subject);
        MaterialEditText editTitle = findViewById(R.id.edit_plan_add_title);
        MaterialEditText editDescription = findViewById(R.id.edit_plan_add_description);
        MaterialEditText editLocation = findViewById(R.id.edit_plan_add_location);
        MaterialEditText editStarttime = findViewById(R.id.edit_plan_add_start_time);
        MaterialEditText editDuringMin = findViewById(R.id.edit_plan_add_during_min);
        String subject = editSubject.getText().toString();
        String title = editTitle.getText().toString();
        String description = editDescription.getText().toString();
        String location = editLocation.getText().toString();
        String starttime = editStarttime.getText().toString();
        String duringMin = editDuringMin.getText().toString();

        String date = starttime.split(" ")[0];
        String time = starttime.split(" ")[1];

        // 构建对象
        Task task = new Task(
                -1,
                subject,
                title,
                description,
                date,
                time,
                Integer.parseInt(duringMin),
                taskType == R.id.radio_plan_add_type_exam,
                location
        );

        // 存入数据库
        dt.addSubject(new Subject(subject));
        dt.addTask(task);
        // 添加锁
        dt.setLock("plan_lock", true);
        dt.setLock("subject_lock", true);
        dt.setLock("exam_lock", true);
        finish();
    }

    /**
     * 单选按钮组改变时回调方法。
     */
    private void onTaskTypeRadioGroupChanged(int checkedId) {
        taskType = checkedId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dt.close();
    }
}
