package org.jasig.cas.util;

import org.jasig.cas.entity.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;

	public User encryptPassword(User userFormMap) {
		String salt=randomNumberGenerator.nextBytes().toHex();
		salt = userFormMap.getCredentialsSalt();
		userFormMap.setCredentialsSalt(salt);
		String newPassword = new SimpleHash(algorithmName, userFormMap.getPassword(), ByteSource.Util.bytes(userFormMap.getUsername()+salt), hashIterations).toHex();
		userFormMap.setPassword(newPassword);
		return userFormMap;
	}
	public static void main(String[] args) {
		PasswordHelper passwordHelper = new PasswordHelper();
		User userFormMap = new User();
		userFormMap.setPassword("123456789");
		userFormMap.setUsername("sunhong");
		passwordHelper.encryptPassword(userFormMap);
		System.out.println(userFormMap);
	}
}
