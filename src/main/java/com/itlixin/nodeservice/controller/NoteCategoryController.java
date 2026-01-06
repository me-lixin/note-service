package com.itlixin.nodeservice.controller;

import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.NoteCategory;
import com.itlixin.nodeservice.service.NoteCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class NoteCategoryController {

    private final NoteCategoryServiceImpl noteCategoryService;

    /**
     * 获取分类树
     * 前端：页面初始化 / 新建笔记时调用
     */
    @GetMapping("/tree")
    public Result<List<NoteCategoryTree>> tree() {
        return Result.ok(noteCategoryService.getTree());
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> create(@RequestBody NoteCategory category) {
        noteCategoryService.create(category);
        return Result.ok();
    }
}

