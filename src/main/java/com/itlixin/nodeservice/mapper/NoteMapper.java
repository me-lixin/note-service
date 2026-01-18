package com.itlixin.nodeservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itlixin.nodeservice.dto.CategoryCountDTO;
import com.itlixin.nodeservice.entity.Note;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    @Select(
            "SELECT category_id AS categoryId, " +
                    "       COUNT(*) AS cnt " +
                    "FROM note " +
                    "GROUP BY category_id"
    )
    List<CategoryCountDTO> countByCategory();
}

