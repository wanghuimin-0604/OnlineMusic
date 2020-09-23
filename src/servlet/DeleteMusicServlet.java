package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import eneity.Music;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过歌曲id删除歌曲信息
 */
@WebServlet("/deleteMusicServlet")
public class DeleteMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String idStr = req.getParameter("id");
        int id = Integer.parseInt(idStr);
        MusicDao musicDao = new MusicDao();
        //1、查找当前id的音乐是否存在
        Music music = musicDao.findMusicById(id);
        if(music == null) {
            return;
        }
        //2、如果该歌曲存在，那么根据id删除歌曲
        int delete = musicDao.deleteMusicById(id);

        Map<String ,Object> return_map = new HashMap<>();
        //数据库删除成功
        if(delete == 1) {
            //仅仅代表数据库删除了，但是服务器上的音乐是否存在
            System.out.println("数据库删除成功");
            File file = new File("/root/java/apache-tomcat-8.5.57/webapps/OnlineMusic/"+music.getUrl()+".mp3");
            //服务器上删除成功了,但是页面显示是错误的
            //delete()方法，删除文件成功返回true，删除文件失败返回false
            if(file.delete()) {
                return_map.put("msg",true);
                System.out.println("服务器删除成功！");
            }else {
                return_map.put("msg",false);
                System.out.println("服务器删除失败！");
            }
        }else {
            System.out.println("数据库删除失败");
            return_map.put("msg",false);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
