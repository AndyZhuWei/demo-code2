package com.msb.dubbo.service;

import com.msb.pojo.Dept;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
public interface DeptDubboService {

    public List<Dept> findAllDept();
}
