package com.xp.common.po;

/**
 * callback 封装的 result
 */
public class Result {
    private Status status;
    private String message;
    /**
     * 可以装入万能的对象
     */
    private Object tag;
    public Result(Status status) {
        this.status = status;
    }

    public Result(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
