package com.Ongle.ong.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ong.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.Ongle.ong.calendar.OngCalendarEdit.alarmshow;
import static com.Ongle.ong.calendar.OngCalendarEdit.yoyo;
import static java.lang.Integer.parseInt;


public class OngCalendar extends Fragment  {

    View view;
    GridView mGridView;
    //calendarmenu
    //calendar
    DateAdapter adapter;
    ArrayList<CalData> arrData;
    Calendar mCalToday;
    Calendar mCal;
    TextView maintext,tvY;
    //달력의 년,월,일
    int thisYear, thisMonth, thisStartDay,thisday,thisdays, y,m,d;
    //다음달 이전달 버튼
    Button prev,next;
    //날짜 넣은 (달력 선택 날짜 저장)
    String ToDodate;
    //달력스케줄
    static HelperCalendar cdb;
    static HelperAlram acb;
    ListView listView;
    ListViewAdapter ScheduleViewAdapter;
    List<ScheduleItem> Schedule;
    EditText addText;
    View dialogView;
    //요일
    Calendar dateCal;
    public static String todayal;
    public static List<ScheduleItem> alarm;
    public static OngCalendar CONTEXT;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        CONTEXT = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.activity_ong_calendar,null);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        prev = view.findViewById(R.id.prev);
        next= view.findViewById(R.id.next);


        maintext = view.findViewById(R.id.maintext);
        mCalToday = Calendar.getInstance();
        mCal = Calendar.getInstance();

        thisYear = mCal.get(Calendar.YEAR);
        thisMonth = mCal.get(Calendar.MONTH) + 1;
        thisday= mCal.get(Calendar.DATE);
        thisdays=mCal.get(Calendar.DAY_OF_WEEK);
        String datS= yoyo(thisdays);
        dateCal = Calendar.getInstance();
        //날짜 넣은 (달력 선택 날짜 저장)
        todayal=thisYear + "/" + thisMonth + "/" +thisday+"/"+datS;
        ToDodate = thisYear + "/" + thisMonth + "/" +thisday+"/"+datS;
        mGridView =view.findViewById(R.id.calGrid);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //month 날짜 표기 텍스트에 사용함.
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final  SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final  SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //오늘 날짜
        y = parseInt(curYearFormat.format(date));
        m = parseInt(curMonthFormat.format(date));
        d = parseInt(curDayFormat.format(date));
        tvY = view.findViewById(R.id.tvY);

        //10월 이전에는 0을 붙여주긔 10일 이전에는 0을 붙여주긔
        //ex)01월 01일
        setCalendarDate(thisMonth);
        if(thisMonth >= 10) {
            if(d >= 10){
                maintext.setText(thisYear + "." + thisMonth);
                tvY.setText(thisMonth + "월 " + d + "일 ");}
            else {
                maintext.setText(thisYear + "." + thisMonth);
                tvY.setText(thisMonth + "월 0" + d + "일 ");}
        }else {
            if(d >= 10){
                maintext.setText(thisYear + ".0" + thisMonth);
                tvY.setText("0"+thisMonth + "월 " + d + "일 ");
            }else {
                maintext.setText(thisYear + ".0" + thisMonth);
                tvY.setText("0"+thisMonth + "월 0" + d + "일 ");}

        }
        //이전달스
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisMonth > 1) {
                    thisMonth--;
                    //이전달로
                    setCalendarDate(thisMonth);
                } else {
                    thisMonth = 0;
                    //1월 일때 이전 달 누르면 0으로 준다.
                    setCalendarDate(thisMonth);
                }
            }
        });
        //다음달스
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisMonth < 12) {
                    thisMonth++;
                    setCalendarDate(thisMonth);
                } else {
                    thisMonth = 13;
                    setCalendarDate(thisMonth);
                }
            }
        });

        //convertView.setBackgroundColor(getResources().getColor(R.color.main));
        //클릭 시
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrData.get(i) != null) {
                    if (thisMonth >= 10 && arrData.get(i).getDay() >= 10) {
                        tvY.setText(thisMonth + "월 " + arrData.get(i).getDay() + "일 ");
                        ToDodate = thisYear + "/" + thisMonth + "/" + arrData.get(i).getDay();
                    } else if (thisMonth >= 10 && arrData.get(i).getDay() < 10) {
                        ToDodate = thisYear + "/" + thisMonth + "/" + arrData.get(i).getDay();
                        tvY.setText(thisMonth + "월 0" + arrData.get(i).getDay() + "일 ");
                    } else if (thisMonth < 10 && arrData.get(i).getDay() >= 10) {
                        ToDodate = thisYear + "/" + thisMonth + "/" + arrData.get(i).getDay();
                        tvY.setText("0" + thisMonth + "월 " + arrData.get(i).getDay() + "일 ");
                    } else if (thisMonth < 10 && arrData.get(i).getDay() < 10) {
                        ToDodate = thisYear + "/" + thisMonth + "/" + arrData.get(i).getDay();
                        tvY.setText("0" + thisMonth + "월 0" + arrData.get(i).getDay() + "일 ");
                    } else {
                    }
                    dateCal.set(Calendar.YEAR,thisYear);
                    dateCal.set(Calendar.MONTH,thisMonth-1);
                    dateCal.set(Calendar.DATE,arrData.get(i).getDay());
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    ToDodate=ToDodate+"/"+datS;
                    cdb.selectSchedate(ToDodate);
                    setListView();
                } else {
                    Toast.makeText(getActivity(),"잘못된 접근입니다. :-p", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //////알람추가
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrData.get(i) != null) {
                    dateCal.set(Calendar.YEAR, thisYear);
                    dateCal.set(Calendar.MONTH, thisMonth - 1);
                    dateCal.set(Calendar.DATE, arrData.get(i).getDay());
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    ToDodate=thisYear+"/"+thisMonth+"/"+arrData.get(i).getDay()+"/"+datS;
                    Intent intent = new Intent(getActivity(),OngCalendarEdit.class);
                    intent.putExtra("ToDodate",ToDodate);
                    getActivity().startActivityForResult(intent,300);
                    getActivity().overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);
                }else{}

                return false;
            }

        });
        //calenrdb
        /* DB */
        cdb = new HelperCalendar(getContext());
        acb = new HelperAlram(getContext());
        /* LIST VIEW */
        listView = view.findViewById(R.id.schedule_view);
        setfirstListView();
        // 12월 일 때 다음달 누르면 13됨
        // 다음해에 대한 연도 및 월 처리
        thisMonth = m;

        super.onActivityCreated(savedInstanceState);
    }

    public void setCalendarDate(int month) {
        arrData = new ArrayList<CalData>();

        //1월 일 때 이전 누르면 0이 됨
        if (month == 0) {
            // 이전 해에 대한 연 및 월처리
            thisMonth = 12;
            //작년이니까 년도도 줄여줘야함
            --thisYear;
            mCal.set(thisYear, 11, 1);
            if(thisMonth > 10)
                maintext.setText(thisYear+"."+thisMonth);
            else
                maintext.setText(thisYear+".0"+thisMonth);
        } else if (month == 13) {
            //다음달 1월
            thisMonth = 1;
            //다음년도 처리
            ++thisYear;
            mCal.set(thisYear, 0, 1);
            if(thisMonth > 10)
                maintext.setText(thisYear+"."+thisMonth);
            else
                maintext.setText(thisYear+".0"+thisMonth);
        } else {
            // 1일에 맞는 요일을 세팅하기 위한 설정
            mCal.set(thisYear, month - 1, 1);
            if(thisMonth >= 10)
                maintext.setText(thisYear+"."+thisMonth);
            else
                maintext.setText(thisYear+".0"+thisMonth);
        }

        thisStartDay = mCal.get(Calendar.DAY_OF_WEEK);
        if (thisStartDay != 1) {
            for (int i = 0; i < thisStartDay - 1; i++) {
                arrData.add(null);
            }
        }

        if (month == 13) {
            month = 0;
        } else if (month == 0) {
            month = 11;
        } else {
            --month;
        }

        mCal.set(thisYear, month, 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

            mCalToday.set(thisYear, month, (i + 1));
            arrData.add(new CalData( (i + 1), mCalToday.get(Calendar.DAY_OF_WEEK)));

        }

        adapter = new DateAdapter(this, arrData);
        mGridView.setAdapter(adapter);
    }
    //달력 Adapter
    private class DateAdapter extends BaseAdapter {
        private OngCalendar context;
        private ArrayList<CalData> arrData;
        private LayoutInflater inflater;
        public DateAdapter(OngCalendar c, ArrayList<CalData> arr) {
            this.context = c;
            this.arrData = arr;
            inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public int getCount() {
            return arrData.size();
        }
        public Object getItem(int position) {
            return arrData.get(position);
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            RecyclerView.ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar, parent, false);

            }
            TextView ViewText = (TextView) convertView.findViewById(R.id.date);
            TextView fir = (TextView) convertView.findViewById(R.id.fir);
            TextView sec = (TextView) convertView.findViewById(R.id.sec);
            TextView third = (TextView) convertView.findViewById(R.id.third);
            ////////////기념일 추가하기
            TextView anni = (TextView) convertView.findViewById(R.id.anniversary);
            if (arrData.get(position) == null)
                ViewText.setText("");
            else {
                ViewText.setText(arrData.get(position).getDay() + "");
                if(arrData.get(position).getDay()==d) {
                    //요일참조
                    dateCal.set(Calendar.YEAR,thisYear);
                    dateCal.set(Calendar.MONTH,thisMonth-1);
                    dateCal.set(Calendar.DATE,arrData.get(position).getDay());
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    String anniver= anniversary(thisMonth,arrData.get(position).getDay());
                    anni.setText(anniver);
                    //오늘 날에 색 바꾸기
                    ViewText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    //스케줄러에 일정 표시해주기
                    String ican= thisYear+"/"+thisMonth+"/"+d+"/"+datS;
                    ArrayList can=cdb.viewCalendar(ican);
                    if (can.size()>2){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        String c= (String) can.get(2);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText(c);
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.thi));
                    }else if (can.size()>1){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else if  (can.size()>0){
                        String a= (String) can.get(0);
                        fir.setText(a);
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else {
                        fir.setText("");
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.invisible));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }
                } else if (arrData.get(position).getDayofweek() == 1) {
                    ViewText.setTextColor(getResources().getColor(R.color.pink));
                    //요일참조
                    dateCal.set(Calendar.YEAR,thisYear);
                    dateCal.set(Calendar.MONTH,thisMonth-1);
                    dateCal.set(Calendar.DATE, parseInt(ViewText.getText().toString()));
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    String anniver= anniversary(thisMonth, parseInt(ViewText.getText().toString()));
                    anni.setText(anniver);
                    //스케줄러에 일정 표시해주기
                    String ican= thisYear+"/"+thisMonth+"/"+ViewText.getText()+"/"+datS;
                    ArrayList can=cdb.viewCalendar(ican);
                    if (can.size()>2){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        String c= (String) can.get(2);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText(c);
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.thi));
                    }else if (can.size()>1){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else if  (can.size()>0){
                        String a= (String) can.get(0);
                        fir.setText(a);
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else {
                        fir.setText("");
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.invisible));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }
                } else if (arrData.get(position).getDayofweek() == 7) {
                    ViewText.setTextColor(getResources().getColor(R.color.sen));
                    //요일참조
                    dateCal.set(Calendar.YEAR,thisYear);
                    dateCal.set(Calendar.MONTH,thisMonth-1);
                    dateCal.set(Calendar.DATE, parseInt(ViewText.getText().toString()));
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    String anniver= anniversary(thisMonth, parseInt(ViewText.getText().toString()));
                    anni.setText(anniver);
                    //스케줄러에 일정 표시해주기
                    String ican= thisYear+"/"+thisMonth+"/"+ViewText.getText()+"/"+datS;
                    ArrayList can=cdb.viewCalendar(ican);
                    if (can.size()>2){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        String c= (String) can.get(2);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText(c);
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.thi));
                    }else if (can.size()>1){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else if  (can.size()>0){
                        String a= (String) can.get(0);
                        fir.setText(a);
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else {
                        fir.setText("");
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.invisible));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }
                }else {
                    //요일참조
                    dateCal.set(Calendar.YEAR,thisYear);
                    dateCal.set(Calendar.MONTH,thisMonth-1);
                    dateCal.set(Calendar.DATE, parseInt(ViewText.getText().toString()));
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String datS= yoyo(datI);
                    String anniver= anniversary(thisMonth, parseInt(ViewText.getText().toString()));
                    anni.setText(anniver);
                    //스케줄러에 일정 표시해주기
                    String ican= thisYear+"/"+thisMonth+"/"+ViewText.getText()+"/"+datS;
                    ArrayList can=cdb.viewCalendar(ican);
                    if (can.size()>2){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        String c= (String) can.get(2);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText(c);
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.thi));
                    }else if (can.size()>1){
                        String a= (String) can.get(0);
                        String b= (String) can.get(1);
                        fir.setText(a);
                        sec.setText(b);
                        third.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.sen));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else if  (can.size()>0){
                        String a= (String) can.get(0);
                        fir.setText(a);
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.fir));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }else {
                        fir.setText("");
                        third.setText("");
                        sec.setText("");
                        fir.setBackgroundColor(getResources().getColor(R.color.invisible));
                        sec.setBackgroundColor(getResources().getColor(R.color.invisible));
                        third.setBackgroundColor(getResources().getColor(R.color.invisible));
                    }
                }
            }
            return convertView;
        }
    }

    void setfirstListView(){
        //오늘의 ToDodate를 where 하기.
        Schedule = cdb.firstselectSchedate(ToDodate);
        ScheduleViewAdapter  = new ListViewAdapter(view.getContext(),Schedule);
        listView.setAdapter(ScheduleViewAdapter);
        adapter.notifyDataSetChanged();
    }

    //스케줄러 리스트뷰
    void setListView(){
        //수정될 때 마다...
        Schedule = cdb.selectSchedate(ToDodate);
        ScheduleViewAdapter  = new ListViewAdapter(view.getContext(),Schedule);
        listView.setAdapter(ScheduleViewAdapter);
        adapter.notifyDataSetChanged();
    }

    //스케줄어댑터
    public class ListViewAdapter extends BaseAdapter {
        private final List<ScheduleItem> Schedule;
        private final LayoutInflater inflater;

        private ListViewAdapter(Context context, List<ScheduleItem> Schedule){
            this.Schedule= Schedule;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Schedule.size();
        }

        @Override
        public Object getItem(int position) {
            return Schedule.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            //holder 이용하여 뿌려주기(보여주기)
            ViewHolder holder = null;
            if( convertView==null){
                convertView = inflater.inflate(R.layout.item_schedules,parent,false);
                holder = new ViewHolder();
                holder.cont = convertView.findViewById(R.id.cont);
                holder.checkbox = convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            }
            else {
                holder= (ViewHolder)convertView.getTag();
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean cheked = Schedule.get(position).getIsChecked();
                    Schedule.get(position).setChecked(!cheked);

                    //db table 수정 및 새로운 리스트로 출력 내용 동기화
                    cdb.updateScheduleItem(Schedule.get(position));
                    setListView();
                }
            });

            //삭제
            holder.cont.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    String title = "삭제하시겠습니까?";
                    final String items[] = {"이번 일정만","이번 및 다음 연관 일정"};
                    //반복없고 날짜 같으면 해당일정만 삭제하기 위한 조회
                    final ArrayList<String> delItem  =  cdb.selectdeleteScheduleItem(Schedule.get(position));
                    final String a = delItem.get(1).split("\\|")[0];
                    final String b = delItem.get(2).split("\\|")[0];
                    //만약 반복없고 날짜같으면
                    if(delItem.get(4).equals("0") && a.equals(b) ){
                        dialog.setTitle(title).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cdb.deleteScheduleItem(Schedule.get(position));
                                Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                    }else{
                        final ArrayList<String> selectedItem  = new ArrayList<String>();
                        dialog.setTitle(title).setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        selectedItem.clear();
                                        selectedItem.add(items[whichButton]);
                                    }
                                }).setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try{
                                            if(selectedItem.get(0).equals("이번 일정만")){
                                                cdb.deleteItem(delItem);
                                                acb.deleteAll();
                                                acdupdate();
                                                Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                                setListView();  }
                                            if(selectedItem.get(0).equals("이번 및 다음 연관 일정")){
                                                cdb.deleteItems(delItem);
                                                Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                                setListView();   }
                                        }catch (Exception e){
                                            cdb.deleteItem(delItem);
                                            acb.deleteAll();
                                            acdupdate();
                                            Toast.makeText(getActivity(),"이번 일정만 삭제되었습니다",Toast.LENGTH_SHORT).show();
                                            setListView();
                                        } }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).create().show();
                    }
                    setListView();
                    alarm= cdb.alarm(todayal);
                    return true;
                }
            } );
            //스와이프 삭제
            SwipeDismissListViewTouchListener touchListener=
                    new SwipeDismissListViewTouchListener(listView,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }
                                @Override
                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                        String title = "삭제하시겠습니까?";
                                        final String items[] = {"이번 일정만","이번 및 다음 연관 일정"};
                                        //반복없고 날짜 같으면 해당일정만 삭제하기 위한 조회
                                        final ArrayList<String> delItem  =  cdb.selectdeleteScheduleItem(Schedule.get(position));
                                        final String a = delItem.get(1).split("\\|")[0];
                                        final String b = delItem.get(2).split("\\|")[0];
                                        if(delItem.get(4).equals("0") && a.equals(b) ){
                                            cdb.deleteScheduleItem(Schedule.get(position));
                                            acb.deleteAll();
                                            acdupdate();
                                        }else{
                                            final ArrayList<String> selectedItem  = new ArrayList<String>();
                                            dialog.setTitle(title).setSingleChoiceItems(items, 0,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int whichButton) {
                                                            selectedItem.clear();
                                                            selectedItem.add(items[whichButton]);
                                                        }
                                                    }).setPositiveButton("삭제",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            try{
                                                                if(selectedItem.get(0).equals("이번 일정만")){
                                                                    cdb.deleteItem(delItem);
                                                                    Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                                                    acb.deleteAll();
                                                                    acdupdate();
                                                                    setListView();  }
                                                                if(selectedItem.get(0).equals("이번 및 다음 연관 일정")){
                                                                    cdb.deleteItems(delItem);
                                                                    Toast.makeText(getActivity(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                                                    acb.deleteAll();
                                                                    acdupdate();
                                                                    setListView();   }
                                                            }catch (Exception e){
                                                                cdb.deleteItem(delItem);
                                                                Toast.makeText(getActivity(),"이번 일정만 삭제되었습니다",Toast.LENGTH_SHORT).show();
                                                                acb.deleteAll();
                                                                acdupdate();
                                                                setListView();
                                                            } }
                                                    }).setNegativeButton("취소",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                        }
                                                    }).create().show();
                                        }
                                        setListView();

                                    }
                                }
                            });
            listView.setOnTouchListener(touchListener);
            listView.setOnScrollListener(touchListener.makeScrollListener());
            //수정
            holder.cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> upItem  =  cdb.selectdeleteScheduleItem(Schedule.get(position));
                    final String a = upItem.get(1).split("\\|")[0];
                    final String b = upItem.get(2).split("\\|")[0];
                    dialogView = (View) view.inflate(getActivity(), R.layout.modishedialog, null);
                    AlertDialog.Builder dialo = new AlertDialog.Builder(getActivity());
                    dialo.setIcon(R.drawable.list);
                    String title = "수정할 내용을 입력해 주세요!  '^' ";
                    dialo.setView(dialogView);
                    final EditText modiText= dialogView.findViewById(R.id.modiText);
                    final Spinner alarmSpinner = dialogView.findViewById(R.id.alarmt);
                    final CheckBox ceckview = dialogView.findViewById(R.id.ceckview);
                    final GridLayout Grid = dialogView.findViewById(R.id.Grid);
                    final TextView start= dialogView.findViewById(R.id.start);
                    final TextView end= dialogView.findViewById(R.id.end);
                    final TextView replay= dialogView.findViewById(R.id.replayt);
                    modiText.setText(upItem.get(0));
                    ArrayAdapter alarmAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.array_alarm, android.R.layout.simple_spinner_item);
                    alarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    alarmSpinner.setAdapter(alarmAdapter);
                    String alarm = upItem.get(3).split("]")[0]+"]";
                    int positionA = alarmAdapter.getPosition(alarm);
                    alarmSpinner.setSelection(positionA);
                    if(ceckview.isChecked()){
                        Grid.setVisibility(View.VISIBLE);
                    }else {Grid.setVisibility(View.GONE);}
                    //클릭시 정보 보이게안보이게
                    ceckview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(ceckview.isChecked()){
                                Grid.setVisibility(View.VISIBLE);
                            }else {Grid.setVisibility(View.GONE);}
                        }
                    });
                    String sartt =upItem.get(1).split("\\|")[1];
                    final int sarth = parseInt(sartt.split(":")[0]);
                    final int sartm= parseInt(sartt.split(":")[1]);
                    String endt =upItem.get(2).split("\\|")[1];
                    String replayt = "[없음]";
                    switch (upItem.get(4)){
                        case "1":replayt="[매일]";break;
                        case "7":replayt="[매주]";break;
                        case "30":replayt="[매월]";break;
                        case "365":replayt="[매년]";break;
                        case "0":replayt="[없음]";break;}
                    start.setText(a.split("/")[0]+"년 "+a.split("/")[1]+"월 "+a.split("/")[2]+"일 ("+a.split("/")[3]+") "  +sartt);
                    end.setText(b.split("/")[0]+"년 "+b.split("/")[1]+"월 "+b.split("/")[2]+"일 ("+b.split("/")[3]+") "  +endt);
                    replay.setText(replayt);
                    dialo.setTitle(title).setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            String modiid = String.valueOf(upItem.get(5));
                            String modicont = modiText.getText().toString();
                            String modialarm =alarmSpinner.getSelectedItem().toString();
                            if(modialarm.equals(upItem.get(3).split("]")[0]+"]")){
                                modialarm=upItem.get(6);
                            }else{
                                modialarm=alarmshow(upItem.get(6).split("/")[0]+"/"+upItem.get(6).split("/")[1]+"/"+upItem.get(6).split("/")[2],alarmSpinner.getSelectedItem().toString());
                            }
                            if(upItem.get(4).equals("0") && a.equals(b) ){
                                if(modiText.length() > 0) {
                                    cdb.modiScheduleItem(modicont,modiid);
                                    Toast toast= Toast.makeText(getActivity(), "수정되었습니다! 'v'", Toast.LENGTH_SHORT);
                                    toast.show();
                                    setListView();
                                }else{ Toast.makeText(getActivity(),"공백 오류입니다. :-(", Toast.LENGTH_SHORT).show(); }
                            }else {
                                if(modiText.length() > 0) {
                                    onCreateDialog(upItem,modicont,modialarm,alarmSpinner.getSelectedItem().toString());
                                    setListView();

                                }else{ Toast.makeText(getActivity(),"공백 오류입니다. :-(", Toast.LENGTH_SHORT).show(); }
                                //바꿀 내용과 바꿀 아이디 그리고 원래의 텍스트
                                 } }
                    }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                }
            });
            //체크 시 아닐 시
            if( Schedule.get(position).getIsChecked()) {
                holder.checkbox.setBackground(getResources().getDrawable(R.drawable.check_box_checked));
            }
            holder.cont.setText(Schedule.get(position).getCont());
            return convertView;
        }
    }
    private class ViewHolder{
        TextView cont;
        Button checkbox;
    }

    protected void onCreateDialog(ArrayList<String> upitem, String modicont,String modialarm,String alarmSpinner) {
        final String upcont=modicont;
        final ArrayList<String> upItems = upitem;
        final String upalarm = alarmSpinner+"/"+modialarm;

        final String items[] = {"이번 일정만","이번 및 다음 연관 일정"};
        final ArrayList<String> selectedItem  = new ArrayList<String>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("선택해주세요.");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem.clear();
                selectedItem.add(items[which]);
                try{
                    if(selectedItem.get(0).equals("이번 일정만")){
                        cdb.modiselectScheduleItem(upItems,upcont,upalarm);
                        acb.deleteAll();
                        acdupdate();
                        setListView();  }
                    if(selectedItem.get(0).equals("이번 및 다음 연관 일정")){
                        cdb.modiScheduleItems(upItems,upcont,upalarm);
                        Toast.makeText(getActivity(), "수정되었습니다! 'v'", Toast.LENGTH_SHORT).show();
                        acb.deleteAll();
                        acdupdate();
                        setListView();   }
                }catch (Exception e){
                    cdb.modiselectScheduleItem(upItems,upcont,upalarm);
                    acb.deleteAll();
                    acdupdate();
                    Toast.makeText(getActivity(), "수정되었습니다! 'v'", Toast.LENGTH_SHORT).show();
                    setListView();
                }
                dialog.dismiss(); // 누르면 바로 닫히는 형태
            }
        }).create().show();

    }


    public static void acdupdate(){
        alarm= cdb.alarm(todayal);
        for(int x = 0;x != alarm.size();x++) {
            try{
                if(!alarm.get(x).getAlarmt().split("/")[0].equals("[없음]")) {
                    acb.insert(alarm.get(x).getCont(),parseInt(alarm.get(x).getAlarmt().split("/")[1]),
                            parseInt(alarm.get(x).getAlarmt().split("/")[2]),parseInt(alarm.get(x).getAlarmt().split("/")[3]),
                            parseInt(alarm.get(x).getAlarmt().split("/")[4]),parseInt(alarm.get(x).getAlarmt().split("/")[5]),
                            alarm.get(x).getAlarmt().split("/")[0] );
                }}catch (Exception e){break;}
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setListView();

    }


    public String anniversary(int annymont, int anniday){
        String anni="";
        if(annymont==11 && anniday ==11){ anni = "빼빼로";}
        if(annymont==11 && anniday ==1){ anni = "이브와";}
        if(annymont==11 && anniday ==2){ anni = "최종발표";}
        if(annymont==12 && anniday ==25){ anni = "성탄절";}
        if(annymont==10 && anniday ==3){ anni = "개천절";}
        if(annymont==10 && anniday ==9){ anni = "한글날";}
        if(annymont==8 && anniday ==15){ anni = "광복절";}
        if(annymont==6 && anniday ==6){ anni = "현충일";}
        if(annymont==5 && anniday ==5){ anni = "어린이날";}
        if(annymont==3 && anniday ==1){ anni = "삼일절";}
        if(annymont==1 && anniday ==1){ anni = "신정";}
        return anni;
    }

}


