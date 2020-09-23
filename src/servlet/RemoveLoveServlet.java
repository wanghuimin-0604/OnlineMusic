package servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import eneity.User;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 从喜欢列表删除歌曲，但不是从数据库库里面删除，只是在喜欢音乐的数据库删除
 */
@WebServlet("/removeLoveServlet")
public class RemoveLoveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();
        Map<String,Object> map=new HashMap<>();
        //根据id去删除
        String strId = req.getParameter("id");
        int music_id = Integer.parseInt(strId);
        MusicDao musicDao = new MusicDao();
        //根据user_id和music_id从喜欢列表中删除歌曲
        int delete = musicDao.removeLoveMusic(user_id,music_id);
        if(delete == 1) {
            map.put("msg",true);
        }else {
            map.put("msg",false);
        }
        ObjectMapper mapper=new ObjectMapper();
        mapper.writeValue(resp.getWriter(),map);
    }
}
