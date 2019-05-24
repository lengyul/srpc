package pers.allen.rpc.server.dto;

/**
 * Created by lengyul on 2019/4/25 16:36
 */
public class RequestMsg {

    private int type; // 请求类型 0 sync、1 async
    private long requestId; // 请求唯一标识
    private String url; // 请求地址
    private long timeout; // 超时时间
    private String className; // 全类名
    private String methodName; // 方法名称
    private Class<?>[] paramTypes; // 参数类型
    private Object[] params; // 参数列表


    public  RequestMsg() {}
    // 初始化数据（注解）
    public RequestMsg(int type, String url, long timeout) {
        this.type = type;
        this.url = url;
        this.timeout = timeout;
    }

    // 请求数据封装
    public RequestMsg(long requestId, String className, String methodName, Class<?>[] paramTypes, Object[] params) {
     //   this.type = 0;
        this.requestId = requestId;
        this.className = className;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
