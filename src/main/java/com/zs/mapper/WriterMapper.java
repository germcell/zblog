package com.zs.mapper;

import com.zs.pojo.Writer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Created by zs on 2022/4/21.
 */
@Mapper
public interface WriterMapper {

    /**
     * 动态条件查询
     * @param writer
     * @return
     */
    List<Writer> listWriterByCondition(@Param("writer") Writer writer) throws Exception;

    /**
     * 插入记录
     * @param writer
     * @return
     */
    int insert(@Param("writer") Writer writer);

    /**
     * 条件更新
     * @param writer
     * @return
     */
    int updateWriterByUid(@Param("writer") Writer writer);
}
