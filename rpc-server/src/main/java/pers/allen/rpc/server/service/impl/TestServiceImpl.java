package pers.allen.rpc.server.service.impl;

import pers.allen.rpc.server.annotation.Service;
import pers.allen.rpc.server.service.TestService;

import java.util.Arrays;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {


    @Override
    public List<String> getList() {

        return Arrays.asList("1","2","3");
    }
}
