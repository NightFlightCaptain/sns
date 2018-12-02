package com.ran.sns.dao;

import com.ran.sns.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDAO {

	String TABLE_NAME = " question ";
	String INSERT_FILEDS = " title,content,user_id,created_date,comment_count ";
	String SELECT_FILEDS = " id, " + INSERT_FILEDS;

	@Insert({" insert into ", TABLE_NAME, " ( ", INSERT_FILEDS, ")" +
			" values (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
	int addQuestion(Question question);

	@Select({" select ", SELECT_FILEDS, " from ", TABLE_NAME, " where id = #{id}"})
	Question selectById(int id);

	List<Question> selectLatestQuestions(@Param("userId") int userId,
	                                     @Param("offset") int offset,
	                                     @Param("limit") int limit);

	@Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id = #{id}"})
	int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);
}
