package dao;

import eneity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//有关音乐的操作
public class MusicDao {
    /**
     * 查询全部歌单
     * @return list<Music> 音乐列表
     */
    public List<Music> findMusic() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Music> musiclist = new ArrayList<>();
        try {
            String sql = "select * from music";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musiclist.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, rs);
        }
        return musiclist;
    }

    /**
     * 根据歌曲id查找指定歌曲
     * id是唯一的，因此只会查到一个歌曲
     */
    public  Music findMusicById(int id) {
        Music music = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select * from music where id=?";
            //获取连接
            connection = DBUtils.getConnection();
            //编译sql
            statement = connection.prepareStatement(sql);
            //具体传的id
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            DBUtils.getClose(connection, statement, rs);
        }
        return music;
    }

    /**
     * 根据关键字查询歌单
     *
     */
    public  List<Music> ifMusic(String str) {
        List<Music> musics = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select * from music where title like '%" + str + "%'";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                //说明这段代码是重复的
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            DBUtils.getClose(connection, statement, rs);
        }
        return musics;
    }

    /**
     * 上传歌曲到数据库中
     */
    public  static int insert(String title, String singer, String time, String url,
                      int userid) {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pst=null;
        int number = 0;
        try {
            String sql="insert into music(title,singer,time,url,userid) values(?,?,?,?,?)";
            pst=conn.prepareStatement(sql);
            pst.setString(1,title);
            pst.setString(2,singer);
            pst.setString(3,time);
            pst.setString(4,url);
            pst.setInt(5,userid);
            //executeUpdate返回的是一个整数：受影响的行数
            number=pst.executeUpdate();
            return number;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DBUtils.getClose(conn, pst, null);
        }
        return 0;
    }

    /**
     * 根据id删除音乐
     * 如果要删除的歌曲添加到了喜欢列表中，那么也要在喜欢列表中将这个歌曲删除
     * @param
     */
    public  int deleteMusicById(int id){
        Connection connection=null;
        PreparedStatement statement=null;
        try{
            //在music中删除
            String sql="delete from music where id=?";
            connection=DBUtils.getConnection();
            statement=connection.prepareStatement(sql);
            statement.setInt(1,id);
            int ret=statement.executeUpdate();
            if(ret == 1) {
                //删除music表当中的数据成功
                //判断要删除的这个歌曲在喜欢列表中
                //在的话，就删除
                if(findLoveMusicOnDel(id)) {
                    int ret2 = removeLoveMusicOnDelete(id);
                    if(ret2 == 1) {
                        //删除成功
                        return 1;
                    }
                }
                //表示这首歌没有被添加到lovemusic这张表当中
                //删除成功
                return 1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,statement,null);
        }
        return 0;
    }

    /**
     * 判断中间表（lovemusic)是否包含某个id
     * 判断某个歌曲是不是被添加到了喜欢列表中
     * @param id
     * @return
     */
    public  boolean findLoveMusicOnDel(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,preparedStatement,resultSet);
        }
        return false;
    }

    /**
     * 根据id删除中间表的数据
     *
     * @param musicId
     * @return
     */
    public  int removeLoveMusicOnDelete(int musicId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "delete from lovemusic where music_id=?";
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,musicId);
            int ret = preparedStatement.executeUpdate();
            //如果删除成功
            if(ret == 1) {
                return ret;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,preparedStatement,null);
        }
        return 0;
    }
    /**
     * 判断某个音乐是否被添加到了喜欢列表
     * 添加音乐到喜欢列表之前，先看下之前有没有添加过
     */
    public boolean findMusicByMusicId(int user_id,int music_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement("select * from lovemusic where music_id=? and user_id=?");
            ps.setInt(1,music_id);
            ps.setInt(2,user_id);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            DBUtils.getClose(conn, ps, rs);
        }
        return false;
    }

    /**
     * 把歌曲插入到喜欢列表中
     */
    public boolean insertLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into lovemusic(user_id, music_id) VALUES (?,?)";
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,musicId);
            //在添加歌曲到喜欢列表之前先判断喜欢列表中是否包含该歌曲
            //不包含的话，我们再添加
            if(!findMusicByMusicId(userId, musicId)) {
                int ret = preparedStatement.executeUpdate();
                if (ret == 1) {
                    return true;
                }else{
                    return false;
                }
            }
            //喜欢列表中已经包含了该歌曲，就不必再进行添加了
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,preparedStatement,null);
        }
        return false;
    }

    /**
     * 移除歌曲从喜欢列表中
     * 但是音乐表中是不能删除的
     * 根据userId和musicId来进行删除
     */
    public int removeLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "delete from lovemusic where user_id=? and music_id=?";
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,musicId);
            int ret = preparedStatement.executeUpdate();
            if(ret == 1) {
                return ret;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(connection,preparedStatement,null);
        } return 0;
    }
    /**
     * 查询用户喜欢的所有音乐
     * 也就是喜欢列表中的所有数据
     * 那么，根据user_id来进行查找
     * @param user_id
     * @return
     */
    public  List<Music> findLoveMusic(int user_id){
        List<Music> musics = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql="select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=?";
            //String sql=" select * from music where music.id in (select music_id from lovemusic where user_id = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while(rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.getClose(conn, ps, rs);
        }
        return musics;
    }
    /**
     * 根据关键字查询用户的喜欢列表
    * @param str
    * @return
        */
    public List<Music> ifMusicLove(String str,int user_id){
        List<Music> musics = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql="select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=? and title like '%"+str+"%'";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while(rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserid(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            DBUtils.getClose(conn, ps, rs);
        }
        return musics;
    }
    public static void main(String[] args) {
        MusicDao dao=new MusicDao();
        int ret=dao.insert("李四的歌","赵三","2020-07-31","music/张三的歌",1);
        System.out.println(ret);


    }
}
