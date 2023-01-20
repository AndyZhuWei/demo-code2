package com.msb.dubbo.service.impl;

import com.msb.dubbo.service.DemoDubboService;
import org.apache.dubbo.config.annotation.Service;

/**
 * 此类为接口的实现类，
 *
 *     注解为apache的@Service.
 *     区分项目是Provider还是Consumer最重要依据
 */
@Service(weight = 4)
public class DemoDubboServiceImpl implements DemoDubboService {

    public String demo(String param) {
        System.out.println("执行demo");
        return param+"123";
    }
}
