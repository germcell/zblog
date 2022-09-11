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

    /**
     * 根据id集合获取记录
     * @param ids
     * @return
     */
    List<TbComment> getByIds(@Param("ids") List<Long> ids);

    /**
     * 批量修改私信状态
     * @param cIds
     * @param isRead
     * @return
     */
    int updateStatusByIds(@Param("ids") List<Long> cIds, @Param("isRead") int isRead);

    /**
     * 获取回复
     * @param sendId
     * @param receiveId
     * @return
     */
    List<TbComment> getCommunication(@Param("sendId") long sendId, @Param("receiveId") long receiveId);
}
