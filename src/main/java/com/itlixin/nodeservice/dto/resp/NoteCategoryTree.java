package com.itlixin.nodeservice.dto.resp;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NoteCategoryTree {

    private Long id;
    private String name;
    private Integer level;

    private List<NoteCategoryTree> children = new ArrayList<>();
}
