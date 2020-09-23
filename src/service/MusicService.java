package service;

import dao.MusicDao;
import eneity.Music;

import java.util.List;
public class MusicService {
    public List<Music> findMusic(){
        MusicDao musicDao = new MusicDao();
        List<Music> musicList = musicDao.findMusic();
        return musicList;
    }
}
