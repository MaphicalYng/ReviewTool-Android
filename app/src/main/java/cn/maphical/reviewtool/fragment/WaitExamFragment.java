package cn.maphical.reviewtool.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Task;
import cn.maphical.reviewtool.tool.DatabaseTool;

/**
 * 待考科目Fragment。
 */
public class WaitExamFragment extends Fragment {

    private DatabaseTool dt;
    private String subjectName;

    public WaitExamFragment() {
        // Required empty public constructor
    }

    /**
     * 新建Fragment。
     */
    public static WaitExamFragment newInstance() {
        WaitExamFragment fragment = new WaitExamFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dt = new DatabaseTool(getActivity(), "main_data", null, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wait_exam, container, false);

        TextView subjectText = view.findViewById(R.id.text_subject_exam);
        TextView remainTimeText = view.findViewById(R.id.text_remain_time);

        Task closedExam = null;
        try {
            closedExam = dt.getClosedExam();
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "获取数据异常。", Toast.LENGTH_SHORT).show();
        }
        if (closedExam != null) {
            subjectName = closedExam.getSubjectName();
            // 已有科目
            subjectText.setText(closedExam.getSubjectName());
            Date current = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date eventTime;
            try {
                eventTime = sdf.parse(closedExam.getDate() + " " + closedExam.getTime());
            } catch (ParseException e) {
                Toast.makeText(getActivity(), "读取数据失败。", Toast.LENGTH_SHORT).show();
                return view;
            }
            long duringSeconds = (eventTime.getTime() - current.getTime()) / 1000;
            long dayCount = duringSeconds / (3600 * 24);
            long hourCount = (duringSeconds % (3600 * 24)) / 3600;
            long minuteCount = (duringSeconds % (3600 * 24) % 3600) / 60;
            long secondCount = (duringSeconds % (3600 * 24) % 3600) % 60;

            remainTimeText.setText(dayCount + "天" + hourCount + "小时" + minuteCount + "分" + secondCount + "秒");
        }
        ImageView mainCircleButton = view.findViewById(R.id.main_circle_button);
        mainCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartStudyButtonPressed();
            }
        });
        Button otherSubjectsButton = view.findViewById(R.id.button_other_subject);
        otherSubjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOtherSubjectsButtonPressed();
            }
        });
        return view;
    }

    /**
     * 开始学习按钮按下。
     */
    public void onStartStudyButtonPressed() {
        Intent intent = new Intent(getActivity(), cn.maphical.reviewtool.activity.StudyingActivity.class);
        intent.putExtra("subjectName", subjectName);
        startActivity(intent);
    }

    /**
     * 其他科目按钮按下。
     */
    public void onOtherSubjectsButtonPressed() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
