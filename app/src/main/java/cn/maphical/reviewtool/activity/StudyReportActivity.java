package cn.maphical.reviewtool.activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class StudyReportActivity extends AppCompatActivity {

    private DatabaseTool dt;
    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_report);

        subjectName = getIntent().getExtras().getString("subjectName");
        dt = new DatabaseTool(this, "main_data", null, 1);

        TextView plannedTimeText = findViewById(R.id.text_study_report_planned_time);
        TextView realTimeText = findViewById(R.id.text_study_report_total_time);

        long totalTime = dt.getAllTasksTotalMinBySubject(new Subject(subjectName));
        long currentTime = dt.getStudyingTime(new Subject(subjectName));

        plannedTimeText.setText(totalTime + "分钟");
        realTimeText.setText((currentTime / 60 + 1) + "分钟");
    }

    public void onBackButtonPressed(View view) {
        finish();
    }
}
