package cn.maphical.reviewtool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class StudyingActivity extends AppCompatActivity {

    private AppCompatActivity activity;

    private String subjectName;
    private long seconds;

    private DatabaseTool dt;

    private Thread timeThread;
    private TextView timeText;
    private boolean ifExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studying);
        activity = this;

        // 获得科目名称
        subjectName = getIntent().getExtras().getString("subjectName");

        timeText = findViewById(R.id.text_studying_time);

        dt = new DatabaseTool(this, "main_data", null, 1);

        // 启动线程计算时间
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                seconds = 0;
                while (!ifExit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds++;
                    final long hour = seconds / 3600;
                    final long min = (seconds % 3600) / 60;
                    final long sec = seconds % 3600 % 60;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeText.setText(hour + ":" + min + ":" + sec);
                        }
                    });
                }
            }
        });
        timeThread.start();
    }

    /**
     * 停止按钮回调。
     */
    public void onStopButtonPressed(View view) {
        // 记录学习时间
        long current = dt.getStudyingTime(new Subject(subjectName));
        dt.setStudyingTime(new Subject(subjectName), current + seconds);
        // 返回
        ifExit = true;
        finish();
    }
}
