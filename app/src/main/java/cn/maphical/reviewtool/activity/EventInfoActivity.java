package cn.maphical.reviewtool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class EventInfoActivity extends AppCompatActivity {

    private long id;
    private String subjectName;
    private DatabaseTool dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        dt = new DatabaseTool(this, "main_data", null, 1);

        String jsonString = getIntent().getExtras().getString("data");
        String title;
        String description;
        String startTime;
        String endTime;
        String location;
        try {
            JSONObject data = new JSONObject(jsonString);
            id = data.getLong("id");
            title = data.getString("title");
            description = data.getString("des");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
            location = data.getString("location");
        } catch (JSONException e) {
            Toast.makeText(this, "数据读取异常。", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView titleText = findViewById(R.id.text_event_title);
        TextView descriptionText = findViewById(R.id.text_event_description);
        TextView startTimeText = findViewById(R.id.text_event_start_time);
        TextView endTimeText = findViewById(R.id.text_event_end_time);
        TextView locationText = findViewById(R.id.text_event_location);
        titleText.setText(title);
        descriptionText.setText(description);
        startTimeText.setText(startTime);
        endTimeText.setText(endTime);
        locationText.setText(location);
    }

    /**
     * 返回回调。
     */
    public void onBackButtonPressed(View view) {
        finish();
    }

    /**
     * 更多按钮回调。
     */
    public void onMoreButtonPressed(View view) {
        // 弹出菜单
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.event_info_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_event_info_delete:
                        // 删除日程
                        dt.deleteTask(id);
                        // 添加锁
                        dt.setLock("plan_lock", true);
                        dt.setLock("subject_lock", true);
                        dt.setLock("exam_lock", true);
                        finish();
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dt.close();
    }
}
