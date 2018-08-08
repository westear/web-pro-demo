package com.mmall.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Qinyunchan
 * @date Created in 下午5:21 2018/8/8
 * @Modified By
 */

public class ImportExcelUtils {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        //Excel 2003
        if(file.getName().endsWith(EXCEL_XLS)){
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){
            // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断文件是否是excel
     * @throws Exception
     */
    public static void checkExcelVaild(File file) throws Exception{
        if(!file.exists()){
            throw new Exception("文件不存在");
        }
        boolean isFile = file.isFile();
        boolean isCorrectType = file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX);
        if(!isFile){
            throw new Exception("文件不存在");
        }
        if(!isCorrectType){
            throw new Exception("文件不是Excel");
        }
    }

    public static <T> List<T> getData(String filePath, Class<T> clz){
        List<T> list = new ArrayList<>();
        return list;
    }

    public static void main(String[] args) throws Exception {

        String path = ImportExcelUtils.class.getResource("").getPath();
        System.out.println(path);

//        ImportExcelUtils.getData(path, CarReportDTO.class);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 同时支持Excel 2003、2007
            // 创建文件对象
            File excelFile = new File(path+"/test.xlsx");
            // 文件流
            FileInputStream in = new FileInputStream(excelFile);
            checkExcelVaild(excelFile);
            Workbook workbook = getWorkbok(in,excelFile);

            // Sheet的数量
            int sheetCount = workbook.getNumberOfSheets();
            System.out.println(sheetCount);
            /**
             * 设置当前excel中sheet的下标：0开始
             */
            Sheet sheet = workbook.getSheetAt(0);

            //获取总行数
          System.out.println(sheet.getLastRowNum());

            // 为跳过第一行目录设置count
            int count = 0;
            for (Row row : sheet) {
                try {
                    // 跳过第一和第二行的目录
                    if(count < 2 ) {
                        count++;
                        continue;
                    }

                    //如果当前行没有数据，跳出循环
                    if(row.getCell(0).toString().equals("")){
                        return;
                    }

                    //获取总列数(空格的不计算)
                    int columnTotalNum = row.getPhysicalNumberOfCells();
                    System.out.println("总列数：" + columnTotalNum);

                    System.out.println("最大列数：" + row.getLastCellNum());

                    //for循环的，不扫描空格的列
                    int end = row.getLastCellNum();
                    for (int i = 0; i < end; i++) {
                        Cell cell = row.getCell(i);
                        if(cell == null) {
                            System.out.print("null" + "\t");
                            continue;
                        }

                        Object obj = getValue(cell);
                        System.out.print(obj + "\t");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                obj = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

}
