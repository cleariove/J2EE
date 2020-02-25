package tmall.servlet;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductImageServlet extends BaseBackServlet
{
    public String list(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        List<ProductImage> imageSingle = productImageDAO.list(p, ProductImageDAO.type_single);
        List<ProductImage> imageDetail = productImageDAO.list(p, ProductImageDAO.type_detail);
        request.setAttribute("p",p);
        request.setAttribute("imageSingle",imageSingle);
        request.setAttribute("imageDetail",imageDetail);
        return "admin/listProductImage.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        Map<String,String> map = new HashMap<>();
        InputStream inputStream = parseUpload(request,map);
        String type = map.get("type");
        int pid = Integer.parseInt(map.get("pid"));
        Product product = productDAO.get(pid);
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setType(type);
        productImageDAO.add(productImage);
        String fileName = productImage.getId()+".jpg";
        String imageFolder = null;
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if(type.equals(ProductImageDAO.type_single))
        {
            imageFolder = request.getServletContext().getRealPath("img/productSingle");
            imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
        }
        else
            imageFolder = request.getServletContext().getRealPath("img/productDetail");
        File f = new File(imageFolder,fileName);
        if(!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try
        {
            if(inputStream != null && inputStream.available()!=0)
            {
                FileOutputStream fos = new FileOutputStream(f);
                byte[] bytes = new byte[1024*1024];
                int length = 0;
                while (-1 != (length = inputStream.read(bytes)))
                    fos.write(bytes,0,length);
                fos.flush();
                BufferedImage bufferedImage = ImageUtil.change2jpg(f);
                ImageIO.write(bufferedImage,".jpg",f);
                if(type.equals(ProductImageDAO.type_single))
                {
                    File fSmall = new File(imageFolder_small,fileName);
                    File fMiddle = new File(imageFolder_middle,fileName);
                    ImageUtil.resizeImage(f, 56, 56, fSmall);
                    ImageUtil.resizeImage(f, 217, 190, fMiddle);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "@admin_productImage_list?pid="+pid;
    }

    public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage productImage = productImageDAO.get(id);
        productImageDAO.delete(id);
        if(productImage.getType().equals(ProductImageDAO.type_single))
        {
            String imgFolderSingle = request.getServletContext().getRealPath("img/productSingle");
            String imgFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            String imgFolderMiddle = request.getServletContext().
                    getRealPath("img/productSingle_middle");
            File fileSingle = new File(imgFolderSingle,productImage.getId()+".jpg");
            File fileSmall = new File(imgFolderSmall,productImage.getId()+".jpg");
            File fileMiddle = new File(imgFolderMiddle,productImage.getId()+".jpg");
            fileSingle.delete();
            fileSmall.delete();
            fileMiddle.delete();
        }
        else
        {
            String imgFolderDetail = request.getServletContext().getRealPath("img/productDetail");
            File fileDetail = new File(imgFolderDetail,productImage.getId()+".jpg");
            fileDetail.delete();
        }
        return "@admin_productImage_list?pid="+productImage.getProduct().getId();
    }

    public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        return null;
    }

    public String update(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        return null;
    }
}
