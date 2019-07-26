package com.example.mnotification;

public class CallLogBean {

    String callnumber,callname,callduration,calltime,calltype;

    public CallLogBean(String callnumber, String callname, String callduration, String calltime, String calltype) {
        this.callnumber = callnumber;
        this.callname = callname;
        this.callduration = callduration;
        this.calltime = calltime;
        this.calltype = calltype;
    }

    public CallLogBean() {
    }

    public String getCallnumber() {
        return callnumber;
    }

    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }

    public String getCallname() {
        return callname;
    }

    public void setCallname(String callname) {
        this.callname = callname;
    }

    public String getCallduration() {
        return callduration;
    }

    public void setCallduration(String callduration) {
        this.callduration = callduration;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }
}
