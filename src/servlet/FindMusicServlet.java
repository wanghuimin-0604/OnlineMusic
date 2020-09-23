package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import eneity.Music;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找音乐
 */
@WebServlet("/findMusic")
public class FindMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String musicName = req.getParameter("musicName");
        MusicDao musicDao = new MusicDao();
        List<Music> musicList = new ArrayList<>();
        //判断数据库中是否有这首歌曲
        if(musicName != null) {
            //有这首歌曲的话，就返回该歌曲
            musicList = musicDao.ifMusic(musicName);
        }else {
            //没有的话，就返回整个歌曲列表
            musicList = musicDao.findMusic();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musicList);

    }
}