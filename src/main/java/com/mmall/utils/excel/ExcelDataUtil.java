package com.mmall.utils.excel;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Qinyunchan
 * @date Created in 下午6:01 2018/8/12
 * @Modified By
 */

public class ExcelDataUtil implements Runnable {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataUtil.class);

    private Sheet sheet;
    private long rowTotalCount = 0L ;
    private AtomicLong rowIndex = null;
    private List<LinkedList<Object>> rowList = null;

    public ExcelDataUtil(Sheet sheet){
        this.sheet = sheet;
        this.rowIndex = new AtomicLong(1L);
        this.rowTotalCount = sheet.getLastRowNum() + 1;
        rowList = Lists.newArrayListWithCapacity(sheet.getLastRowNum() + 1);
    }

    @Override
    public void run() {
        try{
            while (rowIndex.get() < rowTotalCount){
                System.out.println(Thread.currentThread().getId()+":"+Thread.currentThread().getName());
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                synchronized (rowList){
                    traversalRow(sheet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 遍历每一个Sheet的行
     * @param sheet
     */
    public void traversalRow(Sheet sheet) {
        for(rowIndex.intValue(); rowIndex.intValue() < rowTotalCount; rowIndex.getAndIncrement()){
            Row row = sheet.getRow(rowIndex.intValue());
            try {
                //获取总列数(空格的不计算)
                //遍历每一行的单元格
                LinkedList<Object> cellList = traversalCell(row, row.getLastCellNum());
                if(cellList != null ||cellList.size() > 0){
                    rowList.add(cellList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<LinkedList<Object>> getRowList(){
        return rowList;
    }

    public long getRowIndex(){
        return rowIndex.get();
    }

    /**
     * 遍历每一行的单元格
     * @param row
     * @return 单元值List
     */
    private static LinkedList<Object> traversalCell(Row row, int cellTotalNum) {
        //保存单元格数据顺序
        LinkedList<Object> cellList = Lists.newLinkedList();
        //for循环的，不扫描空格的列
        for (int i = 0; i < cellTotalNum; i++) {
            Cell cell = row.getCell(i);
            if(cell == null) {
                continue;
            }

            Object obj = getValue(cell);
            cellList.add(obj);
        }
        return cellList;
    }

    /**
     * 读取单元格的值
     * @param cell
     * @return
     */
    private static Object getValue(Cell cell) {
        Object obj = null;
        if(StringUtils.isEmpty(cell)){
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                obj = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    if (cell.getCellStyle().getDataFormat() == 58) {
                        // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        obj = sdf.format(date);
                    }
                }else {
                    obj = cell.getNumericCellValue();
                }
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
