package com.msb.mapper;

import com.msb.pojo.Emp;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
public interface EmpMapper {


    public int insertEmp(Emp emp);

    public List<Emp> findEmpByDeptId(Integer did);

}
