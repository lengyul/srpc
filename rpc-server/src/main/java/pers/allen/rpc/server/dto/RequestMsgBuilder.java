package pers.allen.rpc.server.dto;

import java.util.Arrays;

public class RequestMsgBuilder {

    private final int type; // 请求类型
    private final long requestId; // 请求唯一标识
    private final String url; // 请求地址
    private final long timeout; // 超时时间
    private final String className; // 全类名
    private final String methodName; // 方法名称
    private final Class<?>[] paramTypes; // 参数类型
    private final Object[] params; // 参数列表

    private RequestMsgBuilder(Builder builder){
        type = builder.type;
        requestId = builder.requestId;
        url = builder.url;
        timeout = builder.timeout;
        className = builder.className;
        methodName = builder.methodName;
        paramTypes = builder.paramTypes;
        params = builder.params;
    }

    public static class Builder {
        private int type;
        private final long requestId;
        private String url;
        private long timeout;
        private String className;
        private String methodName;
        private Class<?>[] paramTypes;
        private Object[] params;

        public Builder(long requestId) {
            this.type = 1;
            this.requestId = requestId;
        }

        public Builder(int type, long requestId) {
            this.type = type;
            this.requestId = requestId;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
        public Builder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }
        public Builder className(String className) {
            this.className = className;
            return this;
        }
        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }
        public Builder paramTypes(Class<?>[] paramTypes) {
            this.paramTypes = paramTypes;
            return this;
        }
        public Builder params(Object[] params) {
            this.params = params;
            return this;
        }

        public RequestMsgBuilder build() {
            return new RequestMsgBuilder(this);
        }
    }

    public long getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "RequestMsgBuilder{" +
                "type=" + type +
                ", requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + paramTypes +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
