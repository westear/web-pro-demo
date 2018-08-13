package com.mmall.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qinyunchan
 * @date Created in 下午5:56 2018/8/12
 * @Modified By
 */

public class ImportExcelUtilsV2 {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ImportExcelUtilsV2.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /** 200个线程 */
    private static int executeThreads = 200;

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

    public static void main(String[] args) throws InterruptedException{
        Workbook workbook = null;
        String path = ImportExcelUtils.class.getResource("").getPath();
        String filePath = path + "/test.xlsx";

        ExecutorService exec = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(executeThreads);
        try {
            // 同时支持Excel 2003、2007
            // 创建文件对象
            File excelFile = new File(filePath);
            // 文件流
            FileInputStream in = new FileInputStream(excelFile);
            checkExcelVaild(excelFile);
            workbook = getWorkbok(in, excelFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        Sheet sheet = workbook.getSheetAt(0);
        ExcelDataUtil excelDataUtil = new ExcelDataUtil(sheet);
        for(int i = 0; i < executeThreads; i++){
            exec.execute(excelDataUtil);
            countDownLatch.countDown();
        }

        countDownLatch.await();
        exec.shutdown();

        System.out.println("输出数据：");
        List<LinkedList<Object>> rowList = excelDataUtil.getRowList();
        for (LinkedList<Object> item : rowList){
            if(item != null && item.size() > 0){
                for(int i = 0; i < item.size(); i++){
                    if(item.get(i) != null){
                        System.out.print(item.get(i).toString() + "\t");
                    }
                }
            }
        }

    }
}
