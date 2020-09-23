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
import java.util.Arrays;

/**
 * 删除选中歌曲（有可能是一首、也可能是多首）
 */
@WebServlet("/deleteSelMusicServlet")
public class DeleteSelMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //如果是批量删除怎么样算成功？-》全部删除成功
        String[] values = req.getParameterValues("id[]");
        //values数组当中，存放的所有需要删除的歌曲的id
        MusicDao musicDao = new MusicDao();
        int sum = 0;
        Map<String ,Object> return_map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            int id = Integer.parseInt(values[i]);
            Music music = musicDao.findMusicById(id);
            int delete = musicDao.deleteMusicById(id);
            //删除数据库
            if(delete == 1) {
                File file = new File("/root/java/apache-tomcat-8.5.57/webapps/OnlineMusic/"+music.getUrl()+".mp3");
                /**
                 * sum用来统计删除成功的歌曲数目，成功一次，sum++
                 */
                //删除服务器上的
                if(file.delete()) {
                    sum += delete;
                }else {
                    return_map.put("msg",false);
                    System.out.println("服务器删除失败！");
                }
            }else {
                return_map.put("msg",false);
                System.out.println("数据库删除失败！");
            }

        }
        //删除成功的歌曲数目等于要删除的歌曲数目，说明批量删除成功
        if(sum == values.length) {
            return_map.put("msg",true);
        }else {
            return_map.put("msg",false);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
