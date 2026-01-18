package com.itlixin.nodeservice.utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileParseUtil {

    private FileParseUtil() {
    }

    /**
     * 解析上传文件为文本内容
     */
    public static String parse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("文件名非法");
        }

        String suffix = getSuffix(filename);

        try {
            switch (suffix) {
                case "md":
                case "txt":
                    return readText(file);
                case "docx":
                case "doc":
                    return readWord(file);
                default:
                    throw new IllegalArgumentException("不支持的文件类型: " + suffix);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件解析失败", e);
        }
    }

    private static String readText(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private static String readWord(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("文件名为空");
        }
        if (filename.endsWith(".docx")) {
            return readDocx(file);
        } else if (filename.endsWith(".doc")) {
            return readDoc(file);
        } else {
            throw new IllegalArgumentException("不支持的文件类型：" + filename);
        }
    }

    // 解析 .docx 文件
    private static String readDocx(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            for (IBodyElement element : doc.getBodyElements()) {
                if (element instanceof XWPFParagraph p) {
                    String text = p.getText();
                    if (text != null && !text.isBlank()) {
                        sb.append(text).append("\n");
                    }
                } else if (element instanceof XWPFTable table) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                String text = p.getText();
                                if (text != null && !text.isBlank()) {
                                    sb.append(text).append("\n");
                                }
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    // 解析 .doc 文件
    private static String readDoc(MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (HWPFDocument doc = new HWPFDocument(file.getInputStream())) {
            Range range = doc.getRange();

            // 遍历段落
            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph p = range.getParagraph(i);
                String text = p.text();
                if (text != null && !text.isBlank()) {
                    sb.append(text).append("\n");
                }
            }

            // 遍历表格
            TableIterator tableIterator = new TableIterator(range);
            while (tableIterator.hasNext()) {
                Table table = tableIterator.next();
                for (int r = 0; r < table.numRows(); r++) {
                    TableRow row = table.getRow(r);
                    for (int c = 0; c < row.numCells(); c++) {
                        TableCell cell = row.getCell(c);
                        for (int p = 0; p < cell.numParagraphs(); p++) {
                            Paragraph paragraph = cell.getParagraph(p);
                            String text = paragraph.text();
                            if (text != null && !text.isBlank()) {
                                sb.append(text).append("\n");
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String getSuffix(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? "" : filename.substring(index + 1).toLowerCase();
    }
}
