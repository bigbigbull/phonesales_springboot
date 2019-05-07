package com.lck.controller;

import com.lck.pojo.Category;
import com.lck.service.CategoryService;
import com.lck.util.ImageUtil;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/** 此控制器每个方法的返回值都会转为json格式*/

/**
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;



     /**
      *describe: 分页查询
      *@return Page4Navigator
      *@param size,start
      *@excption
      * */
    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start",defaultValue ="0" ) int start,@RequestParam(value = "size", defaultValue = "5") int size)throws Exception{
        start = start<0?0:start;
        // 5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        Page4Navigator<Category> page = categoryService.list(start,size,5);
        return page;
    }

    /** 增加 */
    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception{
        categoryService.add(bean);
        saveOrUpdateImageFile(bean,image,request);
        return bean;
    }

    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        // 保存到img/category目录下
        File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        // 用id命名
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 文件复制
        image.transferTo(file);
        // 强转为jpg格式
        BufferedImage img = ImageUtil.change2jpg(file);
        // 保存图片
        ImageIO.write(img, "jpg", file);
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") Integer id,HttpServletRequest request)throws Exception{
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") Integer id) throws Exception {
        Category bean=categoryService.get(id);
        return bean;
    }

    @PutMapping("/categories/{id}")
    public Object update(Category bean,MultipartFile image,HttpServletRequest request) throws Exception{
        // put方式不能直接注入Category对象
        String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);

        if(image!=null){
            saveOrUpdateImageFile(bean,image,request);
        }
        return bean;
    }
}
