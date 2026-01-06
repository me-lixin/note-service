package com.itlixin.nodeservice.controller;

import com.itlixin.nodeservice.dto.resp.Result;
import com.itlixin.nodeservice.entity.Note;
import com.itlixin.nodeservice.service.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;

    @PostMapping
    public Result<Void> create(@RequestBody Note req) {
        noteService.create(req);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<List<Note>> list(@RequestAttribute("userId") Long userId) {
        return Result.ok(noteService.list(userId));
    }

    @GetMapping("/{id}")
    public Result<Note> get(@PathVariable Long id,
                            @RequestAttribute("userId") Long userId) {
        return Result.ok(noteService.get(id, userId));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Note req) {
        noteService.update(req);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id,
                               @RequestAttribute("userId") Long userId) {
        noteService.delete(id, userId);
        return Result.ok();
    }
}

