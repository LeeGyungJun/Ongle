package com.Ongle.ong.calendar;


public class ScheduleItem {

    static int count = 0;
    private int id;
    private boolean isChecked;
    private String cont;
    private String schedate;
    private String start;
    private String endd;
    private String alarmt;
    private int replayt;


    public ScheduleItem(){
    }

    public ScheduleItem(String cont){
        isChecked=false;
        this.cont=cont;
    }



    public ScheduleItem(String cont, String schedate){
        isChecked=false;
        this.cont=cont;
        this.schedate=schedate;
    }

    public ScheduleItem(String cont, boolean isChecked, String schedate){
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
    }


    public ScheduleItem(String cont, boolean isChecked){
        this.isChecked=isChecked;
        this.cont=cont;
    }



    public ScheduleItem(int id , String cont, boolean isChecked){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
    }

    public ScheduleItem(int id , String cont, boolean isChecked, String schedate){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
    }
    public ScheduleItem(int id , String cont, boolean isChecked, String schedate, String start){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
        this.start=start;
    }
    public ScheduleItem(int id , String cont, boolean isChecked, String schedate, String start, String endd){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
        this.start=start;
        this.endd=endd;
    }
    public ScheduleItem(int id , String cont, boolean isChecked, String schedate, String start, String endd, String alarmt){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
        this.start=start;
        this.endd=endd;
        this.alarmt=alarmt;
    }
    public ScheduleItem(String cont, String schedate, String start, String endd, String alarmt, int replayt){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
        this.schedate=schedate;
        this.start=start;
        this.endd=endd;
        this.alarmt=alarmt;
        this.replayt=replayt;
    }



    void setId(int i){
        this.id=i;
    }
    void setCont(String cont){
        this.cont = cont;
    }
    void setChecked(boolean checked){
        isChecked = checked;
    }
    void setSchedate(String schedate){
        this.schedate  = schedate;
    }
    void setStart(String start){
        this.start  = start;
    }
    void setEndd(String endd){
        this.endd  = endd;
    }
    void setAlarmt(String alarmt){
        this.alarmt  = alarmt;
    }
    void setReplayt(int replayt){
        this.replayt  = replayt;
    }


    int getId(){
        return this.id;
    }
    String getCont(){
        return this.cont;
    }
    boolean getIsChecked(){
        return this.isChecked;
    }
    String getSchedate(){
        return this.schedate;
    }
    String getStart(){return this.start;}
    String getEndd(){return this.endd;}
    String getAlarmt(){return this.alarmt;}
    int getReplayt(){return this.replayt;}


}
