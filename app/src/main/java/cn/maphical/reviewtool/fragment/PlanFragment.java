package cn.maphical.reviewtool.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.maphical.reviewtool.R;
import cn.maphical.reviewtool.entity.Task;
import cn.maphical.reviewtool.tool.DatabaseTool;

/**
 * 日程Fragment。
 */
public class PlanFragment extends Fragment implements CalendarPickerController {

    private DatabaseTool dt;

    public PlanFragment() {
        // Required empty public constructor
    }

    /**
     * 新建Fragment。
     */
    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
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
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        AgendaCalendarView acv = view.findViewById(R.id.agenda_calendar_view);

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();
        mockList(eventList);

        acv.init(eventList, minDate, maxDate, Locale.getDefault(), this);

        return view;
    }

    /**
     * 读取并初始化日历事件。
     * @param eventList 日历事件列表。
     */
    private void mockList(List<CalendarEvent> eventList) {
        Task[] result = dt.getAllTasks();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (result != null) {

            for (Task task: result) {
                Date date;
                try {
                    date = sdf.parse(task.getDate() + " " + task.getTime());
                } catch (ParseException e) {
                    Toast.makeText(getActivity(), "读取数据异常。", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar startTime = Calendar.getInstance();
                startTime.setTime(date);

                Calendar endTime = Calendar.getInstance();
                endTime.setTime(date);
                endTime.add(Calendar.MINUTE, task.getDuringMin());

                BaseCalendarEvent event = new BaseCalendarEvent(
                        task.getId(),
                        task.isExam() ? R.color.colorExamEvent : R.color.colorReviewEvent,
                        task.getTitle(),
                        task.getDescription(),
                        task.getLocation(),
                        startTime.getTimeInMillis(),
                        endTime.getTimeInMillis(),
                        0,
                        ""
                );

                eventList.add(event);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dt.close();
    }

    // 日历事件监听器
    // ==============================================================================
    @Override
    public void onDaySelected(DayItem dayItem) {

    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        JSONObject data = new JSONObject();
        BaseCalendarEvent baseEvent = (BaseCalendarEvent) event;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            data.put("id", baseEvent.getId());
            data.put("title", baseEvent.getTitle());
            data.put("des", baseEvent.getDescription());
            data.put("startTime", sdf.format(baseEvent.getStartTime().getTime()));
            data.put("endTime", sdf.format(baseEvent.getEndTime().getTime()));
            data.put("location", baseEvent.getLocation());
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "读取数据异常。", Toast.LENGTH_SHORT).show();
            return;
        } catch (NullPointerException e) {
            return;
        }
        Intent intent = new Intent(getActivity(), cn.maphical.reviewtool.activity.EventInfoActivity.class);
        intent.putExtra("data", data.toString());
        startActivity(intent);
    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }
}
