package com.msb.controller;

import com.msb.pojo.Emp;
import com.msb.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IBM on 2020/9/27.
 */
@Controller
public class EmpController {

    @Autowired
    private EmpService empService ;

    @GetMapping("/empadd")
    public String empAdd(Model model)
    {
        model.addAttribute("list" , empService.showAll());

        return "emp-add";
    }

    @PostMapping("/add")
    public String add(Emp emp , MultipartFile file)
    {
        empService.insert(emp , file);
        return "emp-add";
    }
}
