package com.msb.dubbo.service.impl;

import com.msb.dubbo.service.DeptDubboService;
import com.msb.mapper.DeptMapper;
import com.msb.pojo.Dept;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
@Service
public class DeptDubboServiceImpl  implements DeptDubboService{

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAllDept() {
        return deptMapper.findAll();
    }
}
