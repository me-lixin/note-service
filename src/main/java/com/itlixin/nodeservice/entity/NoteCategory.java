package com.itlixin.nodeservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note_category")
public class NoteCategory {

    /** 分类ID */
    private Long id;

    /** 父级ID，0为根节点 */
    private Long parentId;

    /** 层级：1-一级，2-二级，3-三级 */
    private Integer level;

    /** 分类名称 */
    private String name;
    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

