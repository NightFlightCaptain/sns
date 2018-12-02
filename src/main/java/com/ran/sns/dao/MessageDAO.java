package com.ran.sns.dao;

import com.ran.sns.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDAO {
	String TABLE_NAME = " message ";
	String INSERT_FILEDS = " from_id,to_id,content,created_date,has_read,conversation_id ";
	String INSERT_FILEDS_WITHOUT_CREATED_DATE = " from_id,to_id,content,has_read,conversation_id ";
	String SELECT_FILEDS = " id, " + INSERT_FILEDS;

	@Insert({" insert into ", TABLE_NAME," ( ",INSERT_FILEDS, ") values ( " +
			"#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})" })
	int addMessage(Message message);

	@Select({" select ",SELECT_FILEDS," from ",TABLE_NAME, "where id = #{id} "})
	Message selectMessageById(int id);

	@Select({" select ",SELECT_FILEDS," from ",TABLE_NAME, "where conversation_id = #{conversationId} " +
			"order by created_date limit #{offset},#{limit} "})
	List<Message> getConversationDetail(@Param("conversationId") String conversationId,
	                                    @Param("offset") int offset,
	                                    @Param("limit") int limit);

	@Select({"select ",INSERT_FILEDS_WITHOUT_CREATED_DATE,",count(id) as id,max(created_date) as created_date " +
			"from",TABLE_NAME,"where to_id = #{userId} or from_id = #{userId}" +
			" group by conversation_id order by created_date desc limit  #{offset},#{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,
	                                  @Param("offset") int offset,
	                                  @Param("limit") int limit);

	@Select({"select count(id) from ",TABLE_NAME," where conversation_id = #{conversationId}"})
	int getConversationUnreadCount(@Param("conversationId")String conversationId);

}
