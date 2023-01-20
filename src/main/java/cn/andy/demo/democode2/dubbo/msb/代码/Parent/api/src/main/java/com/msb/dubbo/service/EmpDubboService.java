package com.msb.dubbo.service;

import com.msb.pojo.Emp;

import java.util.List;

/**
 * Created by 金喆
 */
public interface EmpDubboService {

    public int insertEmp(Emp emp);

    public List<Emp> findEmpByDeptId(Integer did);
}
