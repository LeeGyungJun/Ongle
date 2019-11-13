package com.Ongle.ong.checklist;

public class CheckListItem {

    static int count = 0;
    private int id;
    private boolean isChecked;
    private String cont;

    public CheckListItem(){
    }

    public CheckListItem(String cont){
        isChecked=false;
        this.cont=cont;
    }

    public CheckListItem(String cont, boolean isChecked){
        this.isChecked=isChecked;
        this.cont=cont;
    }
    public CheckListItem(int id , String cont, boolean isChecked){
        this.id = id;
        this.isChecked=isChecked;
        this.cont=cont;
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

    int getId(){
        return this.id;
    }
    String getCont(){
        return this.cont;
    }
    boolean getIsChecked(){
        return this.isChecked;
    }

}


