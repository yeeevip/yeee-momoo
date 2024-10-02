package vip.yeee.memoo.demo.websocket.mapper;

import vip.yeee.memoo.demo.websocket.po.Staff;

public interface LoginMapper {
	Staff getpwdbyname(String name);
	Staff getnamebyid(long id);
}
