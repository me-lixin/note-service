package com.itlixin.nodeservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itlixin.nodeservice.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
    @Select(
            "SELECT * " +
                    "FROM note " +
                    "WHERE user_id = #{userId} " +
                    "AND MATCH(title, content) " +
                    "AGAINST (#{keyword} IN NATURAL LANGUAGE MODE) " +
                    "ORDER BY update_time DESC"
    )
    IPage<Note> search(Page<Note> page,
                       @Param("userId") Long userId,
                       @Param("keyword") String keyword);
}

