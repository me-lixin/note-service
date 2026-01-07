package com.itlixin.nodeservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note_share")
public class NoteShare {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long noteId;

    private String shareCode;

    private String titleSnapshot;

    private String contentSnapshot;

    private LocalDateTime expireTime;

    private Integer viewCount;

    private LocalDateTime createTime;
}
