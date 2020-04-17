package com.xp.common.po;

import com.google.gson.annotations.Expose;

public class ResultData {
    public ResultData(){

    }
    /**
     * http 请求的响应
     * @param status 响应状态
     * @param message 响应信息
     * @param json 响应数据
     */

    public ResultData(Status status, String message, String json) {
        this.status = status;
        this.message = message;
        this.json = json;
    }
    /**
     * 结果状态
     */
    @Expose
    private Status status;
    /**
     * 提示
     */
    @Expose
    private String message;
    /**
     * 返回的json 数据
     */
    @Expose
    private String json;

    /**
     * string data 转换 成 具体对象
     */
    @Expose
    private Object object;

    public ResultData(Status error, String message) {
        this.status = status;
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
