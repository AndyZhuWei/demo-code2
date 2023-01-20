package com.msb.service;

import com.msb.pojo.Dept;
import com.msb.pojo.Emp;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
public interface DeptService {

    public List<Dept> findAll();

    public List<Emp> findEmpByDeptId(Integer did);
}