package com.sonicers.commonlib.net;

/**
 * 网络请求结果 基类,剥离出数据给上层.该类仅供参考，实际业务逻辑, 根据需求来定义
 */
public class RxRespond<T> {

    /**
     * code : 200
     * status : SUCCESS
     * msg :
     * data : [{"alarmLevel":1,"alarmTemperature":75},{"alarmLevel":2,"alarmTemperature":100},{"alarmLevel":3,"alarmTemperature":150}]
     */

    private int code;
    private String status;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
