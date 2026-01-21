package com.itlixin.nodeservice.utils;

import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.entity.NoteCategory;

import java.util.*;

public class TreeBuilder {

    private TreeBuilder() {}

    public static List<NoteCategoryTree> build(List<NoteCategory> categories, Map<Long, Long> longIntegerMap) {

        Map<Long, NoteCategoryTree> nodeMap = new HashMap<>();
        List<NoteCategoryTree> roots = new ArrayList<>();
        Long count = 0L;
        // 1. entity -> node
        for (NoteCategory category : categories) {
            NoteCategoryTree node = new NoteCategoryTree();
            Long tmplCount = longIntegerMap.getOrDefault(category.getId(), 0L);
            count += tmplCount;
            node.setId(category.getId());
            node.setName(category.getName());
            node.setLevel(category.getLevel());
            node.setCount(tmplCount);
            node.setParentId(category.getParentId());
            nodeMap.put(category.getId(), node);
        }

        // 2. 构建父子关系
        for (NoteCategory category : categories) {
            NoteCategoryTree node = nodeMap.get(category.getId());
            if (category.getParentId() == 0) {
                roots.add(node);
            } else {
                NoteCategoryTree parent = nodeMap.get(category.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        roots.get(0).setCount(count);
        return roots;
    }
}
