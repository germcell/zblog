package com.zs.mapper;

import com.zs.pojo.TbComment;
import com.zs.vo.ArticleCommentVO;
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

    /**
     * 获取未读私信id
     * @param receiveId 接收者id
     * @param sendId 发送者id
     * @return
     */
    List<CommentVO> getNewUnreadIds(@Param("receiveId") long receiveId, @Param("sendId") long sendId);

    /**
     * 修改receiveId和sendId的对话是否可见
     * @param sendId 发送者id
     * @param receiveId 接收者id
     * @return
     */
    int updateBySendIdAndReceiveId(@Param("sendId") long sendId, @Param("receiveId") long receiveId);

    /**
     * 查询文章的根评论
     * @param bid
     * @return
     */
    List<ArticleCommentVO> pageRootComments(@Param("bid") long bid);

    /**
     * 根据消息id查询
     * @param id
     * @return
     */
    ArticleCommentVO getCommentById(@Param("id") long id);

    /**
     * 查询指定文章的指定父评论的一级子评论
     * @param bid 文章id
     * @param pid 父评论id
     * @return
     */
    List<ArticleCommentVO> listCommentsByPid(@Param("bid") long bid, @Param("pid") long pid);

    /**
     * 插入
     * @param newComment
     * @return
     */
    int insert(@Param("comment") TbComment newComment);
}
