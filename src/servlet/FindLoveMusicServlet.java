package servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import eneity.Music;
import eneity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据关键字查找当前用户喜欢的所有音乐
 */
@WebServlet("/findLoveMusic")
public class FindLoveMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //得到关键字
        String str = req.getParameter("loveMusicName");
        System.out.println("loveMusicName:"+str);
        //根据session得到user_id
        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();
        MusicDao musicDao = new MusicDao();
        List<Music> musics = null;
        if(str!=null) {
            //根据关键字去查询
            musics = musicDao.ifMusicLove(str,user_id);//关键字查询
        }else {
            //str为空的话，默认查找全部的喜欢列表的音乐
            musics = musicDao.findLoveMusic(user_id);
        }
        //得到音乐的url
        for (Music music : musics) {
            System.out.println(music.getUrl());
        }
        //将map返回给客户端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musics);
    }
}
