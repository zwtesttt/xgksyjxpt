package com.xgksyjxpt.xgksyjxpt.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class GetCellValue {
    //获取列值的工具方法
    public static String getCellValue(HSSFCell cell) {
        String v="";
        if (cell.getCellType().getCode() == 1) {
            v=cell.getStringCellValue();
        } else if (cell.getCellType().getCode() == 0) {
            v=cell.getNumericCellValue()+"";
        } else if (cell.getCellType().getCode() == 4) {
            v=cell.getBooleanCellValue() + "";
        } else if (cell.getCellType().getCode() == 2) {
            v=cell.getCellFormula() + "";
        } else {
            v="";
        }
        return v;
    }
    public static String getXSSFCellValue(XSSFCell cell) {
        String v="";
        if (cell.getCellType().getCode() == 1) {
            v=cell.getStringCellValue();
        } else if (cell.getCellType().getCode() == 0) {
            v=cell.getNumericCellValue()+"";
        } else if (cell.getCellType().getCode() == 4) {
            v=cell.getBooleanCellValue() + "";
        } else if (cell.getCellType().getCode() == 2) {
            v=cell.getCellFormula() + "";
        } else {
            v="";
        }
        return v;
    }
}
