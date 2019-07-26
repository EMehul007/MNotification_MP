package com.example.mnotification;

public class MessageModel {

    String Number;
    String message;
    String time;
    String type;

    public MessageModel(String number, String message, String time, String type, String name) {
        Number = number;
        this.message = message;
        this.time = time;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public MessageModel(String number, String message, String time, String type) {
        Number = number;
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
