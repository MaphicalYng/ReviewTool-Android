package cn.maphical.reviewtool.activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.fragment.PlanFragment;
import cn.maphical.reviewtool.fragment.SubjectFragment;
import cn.maphical.reviewtool.fragment.WaitExamFragment;
import cn.maphical.reviewtool.tool.DatabaseTool;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseTool dt;

    private Fragment currentFragment = null;
    private Fragment waitExamFragment = null;
    private Fragment planFragment = null;
    private Fragment subjectFragment = null;

    private ImageView addPlanButton;
    private TextView titleText;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // 更改标题
                    titleText.setText("待考科目");
                    // 隐藏日程新建和刷新按钮
                    addPlanButton.setVisibility(View.INVISIBLE);
                    // 切换到主页
                    if (waitExamFragment == null) {
                        waitExamFragment = WaitExamFragment.newInstance();
                    }
                    if (dt.getLock().get("exam_lock")) {
                        onRenewExamFragment();
                        return true;
                    }
                    if (waitExamFragment.isAdded()) {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .show(waitExamFragment)
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .add(R.id.fragment_container, waitExamFragment)
                                .commit();
                    }
                    currentFragment = waitExamFragment;
                    return true;
                case R.id.navigation_plan:
                    // 更改标题
                    titleText.setText("学习日程");
                    // 显示日程新建和刷新按钮
                    addPlanButton.setVisibility(View.VISIBLE);
                    // 切换到日历
                    // 强制刷新，新建Fragment
                    if (planFragment == null) {
                        // 首次加载日历
                        planFragment = PlanFragment.newInstance();
                    }
                    if (dt.getLock().get("plan_lock")) {
                        onRenewPlanFragment();
                        return true;
                    }
                    if (planFragment.isAdded()) {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .show(planFragment)
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .add(R.id.fragment_container, planFragment)
                                .commit();
                    }
                    currentFragment = planFragment;
                    return true;
                case R.id.navigation_subject:
                    // 更改标题
                    titleText.setText("复习科目");
                    // 切换到科目
                    // 隐藏日程新建和刷新按钮
                    addPlanButton.setVisibility(View.INVISIBLE);
                    if (subjectFragment == null) {
                        subjectFragment = SubjectFragment.newInstance();
                    }
                    if (dt.getLock().get("subject_lock")) {
                        onRenewSubjectFragment();
                        return true;
                    }
                    if (subjectFragment.isAdded()) {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .show(subjectFragment)
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .hide(currentFragment)
                                .add(R.id.fragment_container, subjectFragment)
                                .commit();
                    }
                    currentFragment = subjectFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置抽屉
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // 设置底部导航栏
        BottomNavigationView navView = findViewById(R.id.nav_bottom_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 设置用户名和专业
        dt = new DatabaseTool(this, "main_data", null, 1);
        String[] info = dt.readUser();

        // 更新抽屉文字
        View navHeaderView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null, false);
        ((TextView)navHeaderView.findViewById(R.id.nav_username)).setText(info[0]);
        ((TextView)navHeaderView.findViewById(R.id.nav_subject)).setText(info[1]);
        navigationView.addHeaderView(navHeaderView);

        // 隐藏日程添加和刷新按钮
        addPlanButton = findViewById(R.id.image_plan_add);
        addPlanButton.setVisibility(View.INVISIBLE);
        titleText = findViewById(R.id.text_main_title);

        // 加载Fragment
        waitExamFragment = WaitExamFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, waitExamFragment)
                .commit();
        currentFragment = waitExamFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dt.close();
    }

    // 返回按钮处理，关闭抽屉
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 日历界面的添加按钮触发事件。
     */
    public void onPlanAddButtonPressed(View view) {
        // 启动新增日程Activity
        Intent intent = new Intent(this, cn.maphical.reviewtool.activity.PlanAddActivity.class);
        startActivity(intent);
    }

    // 抽屉菜单选中处理
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu_setting) {
            // 启动SettingActivity
        } else if (id == R.id.nav_menu_about) {
            // 启动AboutActivity
        } else if (id == R.id.nav_menu_update) {
            // 检查更新
        } else if (id == R.id.nav_menu_logout) {
            // 退出登录
            // 清理数据库数据
            dt.clearDB();
            //dt.addTestData();
            Intent intent = new Intent(this, cn.maphical.reviewtool.activity.LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 刷新日历回调。
     */
    public void onRenewPlanFragment() {
        // 重新创建
        Fragment tempFragment = PlanFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .remove(planFragment)
                .add(R.id.fragment_container, tempFragment)
                .commit();
        planFragment = tempFragment;
        currentFragment = planFragment;
        dt.setLock("plan_lock", false);
    }

    /**
     * 刷新科目回调。
     */
    public void onRenewSubjectFragment() {
        // 重新创建
        Fragment tempFragment = SubjectFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .remove(subjectFragment)
                .add(R.id.fragment_container, tempFragment)
                .commit();
        subjectFragment = tempFragment;
        currentFragment = subjectFragment;
        dt.setLock("subject_lock", false);
    }

    /**
     * 刷新考试回调。
     */
    public void onRenewExamFragment() {
        // 重新创建
        Fragment tempFragment = WaitExamFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .remove(waitExamFragment)
                .add(R.id.fragment_container, tempFragment)
                .commit();
        waitExamFragment = tempFragment;
        currentFragment = waitExamFragment;
        dt.setLock("exam_lock", false);
    }
}
