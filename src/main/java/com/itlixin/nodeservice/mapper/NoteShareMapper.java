package com.itlixin.nodeservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itlixin.nodeservice.entity.NoteShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoteShareMapper extends BaseMapper<NoteShare> {

    @Update("UPDATE note_share SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") Long id);
}

