package com.itlixin.nodeservice.utils;

import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.entity.NoteCategory;

import java.util.*;

public class TreeBuilder {

    private TreeBuilder() {}

    public static List<NoteCategoryTree> build(
            List<NoteCategory> categories,
            Map<Long, Long> countMap
    ) {

        Map<Long, NoteCategoryTree> nodeMap = new HashMap<>();
        List<NoteCategoryTree> roots = new ArrayList<>();

        // 1. entity -> tree node
        for (NoteCategory category : categories) {
            NoteCategoryTree node = new NoteCategoryTree();
            node.setId(category.getId());
            node.setName(category.getName());
            node.setLevel(category.getLevel());
            node.setParentId(category.getParentId());

            // 先只设置“自身 count”
            node.setCount(countMap.getOrDefault(category.getId(), 0L));
            nodeMap.put(category.getId(), node);
        }

        // 2. 构建父子关系
        for (NoteCategory category : categories) {
            NoteCategoryTree node = nodeMap.get(category.getId());
            if (category.getParentId() == 0L) {
                roots.add(node);
            } else {
                NoteCategoryTree parent = nodeMap.get(category.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }

        // 3. 自底向上计算 count
        for (NoteCategoryTree root : roots) {
            sumChildrenCount(root);
        }

        return roots;
    }
    private static long sumChildrenCount(NoteCategoryTree node) {
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            return node.getCount();
        }

        long total = node.getCount(); // 如果你希望父目录包含自身笔记
        // 如果父目录不应该有自己的笔记，可改成：long total = 0;

        for (NoteCategoryTree child : node.getChildren()) {
            total += sumChildrenCount(child);
        }

        node.setCount(total);
        return total;
    }
}
