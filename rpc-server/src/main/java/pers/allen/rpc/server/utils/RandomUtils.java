package pers.allen.rpc.server.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by lengyul on 2019/4/25 15:20
 */
public class RandomUtils {

    private RandomUtils() {
        throw new AssertionError();
    }

    public static long getRequestId(int count) {
        String number =  RandomStringUtils.randomNumeric(count);
        String millis = String.valueOf(System.currentTimeMillis());
        return Long.parseLong(millis + number);
    }

}
