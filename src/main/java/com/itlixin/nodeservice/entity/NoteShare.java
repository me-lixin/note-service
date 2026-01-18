package com.itlixin.nodeservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note_share")
public class NoteShare {
    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long noteId;
    private Long userId;

    private String shareCode;
    private String url;

    private String titleSnapshot;

    private String contentSnapshot;

    private LocalDateTime expireTime;

    private Integer viewCount;

    private LocalDateTime createTime;
}
