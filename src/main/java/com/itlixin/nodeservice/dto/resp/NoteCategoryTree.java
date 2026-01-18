package com.itlixin.nodeservice.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteCategoryTree {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Integer level;
    private Long count;
    private Long parentId;

    private List<NoteCategoryTree> children = new ArrayList<>();
}
