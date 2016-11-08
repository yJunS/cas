package org.jasig.cas.thrift.server;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.jasig.cas.entity.Person;
import org.jasig.cas.util.HttpClientUtil;
import org.jasig.cas.util.PasswordHelper;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SyJun on 2016/8/15.
 */
public class UserSupDao extends StubPersonAttributeDao {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private JdbcTemplate myjdbcTemplate;

    public JdbcTemplate getMyjdbcTemplate() {
        return myjdbcTemplate;
    }

    public void setMyjdbcTemplate(JdbcTemplate myjdbcTemplate) {
        this.myjdbcTemplate = myjdbcTemplate;
    }

    public boolean existUser(String uid){
        String sql = "";
        sql = "select username,password,personId from t_user where username = ? and state = 1";
        List list = new ArrayList();
        list = myjdbcTemplate.queryForList(sql,new Object[]{uid});
        if(list.size()!=0)
            return true;
        else
            return false;
    }

    public boolean addNewPerson(String username,String password,String email,String mobile){
        String sql1 = "";
        sql1 = "insert into t_person(name,email,type,mobile1,companyId,state) values(?,?,3,?,-1,1)";
        Integer count;
        Integer count1;
        try {
            myjdbcTemplate.update(sql1,new Object[]{username,email,mobile});
            count = myjdbcTemplate.queryForInt("select LAST_INSERT_ID()");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        PasswordHelper passwordHelper = new PasswordHelper();
        String salt=randomNumberGenerator.nextBytes().toHex();
        org.jasig.cas.entity.User user1 = new org.jasig.cas.entity.User();
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setCredentialsSalt(salt);
        user1 = passwordHelper.encryptPassword(user1);
        String sql = "";
        sql = "insert into t_user(username,password,isLock,state,role,personId,credentialsSalt) values(?,?,0,1,6,?,?)";
        try {
            myjdbcTemplate.update(sql,new Object[]{username,user1.getPassword(),count,salt});
            count1 = myjdbcTemplate.queryForInt("select LAST_INSERT_ID()");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        Map<String,String> map = new HashMap<String,String>();
        map.put("username",username);
        map.put("password",password);
        String as = httpClientUtil.doGet("http://mymeeting.ceecloud.cn/CasUserSync_syncCasUser.action","username="+username+"&password="+password+"&casId="+count1);
        String as1 = httpClientUtil.doGet("http://live.ceecloud.cn:8889/CasUserSync_syncCasUser.action","username="+username+"&password="+password+"&casId="+count1);
//        System.out.println(as);
        return true;
    }

    public boolean isTruePassword(String username,String password){
        String sql = "";
        sql = "select * from t_user where username = ? and state = 1";
        final org.jasig.cas.entity.User user =myjdbcTemplate.queryForObject(sql,new Object[]{username}, new RowMapper<org.jasig.cas.entity.User>() {

            @Override
            public org.jasig.cas.entity.User mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                org.jasig.cas.entity.User user = new org.jasig.cas.entity.User();
                user.setState(rs.getInt("state"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setLock(rs.getBoolean("isLock"));
                user.setCredentialsSalt(rs.getString("credentialsSalt"));
                return user;
            }
        });
        PasswordHelper passwordHelper = new PasswordHelper();
        org.jasig.cas.entity.User user1 = new org.jasig.cas.entity.User();
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setCredentialsSalt(user.getCredentialsSalt());
        user1 = passwordHelper.encryptPassword(user1);
        if(user.getPassword().equals(user1.getPassword()))
            return true;
        else
            return false;
    }

    public boolean editPassword(String username,String newpassword){
        String sql = "";
        sql = "select * from t_user where username = ? and state = 1";
        final org.jasig.cas.entity.User user =myjdbcTemplate.queryForObject(sql,new Object[]{username}, new RowMapper<org.jasig.cas.entity.User>() {

            @Override
            public org.jasig.cas.entity.User mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                org.jasig.cas.entity.User user = new org.jasig.cas.entity.User();
                user.setState(rs.getInt("state"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setLock(rs.getBoolean("isLock"));
                user.setCredentialsSalt(rs.getString("credentialsSalt"));
                return user;
            }
        });
        PasswordHelper passwordHelper = new PasswordHelper();
        org.jasig.cas.entity.User user1 = new org.jasig.cas.entity.User();
        user1.setUsername(username);
        user1.setPassword(newpassword);
        user1.setCredentialsSalt(user.getCredentialsSalt());
        user1 = passwordHelper.encryptPassword(user1);
        if(!user.getPassword().equals(user1.getPassword())) {
            sql = "update t_user set password = ? where username = ?";
            user1.setPassword(newpassword);
            user1 = passwordHelper.encryptPassword(user1);
            myjdbcTemplate.update(sql,new Object[]{user1.getPassword(),username});
        }else {
            return false;
        }
        return true;
    }

    public boolean editUserInfo(String username,String name,String email,String mobile){
        String sql = "";
        try {
            sql = "select * from t_user where username = ? and state = 1";
            final org.jasig.cas.entity.User user =myjdbcTemplate.queryForObject(sql,new Object[]{username}, new RowMapper<org.jasig.cas.entity.User>() {
                @Override
                public org.jasig.cas.entity.User mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    org.jasig.cas.entity.User user = new org.jasig.cas.entity.User();
                    user.setState(rs.getInt("state"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setPersonId(rs.getLong("personId"));
                    user.setLock(rs.getBoolean("isLock"));
                    user.setCredentialsSalt(rs.getString("credentialsSalt"));
                    return user;
                }
            });
            sql = "select * from t_person where id = ?";
            final Person person = myjdbcTemplate.queryForObject(sql, new Object[]{user.getPersonId()}, new RowMapper<Person>() {
                @Override
                public Person mapRow(ResultSet resultSet, int i) throws SQLException {
                    Person person = new Person();
                    person.setId(resultSet.getLong("id"));
                    person.setName(resultSet.getString("name"));
                    person.setEmail(resultSet.getString("email"));
                    person.setMobile1(resultSet.getString("mobile1"));
                    return person;
                }
            });
            sql = "update t_person set name = ?,email = ?,mobile1=? where id = ?";
            if(name.isEmpty())
                name = person.getName();
            if(email.isEmpty())
                email = person.getEmail();
            if(mobile.isEmpty())
                mobile = person.getMobile1();
            myjdbcTemplate.update(sql,new Object[]{name,email,mobile,person.getId()});
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeUser(String username){
        String sql = "";
        try {
            sql = "update t_user set state = 0 where username = ? and state = 1";
            myjdbcTemplate.update(sql,new Object[]{username});
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
