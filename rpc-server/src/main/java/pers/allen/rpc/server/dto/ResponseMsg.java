package pers.allen.rpc.server.dto;

public class ResponseMsg {

    private int code; // 响应码
    private String msg; // 响应消息
    private int type; // 请求类型
    private long requestId; // 请求标识
    private long responseId; // 响应标识
    private Object data; // 返回数据

    public ResponseMsg(){}

    public ResponseMsg(int code, String msg,int type,long requestId, long responseId, Object data) {
        this.code = code;
        this.msg = msg;
        this.type = type;
        this.requestId = requestId;
        this.responseId = responseId;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", requestId=" + requestId +
                ", responseId=" + responseId +
                ", data=" + data +
                '}';
    }
}
