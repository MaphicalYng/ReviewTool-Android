package cn.maphical.reviewtool.fragment;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.maphical.reviewtool.R;

public class SubjectInfoPopupWindow extends PopupWindow {

    private Button reviewPlanButton;
    private Button studyReportButton;
    private TextView examTimeText;
    private TextView reviewTaskCountText;

    public Button getReviewPlanButton() {
        return reviewPlanButton;
    }

    public Button getStudyReportButton() {
        return studyReportButton;
    }

    public TextView getReviewTaskCountText() {
        return reviewTaskCountText;
    }

    public TextView getExamTimeText() {
        return examTimeText;
    }

    public SubjectInfoPopupWindow(FragmentActivity mContext, String title) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_subject_info, null);

        reviewPlanButton = view.findViewById(R.id.button_review_plan);
        studyReportButton = view.findViewById(R.id.button_study_report);
        examTimeText = view.findViewById(R.id.text_subject_exam_time);
        reviewTaskCountText = view.findViewById(R.id.text_subject_review_count);

        // 设置外部可点击
        this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(view);

        ((TextView) view.findViewById(R.id.text_subject_info_title)).setText(title);

        // 设置弹出窗体的宽和高
        /*
         * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        this.setHeight(460);
        this.setWidth((int) (d.getWidth() * 0.8));
        Resources resources = mContext.getResources();
        this.setBackgroundDrawable(resources.getDrawable(R.color.colorDialogBackground));

        // 设置弹出窗体可点击
        this.setFocusable(true);

    }
}