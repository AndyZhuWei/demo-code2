package com.msb.service;

import com.msb.pojo.Dept;
import com.msb.pojo.Emp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by IBM on 2020/9/27.
 */
public interface EmpService {

    public List<Dept> showAll() ;

    public int insert(Emp emp , MultipartFile file);
}
