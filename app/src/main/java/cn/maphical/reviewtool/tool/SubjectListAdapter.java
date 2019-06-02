package cn.maphical.reviewtool.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Subject;

public class SubjectListAdapter extends BaseAdapter {

    private Context context;
    private Subject[] subjects;

    public SubjectListAdapter(Context context, Subject[] subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.fragment_subject_list_item, parent, false);
        TextView subjectTitleText = convertView.findViewById(R.id.text_subject_list_item_title);
        subjectTitleText.setText(subjects[position].getSubjectName());
        return convertView;
    }
}
