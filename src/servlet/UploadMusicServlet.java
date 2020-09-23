package servlet;
import eneity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 上传歌曲到服务器
 */
@WebServlet("/upload")
public class UploadMusicServlet extends HttpServlet {
    //private final String SAVEPATH = "E:\\java16\\OnlineMusic\\web\\music";
    private final String SAVEPATH ="/root/java/apache-tomcat-8.5.57/webapps/OnlineMusic/music";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //先判断用户是否登录
        User user = (User) req.getSession().getAttribute("user");
        if(user == null) {
            System.out.println("请登录后再上传音乐！");
            resp.getWriter().write("<h1> 请登录后在上传音乐"+"</h1>");
            return;
        }else {
            //上传
            //创建工厂类
            FileItemFactory factory = new DiskFileItemFactory();
            //创建解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //使用解析器解析request对象
            List<FileItem> fileItems = null;
            try {
                fileItems = upload.parseRequest(req);
            } catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }
            //接受表单的所有数据
            System.out.println("fileItems："+fileItems);
            FileItem fileItem = fileItems.get(0);
            System.out.println("fileItem：" + fileItem);
            String fileName = fileItem.getName();//得到文件名
            req.getSession().setAttribute("fileName", fileName);
            try {
                //存储文件
                fileItem.write(new File(SAVEPATH,fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //2、上传成功的话重定向到上传成功的页面
            resp.sendRedirect("uploadsucess.html");
        }

    }
}
