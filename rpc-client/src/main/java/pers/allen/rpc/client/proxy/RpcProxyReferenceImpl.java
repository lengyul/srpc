package pers.allen.rpc.client.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import pers.allen.rpc.client.annotation.Reference;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public final class RpcProxyReferenceImpl implements ApplicationContextAware, InitializingBean {

    private final Map<Class,Object> proxyImplMap = new HashMap<>(); // 保存消费者接口代理类
    private final Logger logger = LoggerFactory.getLogger(RpcProxyReferenceImpl.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("消费者接口代理类创建完成：" + proxyImplMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(RestController.class);
        initReferenceImpl(beans);
    }

    /**
     * 对@Reference标记的属性绑定代理类
     * @param beans
     */
    private void initReferenceImpl(Map<String, Object> beans) {
        beans.values().forEach((val) -> {
            Field[] fields = val.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Reference ref = fields[i].getAnnotation(Reference.class);
                if(ref != null) {
                    fields[i].setAccessible(true);
                    try {
                        Class clazz = fields[i].getType();
                        fields[i].set(val,createProxyClass(clazz));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 创建代理类并保存到缓存集合中
    * @param clazz
     * @return
     */
    private Object createProxyClass(Class clazz) {
        Object clazzImpl = null;
        if(proxyImplMap.containsKey(clazz)) {
            clazzImpl = proxyImplMap.get(clazz);
        } else {
            clazzImpl =  RpcProxyFactoryInvoke.proxyInstance(clazz);
            proxyImplMap.put(clazz,clazzImpl);
        }
        return clazzImpl;
    }

}
