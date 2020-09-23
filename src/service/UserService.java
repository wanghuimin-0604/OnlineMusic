package service;

import dao.UserDao;
import eneity.User;

/**
 * 业务(相当于中间层）
 */
public class UserService {
        public User login(User loginUser) {
            UserDao userDao = new UserDao();
            User user = userDao.login(loginUser);
            return user;
        }
}
