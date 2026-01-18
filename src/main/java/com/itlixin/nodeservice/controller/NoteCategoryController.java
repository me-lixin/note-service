package com.itlixin.nodeservice.controller;

import com.itlixin.nodeservice.dto.resp.NoteCategoryTree;
import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.entity.NoteCategory;
import com.itlixin.nodeservice.service.NoteCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Note 分类API
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class NoteCategoryController {

    private final NoteCategoryServiceImpl noteCategoryService;

    @GetMapping("/tree")
    public Result<List<NoteCategoryTree>> tree() {
        return Result.ok(noteCategoryService.getTree());
    }


    @PutMapping
    public Result<Integer> update(@RequestBody NoteCategory category) {
        return Result.ok(noteCategoryService.update(category));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Long id) {
        return Result.ok(noteCategoryService.delete(id));
    }
}

