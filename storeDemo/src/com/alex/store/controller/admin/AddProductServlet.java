package com.alex.store.controller.admin;

import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.AddProductService;
import com.alex.store.service.admin.impl.AddProductServiceImpl;
import com.alex.store.service.admin.impl.ProductServiceImpl;
import com.alex.store.util.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddProductServlet",urlPatterns = "/AddProductServlet")
public class AddProductServlet extends BaseServlet {

    AddProductService addProductService = new AddProductServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            if (request.getParameter("op") == null) {
                // 1 创建磁盘文件工厂  ---生产核心上传对象
                DiskFileItemFactory disk = new DiskFileItemFactory();
                // 2  生产上传的核心对象 ---解析请求体 (request)
                ServletFileUpload sfu = new ServletFileUpload(disk);
                // 3 解析request
                List<FileItem> list = sfu.parseRequest(request);
                // 4 遍历所有的表单项
                Map<String, Object> map = new HashMap<>();
                for (FileItem fileItem : list) {
                    // 判断当前循环的这个表单项是否为普通项  true:普通项  false:上传项
                    if (fileItem.isFormField()) {
                        // 普通项
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString("utf-8");
                        map.put(name, value);
                    } else {
                        // 上传项
                        // 获取文件名称
                        String filename = fileItem.getName(); // 获取文件名
                        // 将文件名拼接上时间戳
                        long millis = System.currentTimeMillis();
                        int index = filename.lastIndexOf(".");
                        String newFileName = filename.substring(0, index) + millis + filename.substring(index);
                        // 获取文件内容
                        InputStream is = fileItem.getInputStream(); //文件内容
                        String path = "/upload";
                        // 放入map中

                        String realPath = getServletContext().getRealPath("/upload");
                        File file = new File(realPath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        FileOutputStream os = new FileOutputStream(file + "/" + newFileName);
                        IOUtils.copy(is, os);
                        map.put("imgurl", "/upload" +"/" + newFileName);
                        //关流
                        os.close();
                        is.close();
                    }
                }
                // 将map中的数据拷贝到bean中
                Product product = new Product();
                BeanUtils.populate(product, map);
                // 调用service的方法
                addProductService.addProduct(product);
                // 添加完成之后，请求转发到列表页面
                request.getRequestDispatcher("/ProductServlet?op=findAllProduct&num=1").forward(request, response);
            } else {
                Class clazz = this.getClass();
                Method method = clazz.getMethod(request.getParameter("op"), HttpServletRequest.class, HttpServletResponse.class);
                String value = (String) method.invoke(clazz.newInstance(), request, response);
                if (value != null) {
                    request.getRequestDispatcher(value).forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);

    }
}
