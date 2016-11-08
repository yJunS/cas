package org.jasig.cas.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jasig.cas.thrift.server.User;




public interface UserMapper {
   
	/***
	 * 根据用户名称查询用户信息
	 * @param username
	 * @return
	 */
	User loadUserByUsername(@Param("userName")String username);

	/***
	 * 分页查找用户列表
	 * @param i
	 * @param rows
	 * @return
	 */
	List<User> findUserList(@Param("page")int page, @Param("rows")int rows,
			@Param("username")String username,
			@Param("startTime")Date startTime,@Param("endTime")Date endTime,
			@Param("state")Integer state);
	

	/***
	 * 检查用户昵称是否存在
	 * @param nickName
	 * @param userId
	 * @return
	 */
	Integer checkNickName(@Param("nickName")String nickName, @Param("userId")Integer userId);

	/***
	 * 检查用户名是否存在
	 * @param userName
	 * @param userId
	 * @return
	 */
	Integer checkUsername(@Param("username")String userName, @Param("userId")Integer userId);
	/***
	 * 检查手机号码是否存在
	 * @param mobile
	 * @param userId
	 * @return
	 */
	Integer checkMobile(@Param("mobile")String mobile, @Param("userId")Integer userId);
	/***
	 * 检查邮箱是否存在
	 * @param email
	 * @param userId
	 * @return
	 */
	Integer checkEmail(@Param("email")String email, @Param("userId")Integer userId);

	/**
	 * 新增用户
	 * @param user
	 */
	void addUser(User user);

	/***
	 * 更新用户信息
	 * @param user
	 */
	void editUser(User user);

	User findUserByUserName(@Param("userName")String userName);

	boolean checkPassword(@Param("password")String password, int id);

	int findUserCount();
}