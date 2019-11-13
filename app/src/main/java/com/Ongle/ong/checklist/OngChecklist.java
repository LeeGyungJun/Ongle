package com.Ongle.ong.checklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Ongle.ong.calendar.SwipeDismissListViewTouchListener;
import com.example.ong.R;

import java.util.List;


public class OngChecklist extends Fragment {

    View view;
    HelperChecklist db;
    ListView listView;
    ListViewAdapter checkListViewAdapter;
    List<CheckListItem> checklist;
    Button add_check;
    EditText check_edit;


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.activity_ong_checklist,null);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //check_list
        /* DB */
        db = new HelperChecklist(getContext());

        /* LIST VIEW */
        listView = view.findViewById(R.id.check_list);
        setListView();
        check_edit = view.findViewById(R.id.check_edit);

        add_check = view. findViewById(R.id.add_check);
        add_check.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                String cont = check_edit.getText().toString();
                if (cont.length() > 0) {
                    db.addCheckListItem(new CheckListItem(cont));
                    setListView();  // 새로 업데이트 된 리스트를 사용해서 리스트뷰 다시 설정해줘야 함
                    check_edit.setText("");
                } else {
                    Toast.makeText(getActivity(), "내용을 입력해주세요:-) ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setListView() {
        checklist = db.getAllCheckListItems();
        checkListViewAdapter  = new ListViewAdapter(view.getContext(), checklist);
        listView.setAdapter(checkListViewAdapter);
    }

    public class ListViewAdapter extends BaseAdapter {
            private final List<CheckListItem> checklist;
            private final LayoutInflater inflater;

            private ListViewAdapter(Context context, List<CheckListItem> checklist){
                this.checklist= checklist;
                inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return checklist.size();
            }

            @Override
            public Object getItem(int position) {
                return checklist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if( convertView==null){
                    convertView = inflater.inflate(R.layout.item_checklist,parent,false);
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
                        boolean cheked = checklist.get(position).getIsChecked();
                        checklist.get(position).setChecked(!cheked);

                        //db table 수정 및 새로운 리스트로 출력 내용 동기화
                        db.updateCheckListItem(checklist.get(position));
                        setListView();
                    }
                });

                holder.cont.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        View dialogView = (View) view.inflate(getActivity(), R.layout.modishedialog, null);
                        AlertDialog.Builder dialo = new AlertDialog.Builder(getActivity());
                        dialo.setIcon(R.drawable.list);
                        String title = "수정할 내용을 입력해 주세요!  '^' ";
                        dialo.setView(dialogView);
                        final EditText modiText= dialogView.findViewById(R.id.modiText);
                        modiText.setText(String.valueOf(checklist.get(position).getCont()));

                        dialo.setTitle(title).setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String modiid = String.valueOf(checklist.get(position).getId());
                                if(modiText.length() > 0) {
                                    String a = modiText.getText().toString();
                                    String b = modiid;
                                    db. modiCheckListItem(a, b);
                                    Toast toast= Toast.makeText(getActivity(), "수정되었습니다! 'v'", Toast.LENGTH_SHORT);
                                    toast.show();
                                    setListView();
                                }else{
                                    Toast.makeText(getActivity(),"공백 오류입니다. :-(", Toast.LENGTH_SHORT).show();

                                } }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                        return false;
                    }
                });

                //스와이프 삭제
                SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }
                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            db.deleteCheckListItem(checklist.get(position));
                        }
                        setListView();
                    }
                });

                listView.setOnTouchListener(touchListener);
                listView.setOnScrollListener(touchListener.makeScrollListener());


                if( checklist.get(position).getIsChecked()) {
                    holder.checkbox.setBackground(getResources().getDrawable(R.drawable.check_box_checked));
                }

                holder.cont.setText(checklist.get(position).getCont());

                return convertView;
            }

        }





    private class ViewHolder{
        TextView cont;
        Button checkbox;

    }


    }
