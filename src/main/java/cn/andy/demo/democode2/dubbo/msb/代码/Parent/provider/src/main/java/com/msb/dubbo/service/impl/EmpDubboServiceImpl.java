package com.msb.dubbo.service.impl;

import com.msb.dubbo.service.EmpDubboService;
import com.msb.mapper.EmpMapper;
import com.msb.pojo.Emp;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by 金喆
 */

@Service
public class EmpDubboServiceImpl implements EmpDubboService {

    @Autowired
    private EmpMapper empMapper ;

    @Override
    public int insertEmp(Emp emp) {

        return empMapper.insertEmp(emp);

    }

    @Override
    public List<Emp> findEmpByDeptId(Integer did) {
        return empMapper.findEmpByDeptId(did);
    }


}
