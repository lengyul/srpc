package pers.allen.rpc.server.service.impl;

import pers.allen.rpc.server.annotation.Service;
import pers.allen.rpc.server.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lengyul on 2019/4/26 13:51
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<String> getUserName() {
        return Arrays.asList("Allen","Emma");
    }
}
