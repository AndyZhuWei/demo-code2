package com.msb.service.impl;

import com.msb.dubbo.service.DeptDubboService;
import com.msb.dubbo.service.EmpDubboService;
import com.msb.pojo.Dept;
import com.msb.pojo.Emp;
import com.msb.service.DeptService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Reference
    private DeptDubboService deptDubboService;

    @Reference
    private EmpDubboService empDubboService ;


    @Override
    public List<Dept> findAll() {
        return deptDubboService.findAllDept();
    }

    @Override
    public List<Emp> findEmpByDeptId(Integer did) {
        return empDubboService.findEmpByDeptId(did);
    }


}
