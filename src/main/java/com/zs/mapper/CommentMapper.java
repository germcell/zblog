package com.zs.mapper;

import com.zs.pojo.Comment;
import com.zs.pojo.MDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Created by zs on 2022/3/11.
 */
@Mapper
public interface CommentMapper {

    @Transactional
    int insert(@Param("comment") Comment comment);

    /**
     * 根据 com_parent_id == -1 and bid = bid 查询
     * @param bid
     * @return
     */
    List<Comment> listCommentsByBid(@Param("bid") Long bid);

    /**
     * 根据 com_parent_id 查询
     * @param comId
     * @return
     */
    List<Comment> listCommentsByComId(@Param("comId") Long comId);

    /**
     * 根据多个 com_parent_id 查询
     * @param comIds
     * @return
     */
    List<Comment> listCommentsByComIds(@Param("list") List<Long> comIds);

    /**
     * 动态条件查询(条件不具备唯一性)
     * @param comment
     * @return
     */
    List<Comment> listCommentsByCondition(@Param("comment") Comment comment,
                                          @Param("date") MDate date);

    /**
     * 动态条件查询(条件具备唯一性)
     * @param comment
     * @return
     */
    Comment getCommentByCondition(@Param("comment") Comment comment);

    /**
     * 批量删除
     * @param comIds
     * @return
     */
    int deleteCommentByComIds(@Param("list") List<Long> comIds);

    /**
     * 按com_id批量修改
     * @param comIds com_id集合
     * @return
     */
    int updateCommentByComIds(@Param("comment") Comment comment,
                              @Param("list") List<Long> comIds);

    /**
     * 根据bid删除
     * @param bid
     * @return
     */
    int deleteCommentByBid(@Param("bid") Integer bid);
}
