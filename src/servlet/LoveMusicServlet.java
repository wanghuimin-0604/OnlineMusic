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
 * 添加歌曲到喜欢列表
 */
@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws
    ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String strId = req.getParameter("id");
        int musicId = Integer.parseInt(strId);
        System.out.println("musicID: "+musicId);
        //先拿到用户id
        User user = (User) req.getSession().getAttribute("user");
        int user_id = user.getId();
        MusicDao musicDao = new MusicDao();
        Map<String,Object> map=new HashMap<>();
        //插入之前需要先查看该歌曲是否已经被添加到喜欢列表
        //已经存在的话，就不再重复插入
        //不存在的话，就直接插入
        //通过这两个参数来判断（用户id,音乐id)
        boolean effect = musicDao.findMusicByMusicId(user_id,musicId);
        if(effect) {
            //这首歌在我喜欢列表中，那么就不能再添加了
            map.put("msg",false);
        }else {
            //喜欢列表中没有该歌曲
            //向喜欢列表中添加该歌曲
            boolean flg = musicDao.insertLoveMusic(user_id,musicId);
            if(flg) {
                //添加成功
                map.put("msg",true);
            }else {
                //添加失败
                map.put("msg",false);
            }
        }
        //将map返回给前端就可以了
        ObjectMapper mapper=new ObjectMapper();
        mapper.writeValue(resp.getWriter(),map);
    }
}