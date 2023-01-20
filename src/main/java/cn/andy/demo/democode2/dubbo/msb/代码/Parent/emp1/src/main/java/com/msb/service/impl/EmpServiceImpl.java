package com.msb.service.impl;

import com.msb.dubbo.service.DeptDubboService;
import com.msb.dubbo.service.EmpDubboService;
import com.msb.pojo.Dept;
import com.msb.pojo.Emp;
import com.msb.service.EmpService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by IBM on 2020/9/27.
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Reference
    private DeptDubboService deptDubboService;

    @Reference
    private EmpDubboService empDubboService ;

    @Override
    public List<Dept> showAll() {
        return deptDubboService.findAllDept();
    }

    @Override
    public int insert(Emp emp, MultipartFile file) {
        try {
            //通过spring容器获取HttpServletRequest对象
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //通过HttpServletRequest对象 获取图片上传的路径
            String path = request.getServletContext().getRealPath("/img");
            System.out.println("path == " + path);
            //为了上传到服务器中的图片的名称不重复 编写随机数
            long currentTimeMills = System.currentTimeMillis();
            Random random = new Random();

            String fileName = currentTimeMills + "" + random.nextInt(1000);
            String oldName = file.getOriginalFilename() ;
            //通过图片的原名称获取图片的后缀名
            fileName += oldName.substring(oldName.lastIndexOf("."));

            File pathFile = new File(path);
            //第一次上传图片 检查目录是否存在 如果不存在 创建响应目录
            if(!pathFile.exists())
            {
                pathFile.mkdirs();
            }
            //图片上传
            file.transferTo(new File(path , fileName));

            //封住emp对象 把图片路径封装到emp对象中
            emp.setPhoto("http://localhost:8081/img/"+fileName);

            //把Emp保存到数据库中
            return empDubboService.insertEmp(emp);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return 0;
    }
}
