package com.itlixin.nodeservice.dto;

import lombok.Data;

/**
 * @description: 用于存储统计每个目录下有多少note
 * @author: LiXin
 * @create: 2026-01-18 17:59
 **/
@Data
public class CategoryCountDTO {

    private Long categoryId;

    private Long cnt; // COUNT(*) 在 MySQL / MP 中通常是 Long
}
