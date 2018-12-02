package com.ran.sns.dao;

import com.ran.sns.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDAO {
	String TABLE_NAME = " login_ticket ";
	String INSERT_FILEDS = " user_id,ticket,expired,status ";
	String SELECT_FILEDS = " id, " + INSERT_FILEDS;

	@Insert({" insert into ", TABLE_NAME, "(", INSERT_FILEDS, ") values " +
			"(#{userId},#{ticket},#{expired},#{status} )"})
	int addTicket(LoginTicket LoginTicket);

	@Select({" select ", SELECT_FILEDS, "from ", TABLE_NAME, "where ticket = #{ticket} "})
	LoginTicket selectByTicket(String ticket);

	/**
	 * 修改t票的状态
	 *
	 * @param ticket
	 * @param status
	 */
	@Update({" update ", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
	void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}
