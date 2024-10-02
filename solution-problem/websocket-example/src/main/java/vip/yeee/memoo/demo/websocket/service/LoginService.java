package vip.yeee.memoo.demo.websocket.service;


public interface LoginService {
	String getpwdbyname(String name);
	Long getUidbyname(String name);
	String getnamebyid(long id);
}
