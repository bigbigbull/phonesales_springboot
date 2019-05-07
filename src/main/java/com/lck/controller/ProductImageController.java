package com.lck.controller;

import com.lck.enums.ProductImageTypeEnum;
import com.lck.pojo.Product;
import com.lck.pojo.ProductImage;
import com.lck.service.CategoryService;
import com.lck.service.ProductImageService;
import com.lck.service.ProductService;
import com.lck.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/20
 */
@RestController
public class ProductImageController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") Integer pid) throws Exception {
        Product product = productService.get(pid);

        if (ProductImageTypeEnum.TYPE_SINGLE.toString().equals(type)) {
            List<ProductImage> singles = productImageService.listSingleProductImages(product);
            return singles;
        }

        if (ProductImageTypeEnum.TYPE_DETAIL.toString().equals(type)) {
            List<ProductImage> details = productImageService.listDetailProductImages(product);
            return details;
        }

        return new ArrayList<>();
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") Integer pid, @RequestParam("type") String type, MultipartFile image, HttpServletRequest request) throws Exception {
        ProductImage productImage = new ProductImage();
        Product product = productService.get(pid);
        productImage.setProduct(product);
        productImage.setType(type);

        productImageService.add(productImage);
        // 图片文件夹名
        String folder = "img/";
        if (ProductImageTypeEnum.TYPE_SINGLE.toString().equals(productImage.getType())) {
            folder += "productSingle";
        } else {
            folder += "productDetail";
        }

        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, productImage.getId() + ".jpg");
        String fileName = file.getName();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ProductImageTypeEnum.TYPE_SINGLE.toString().equals(productImage.getType())) {
            String imageFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolderMiddle = request.getServletContext().getRealPath("img/productSingle_middle");
            File fileSmall = new File(imageFolderSmall, fileName);
            File fileMiddle = new File(imageFolderMiddle, fileName);
            fileSmall.getParentFile().mkdirs();
            fileMiddle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, fileSmall);
            ImageUtil.resizeImage(file, 56, 56, fileMiddle);
        }

        return productImage;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") Integer id, HttpServletRequest request) throws Exception {
        ProductImage productImage = productImageService.get(id);
        productImageService.delete(id);

        String folder = "img/";
        if (ProductImageTypeEnum.TYPE_SINGLE.toString().equals(productImage.getType())) {
            folder += "productSingle";
        } else {
            folder += "productDetail";
        }

        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, productImage.getId() + ".jpg");
        String fileName = file.getName();
        file.delete();

        if (ProductImageTypeEnum.TYPE_SINGLE.toString().equals(productImage.getType())) {
            String imageFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolderMiddle = request.getServletContext().getRealPath("img/productSingle_middle");
            File fileSmall = new File(imageFolderSmall, fileName);
            File fileMiddle = new File(imageFolderMiddle, fileName);
            fileSmall.delete();
            fileMiddle.delete();
        }

        return null;
    }


}
