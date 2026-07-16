package com.mehmet.AssignmentTA.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtil {
    private String path;
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtil(String path, String sheetName) {
        this.path = path;
        try {
            File file = new File(path);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
                
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Email");
                headerRow.createCell(1).setCellValue("Case Name");
                headerRow.createCell(2).setCellValue("Status");
                
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
            } else {
                FileInputStream fileInputStream = new FileInputStream(path);
                workbook = WorkbookFactory.create(fileInputStream);
                sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                }
                
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    headerRow = sheet.createRow(0);
                }
                headerRow.createCell(0).setCellValue("Email");
                headerRow.createCell(1).setCellValue("Case Name");
                headerRow.createCell(2).setCellValue("Status");
                
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel file operations failed: " + e.getMessage(), e);
        }
    }

    public void setCellData(String value, int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }
            cell.setCellValue(value);

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Writing to Excel failed: " + e.getMessage(), e);
        }
    }
}
