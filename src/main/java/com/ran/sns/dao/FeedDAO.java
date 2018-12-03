package com.ran.sns.dao;

import com.ran.sns.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedDAO {
	String TABLE_NAME = " feed ";
	String INSERT_FILEDS = " user_id,created_date,data,type ";
	String SELECT_FILEDS = " id, " + INSERT_FILEDS;

	@Insert({" insert into ", TABLE_NAME, "(", INSERT_FILEDS, ") values (" +
			"#{userId},#{createdDate},#{data},#{type})"})
	int addFeed(Feed feed);

	@Select({" select ", SELECT_FILEDS, "from", TABLE_NAME, "where id=#{id}"})
	Feed selectFeedById(int id);

	@Select({" select ", SELECT_FILEDS, "from", TABLE_NAME, "where user_id=#{userId}"})
	List<Feed> selectFeedByUserId(int userId);

	List<Feed> selectUserFeeds(@Param("userIds")List<Integer> userIds,
	                          @Param("count")int count);
}
