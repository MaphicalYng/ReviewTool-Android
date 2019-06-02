package cn.maphical.reviewtool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class LoginActivity extends AppCompatActivity {

    private DatabaseTool dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 启动数据库
        dt = new DatabaseTool(this, "main_data", null, 1);
        if (dt.readUser() != null) {
            // 已经存在用户
            Intent intent = new Intent(this, cn.maphical.reviewtool.activity.MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 登录按钮触发方法，进行登陆。
     */
    public void onLoginButtonClick(View button) {
        // 将用户名和专业添加进入数据库中
        EditText usernameEdit = findViewById(R.id.login_edit_username);
        EditText specialEdit = findViewById(R.id.login_edit_special);

        String username = usernameEdit.getText().toString();
        String special = specialEdit.getText().toString();

        dt.addUser(username, special);

        // 启动主Activity
        Intent intent = new Intent(this, cn.maphical.reviewtool.activity.MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dt.close();
    }
}
