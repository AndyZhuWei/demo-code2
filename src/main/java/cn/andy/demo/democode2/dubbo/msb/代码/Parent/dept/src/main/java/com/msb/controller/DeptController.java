package com.msb.controller;

import com.msb.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IBM on 2020/9/27.
 */
@Controller
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/dept")
    public String showDept(Model model)
    {
        model.addAttribute("list" , deptService.findAll());
        return "dept";
    }

    @GetMapping("/showEmp")
    public String showEmp(Integer did , Model model)
    {
        model.addAttribute("list" , deptService.findEmpByDeptId(did));
        return "showEmp";
    }
}
