package servlet;

import dao.MusicDao;
import eneity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将歌曲信息写入到数据库中
 */
@WebServlet("/uploadsucess")
public class UploadInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String fileName = (String)req.getSession().getAttribute("fileName");
        //红妆.mp3
        String[] strings = fileName.split("\\.");
        String title = strings[0];
        String singer =  req.getParameter("singer");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //2020-07-29
        String time = sdf.format(new Date());
        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();
        String url = "music/"+title;
        MusicDao musicDao = new MusicDao();
        int ret = musicDao.insert(title,singer,time,url,user_id);
        //歌曲插入数据库成功，跳转到歌曲列表页面
        if(ret == 1) {
            resp.sendRedirect("list.html");
        }
    }
}
