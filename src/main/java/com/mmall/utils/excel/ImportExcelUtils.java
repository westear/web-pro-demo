package com.mmall.utils.excel;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Qinyunchan
 * @date Created in 下午5:21 2018/8/8
 * @Modified By
 */

public class ImportExcelUtils {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ImportExcelUtils.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    private static final String CELL_VALUE_TYPE_STRING = "String";
    private static final String CELL_VALUE_TYPE_DOUBLE = "Double";

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
     * 获得导入实体类列表
     * @param filePath
     * @param clz
     * @param <T>
     * @return List<List<T>>
     */
    public static <T> List<List<T>> getEntityDataList(String filePath, Class<T> clz) {
        List<List<T>> dataList = null;
        try{
            // 同时支持Excel 2003、2007
            // 创建文件对象
            File excelFile = new File(filePath);
            // 文件流
            FileInputStream in = new FileInputStream(excelFile);
            checkExcelVaild(excelFile);
            Workbook workbook = getWorkbok(in,excelFile);

            // Sheet的数量
            int sheetCount = workbook.getNumberOfSheets();

            dataList = Lists.newArrayListWithCapacity(sheetCount);

            for(int i = 0; i < sheetCount; i++){
                Sheet sheet = workbook.getSheetAt(i);
                //获取总行数
                int rowTotalCount = sheet.getLastRowNum() + 1;
                //每个sheet的数据
                List<LinkedList<Object>> sheetList = traversalRow(sheet, rowTotalCount);
                //数据处理
                List<T> sheetEntityList = matchEntity(sheetList, clz);

                dataList.add(sheetEntityList);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    /**
     * 判断文件是否是excel
     * @throws Exception
     */
    private static void checkExcelVaild(File file) throws Exception{
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

    /**
     * 遍历每一个Sheet的行
     * @param sheet
     */
    private static List<LinkedList<Object>> traversalRow(Sheet sheet, int rowTotalCount) {
        List<LinkedList<Object>> rowList = Lists.newArrayListWithCapacity(rowTotalCount);
        // 跳过第一的目录
        for (int i = 1; i < rowTotalCount; i++) {
            Row row = sheet.getRow(i);
            try {
                //获取总列数(空格的不计算)
                int columnTotalNum = row.getPhysicalNumberOfCells();
                System.out.println("总列数：" + columnTotalNum);
                System.out.println("最大列数：" + row.getLastCellNum());
                //遍历每一行的单元格
                LinkedList<Object> cellList = traversalCell(row, row.getLastCellNum());
                if(cellList != null ||cellList.size() > 0){
                    rowList.add(cellList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rowList;
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
                System.out.print("null" + "\t");
                continue;
            }

            Object obj = getValue(cell);
            System.out.print(obj + "\t");
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

    /**
     * 根据excel数据给实体类赋值
     * @param sheetList 单页sheet的数据
     * @param clz 实体类类型
     * @param <T> 实体类实例
     * @return 单页 实体类 列表
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private static <T> List<T> matchEntity(List<LinkedList<Object>> sheetList, Class<T> clz)
            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<T> entityList = Lists.newArrayListWithCapacity(sheetList.size());
        for(LinkedList<Object> row : sheetList){
            if(row.size() == 0){
                continue;
            }
            T t = clz.newInstance();
            //成员变量的值
            Object entityFieldValue = null;
            Field[] fields = t.getClass().getDeclaredFields();
            for(int i = 0; i < fields.length; i++){
                fields[i].setAccessible(true);
                Field field = fields[i];
                String fieldName = field.getName();
                boolean fieldHasAnno = field.isAnnotationPresent(IsNeed.class);
                if(fieldHasAnno){
                    IsNeed annotation = field.getAnnotation(IsNeed.class);
                    boolean isNeed = annotation.isNeeded();
                    if(isNeed){
                        int cellIndex = annotation.sort();
                        Object cellValue = row.get(cellIndex);
                        String fieldType = field.getType().getName();
                        //获得实体类真实值
                        entityFieldValue = getEntityFieldValue(cellValue, fieldType);
                        //给实体类赋值
                        PropertyUtils.setProperty(t, fieldName, entityFieldValue);
                    }
                }
            }
            entityList.add(t);
        }
        return entityList;
    }

    /**
     * 根据实体类属性转换excle单元格类型
     * @param cellValue Object 单元格值
     * @param fieldType 实体类属性类型
     * @return Object 实体类属性真实值
     */
    private static Object getEntityFieldValue(Object cellValue, String fieldType) {
        Object realValue = null;
        if(StringUtils.isEmpty(cellValue)){
            return realValue;
        }
        String cellType = cellValue.getClass().getSimpleName();

        switch (fieldType){
            case "char":
            case "java.lang.Short":
            case "java.lang.Character":
            case "java.lang.String":
                realValue = cellValue.toString();
                break;
            case "java.util.Date":
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(cellValue.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                realValue = StringUtils.isEmpty(cellValue) ? null : date;
                break;
            case "int":
            case "java.lang.Integer":
                if(CELL_VALUE_TYPE_DOUBLE.equals(cellType)){
                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    realValue = StringUtils.isEmpty(cellValue) ? null : Integer.parseInt(decimalFormat.format(cellValue));
                }else if(CELL_VALUE_TYPE_STRING.equals(cellType)){
                    realValue = StringUtils.isEmpty(cellValue) ? null : Integer.parseInt(cellValue.toString());
                }
                break;
            case "java.lang.Long":
                if(CELL_VALUE_TYPE_DOUBLE.equals(cellType)){
                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    realValue = StringUtils.isEmpty(cellValue) ? null : Long.parseLong(decimalFormat.format(cellValue));
                }else if(CELL_VALUE_TYPE_STRING.equals(cellType)){
                    realValue = StringUtils.isEmpty(cellValue) ? null : Long.parseLong(cellValue.toString());
                }
                break;
            case "float":
            case "java.lang.Float":
                if(CELL_VALUE_TYPE_DOUBLE.equals(cellType)){
                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    realValue = StringUtils.isEmpty(cellValue) ? null : Float.parseFloat(decimalFormat.format(cellValue));
                }else if(CELL_VALUE_TYPE_STRING.equals(cellType)){
                    realValue = StringUtils.isEmpty(cellValue) ? null : Float.parseFloat(cellValue.toString());
                }
                break;
            case "double":
            case "java.lang.Double":
                if(CELL_VALUE_TYPE_DOUBLE.equals(cellType)){
                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    realValue = StringUtils.isEmpty(cellValue) ? null : Double.parseDouble(decimalFormat.format(cellValue));
                }else if(CELL_VALUE_TYPE_STRING.equals(cellType)) {
                    realValue = StringUtils.isEmpty(cellValue) ? null : Double.parseDouble(cellValue.toString());
                }
                break;
            case "java.math.BigDecimal":
                realValue = StringUtils.isEmpty(cellValue) ? null : new BigDecimal(cellValue.toString());
                break;
            default:
                break;
        }
        return realValue;
    }

    public static void main(String[] args) {
        String path = ImportExcelUtils.class.getResource("").getPath();
        //originImportTest();
        List<List<ImportDemoDTO>> dataList = getEntityDataList(path+"/test_import.xlsx", ImportDemoDTO.class);
        for(List<ImportDemoDTO> item : dataList){
            for(ImportDemoDTO importDemoDTO : item){
                System.out.println(importDemoDTO.toString());
            }
        }
    }

    private static void originImportTest() {
        String path = ImportExcelUtils.class.getResource("").getPath();
        System.out.println(path);

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
                traversalCell(row,row.getLastCellNum());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
