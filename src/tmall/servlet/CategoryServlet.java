package tmall.servlet;


import tmall.bean.Category;
import tmall.dao.CategoryDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryServlet extends BaseBackServlet
{

    public String list(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        List<Category> categories = categoryDAO.list(page.getStart(),page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("categories",categories);
        request.setAttribute("page",page);
        return "admin/listCategory.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        Map<String,String> param = new HashMap<>();
        InputStream is = super.parseUpload(request,param);
        String name = param.get("name");
        Category category = new Category();
        category.setName(name);
        categoryDAO.add(category);
        File folder = new File(request.getServletContext().getRealPath("img/category"));
        if(!folder.exists())
            folder.mkdirs();
        File file = new File(folder, category.getId()+".jpg");
        try
        {
            if(is != null && is.available() != 0)
            {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024 * 1024];
                int length = 0;
                while((length = is.read(b)) != -1)
                    fos.write(b,0,length);
                fos.flush();
                BufferedImage bufferedImage = ImageUtil.change2jpg(file);
                ImageIO.write(bufferedImage,".jpg",file);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin/listCategory.jsp";
    }

    public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id);
        request.setAttribute("c",c);
        return "admin/editCategory.jsp";
    }

    public String update(HttpServletRequest request, HttpServletResponse response, Page page)
    {
    }


}
