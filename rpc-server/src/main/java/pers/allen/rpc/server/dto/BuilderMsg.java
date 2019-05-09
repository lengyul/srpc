package pers.allen.rpc.server.dto;

import pers.allen.rpc.server.utils.RandomUtils;
import java.lang.reflect.Method;

public class BuilderMsg {

    private BuilderMsg() {
        throw new AssertionError();
    }

    public static RequestMsg buildRequestMsg(Method method, Object[] params) {
      //  int type, long requestId, String className, String methodName, Class<?>[] paramTypes, Object[] params
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        /*return new RequestMsgBuilder.Builder(RandomUtils.getRequestId(5))
                .className(className).methodName(methodName)
                .paramTypes(paramTypes).params(params).build();*/
        return new RequestMsg(RandomUtils.getRequestId(5),className,methodName,paramTypes,params);
    }

    public static ResponseMsg buildResponseMsg(long requestId, Object data) {
        return new ResponseMsg(0,"",requestId,RandomUtils.getRequestId(5),data);
    }


}
