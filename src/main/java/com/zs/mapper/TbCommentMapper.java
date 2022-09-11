package com.zs.mapper;

import com.zs.pojo.TbComment;
import com.zs.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-10 21:01
 */
@Mapper
public interface TbCommentMapper {
    /**
     * 查询私信过指定接收用户的用户
     * @param receiveId
     * @return
     */
    List<CommentVO> getByReceiveId(@Param("receiveId") long receiveId);

    /**
     * 查询指定用户的未读私信数量
     * @param receiveId
     * @return
     */
    List<CommentVO> getUnreadByReceiveId(@Param("receiveId") long receiveId);
}
