package com.ran.sns.dao;

import com.ran.sns.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentDAO {
	String TABLE_NAME = " comment ";
	String INSERT_FILEDS = " content,user_id,entity_id,entity_type,created_date,status ";
	String SELECT_FILEDS = " id "+INSERT_FILEDS;

	@Insert({" insert into ",TABLE_NAME," ( ",INSERT_FILEDS," ) values ( " +
			" #{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
	int addComment(Comment comment);

	@Select({" select ",SELECT_FILEDS," from ",TABLE_NAME," where id = #{id}"})
	Comment selectById(int id);

	@Select({" select ",SELECT_FILEDS," from ",TABLE_NAME," where entity_id = #{entity_id} and entity_type = #{entity_type}"})
	List<Comment> selectByEntity(int entityId, int entityType);

	@Select({" select count(id) from ",TABLE_NAME," where entity_id = #{entity_id} and entity_type = #{entity_type}"})
	int getCommentCount(int entityId, int entityType);

	@Select({" select count(id) from ",TABLE_NAME," where user_id = #{userId} "})
	int getUserCount(int userId);
}
