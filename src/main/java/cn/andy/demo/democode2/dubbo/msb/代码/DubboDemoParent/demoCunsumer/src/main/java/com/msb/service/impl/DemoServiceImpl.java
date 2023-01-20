package com.msb.service.impl;

import com.msb.dubbo.service.DemoDubboService;
import com.msb.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * Created by IBM on 2020/9/26.
 */
@Service
public class DemoServiceImpl  implements DemoService{

    @Reference
    private DemoDubboService demoDubboService ;

    @Override
    public String demo() {
        return demoDubboService.demo("张三丰");
    }
}
