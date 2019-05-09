package pers.allen.rpc.server.handler;

import com.alibaba.fastjson.JSON;
import pers.allen.rpc.server.dto.RequestMsg;

import java.lang.reflect.Method;
import java.util.Map;

public class RequestLocalCall {

    private RequestLocalCall() {
        throw new AssertionError();
    }

    private static Map<String, Object> readonlyService;

    public static void setServers(Map<String, Object> readonlyService) {
        if(readonlyService == null) {
            throw new NullPointerException("readonlyService");
        }
        if(RequestLocalCall.readonlyService != null) {
            throw new IllegalStateException("readonlyService set already");
        }
        RequestLocalCall.readonlyService = readonlyService;
    }

    public static Object handler(RequestMsg request) {
        String className = request.getClassName();
        Object iservice = readonlyService.get(className);
        if(iservice != null) {
            try {
                Class<?> iserviceClass = iservice.getClass();
                String methodName = request.getMethodName();
                Class<?>[] paramTypes = request.getParamTypes();
                Object[] params = request.getParams();
                Method method = iserviceClass.getMethod(methodName, paramTypes);
                method.setAccessible(true);
                return method.invoke(iservice, getParameters(paramTypes,params));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return  null;
    }

    private static Object[] getParameters(Class<?>[] parameterTypes,Object[] parameters){
        if (parameters == null || parameters.length == 0){
            return parameters;
        }else{
            Object[] newParameters = new Object[parameters.length];
            for(int i=0;i<parameters.length;i++){
                newParameters[i] = JSON.parseObject(parameters[i].toString(),parameterTypes[i]);
            }
            return newParameters;
        }
    }

}
