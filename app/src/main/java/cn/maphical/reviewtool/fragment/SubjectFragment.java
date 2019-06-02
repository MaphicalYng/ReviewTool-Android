package cn.maphical.reviewtool.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;
import cn.maphical.reviewtool.entity.Task;
import cn.maphical.reviewtool.tool.DatabaseTool;
import cn.maphical.reviewtool.tool.SubjectListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectFragment extends Fragment {

    private DatabaseTool dt;
    private Subject[] subjects;
    private ConstraintLayout layout;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubjectFragment.
     */
    public static SubjectFragment newInstance() {
        SubjectFragment fragment = new SubjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        final ListView list = view.findViewById(R.id.list_view_subject);
        // 读取科目表
        subjects = dt.getSubjects();
        if (subjects != null) {
            // 有科目，隐藏提示信息
            view.findViewById(R.id.text_subject_no_data).setVisibility(View.INVISIBLE);
        } else {
            // 没有科目，不加载列表
            return view;
        }
        layout = view.findViewById(R.id.layout_subject_main);
        SubjectListAdapter sla = new SubjectListAdapter(getActivity(), subjects);
        list.setAdapter(sla);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SubjectInfoPopupWindow sip = new SubjectInfoPopupWindow(getActivity(), subjects[position].getSubjectName());
                sip.getReviewPlanButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 打开复习计划页面
                    }
                });
                sip.getStudyReportButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 打开学习报告页面
                        sip.dismiss();
                        Intent intent = new Intent(getActivity(), cn.maphical.reviewtool.activity.StudyReportActivity.class);
                        intent.putExtra("subjectName", subjects[position].getSubjectName());
                        startActivity(intent);
                    }
                });
                // 获得考试任务
                Task exam = dt.getExamBySubject(subjects[position]);
                if (exam == null) {
                    sip.getExamTimeText().setText("未知");
                } else {
                    sip.getExamTimeText().setText(exam.getDate() + " " + exam.getTime());
                }
                sip.getReviewTaskCountText().setText(dt.getReviewTaskCountBySubject(subjects[position]) + "个事项");
                sip.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
        return view;
    }
}
