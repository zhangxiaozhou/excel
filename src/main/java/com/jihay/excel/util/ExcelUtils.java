package com.jihay.excel.util;

import com.jihay.excel.entity.CovertResult;
import com.jihay.excel.service.SteelService;
import com.jihay.excel.service.impl.SteelServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.regex.Pattern;
import static javax.xml.bind.JAXBIntrospector.getValue;

public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static List<Sheet> getAllSheet(Workbook workbook) {

        List<Sheet> sheets = new LinkedList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++){
            sheets.add(workbook.getSheetAt(i));
        }

        return sheets;
    }

    public static List<ExcelRange> getDataRange(Sheet sheet){

        List<ExcelRange> excelRangeList = new LinkedList<>();

        int head = -1;

        //遍历行row
        for(int rowNum = 0; rowNum<=sheet.getLastRowNum();rowNum++){
            //获取每一行
            Row row = sheet.getRow(rowNum);
            if(row == null){
                continue;
            }

            head = isHead(row);

            //如果是表头
            if(head>=0){

                int temp = head;
                int i = head;
                String flag = row.getCell(i).toString();

                ExcelRange excelRange = new ExcelRange();
                excelRange.setX0(temp);
                excelRange.setY0(rowNum);

                //处理一行表头包含多个数据块
                while(i<row.getLastCellNum()){

                    Cell cell = row.getCell(i);

                    if(cell!=null){
                        String value = getValue(cell).toString();

                        if(flag.equals(value) && (temp!=i)){

                            excelRange.setX1(i);
                            excelRange.setY1(sheet.getLastRowNum());

                            excelRangeList.add(excelRange);

                            temp = i;

                            excelRange = new ExcelRange();
                            excelRange.setX0(temp);
                            excelRange.setY0(rowNum);
                        }
                    }

                    i++;
                }

                excelRange.setX1(row.getLastCellNum());
                excelRange.setY1(sheet.getLastRowNum());

                excelRangeList.add(excelRange);

                break;
            }
        }
        return excelRangeList;
    }

    public static List<CovertResult> getCovertResults(Sheet sheet) {

        List<CovertResult> covertResults = new LinkedList<>();

        SteelService steelService = new SteelServiceImpl();
        List<String> names = steelService.getNames();
        List<String> materias = steelService.getMaterias();
        List<String> specifis = steelService.getSpecifis();

        int nameIndex = -1;
        int specifiIndex = -1;
        int materialIndex = -1;
        int priceIndex = -1;
        int originIndex = -1;
        int warehouseIndex = -1;

        int beginRow = 0;

        for( ; beginRow<=sheet.getLastRowNum(); beginRow++) {
            Row row = sheet.getRow(beginRow);
            if(row==null){
                continue;
            }

            for(int cellNum=0; cellNum<row.getLastCellNum(); cellNum++){
                Cell cell = row.getCell(cellNum);
                if(cell==null){
                    continue;
                }
                String trim = getValue(cell).toString().trim();
                if(names.contains(trim)){
                    nameIndex = cellNum;
                    continue;
                }

                if(materias.contains(trim)){
                    materialIndex = cellNum;
                    continue;
                }

                if(specifis.contains(trim)){
                    specifiIndex = cellNum;
                    continue;
                }
                if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC  && cell.getNumericCellValue()<300 && cell.getNumericCellValue()>10){
                    specifiIndex = cellNum;
                    continue;
                }

                if(trim.indexOf("钢")>0){
                    originIndex = cellNum;
                    continue;
                }

                if(trim.indexOf("库")>0 && trim.length()<8){
                    warehouseIndex = cellNum;
                    continue;
                }

                if( cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    if(cell.getNumericCellValue()>3000 && cell.getNumericCellValue()<6000){
                        priceIndex = cellNum;
                        continue;
                    }
                }
            }

            if(materialIndex<0 || priceIndex<0){
                continue;
            }

            break;
        }

        int blankRow = 0;

        for(int y=beginRow; y<sheet.getLastRowNum(); y++ ){

            Row row = sheet.getRow(y);

            if(row==null){
                logger.info("读取到空行 " + blankRow++);
                continue;
            }

            String name = "";
            String special = "";
            String material = "";
            Double price = -1.0;
            String origin = "";
            String warehouse = "";

            try{
                name = row.getCell(nameIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析品名出错");
            }

            try{
                special = String.format("%.0f", row.getCell(specifiIndex).getNumericCellValue());
            }catch (Exception e){
                logger.error("解析规格出错");
            }

            try{
                material = getValue(row.getCell(materialIndex)).toString();
            }catch (Exception e){
                logger.error("解析材质出错");
            }
            try{
                price = row.getCell(priceIndex).getNumericCellValue();
            }catch (Exception e){
                logger.error("解析价格出错");
                continue;
            }
            try{
                origin = getValue(row.getCell(originIndex)).toString() ;
            }catch (Exception e){
                logger.error("解析产地出错");
            }
            try{
                warehouse = row.getCell(warehouseIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析仓库出错");
            }

            if(price!=null && price>0){
                CovertResult covertResult = new CovertResult(name, special, material , price, origin, warehouse);
                covertResult.setSheetName(sheet.getSheetName());

                covertResults.add(covertResult);
            }
            if(blankRow>100){
                blankRow = 0;
                break;
            }
        }

        return covertResults;
    }

    /**
     * 如果这一行是表头的话，返回表头行的表头列位置
     * @param row
     * @return
     */
    public static int isHead(Row row){

        int result = -1;

        List<String> headNames = new LinkedList<>();

        Set<Map.Entry<String, List<String>>> entries = ColumnNameAlias.columnNameAlias.entrySet();
        for(Map.Entry<String, List<String>> entry : entries){
            List<String> value = entry.getValue();
            headNames.addAll(value);
        }

        List<String> warehouseAlias = ColumnNameAlias.columnNameAlias.get("仓库");

        for(int cellNum = 0; cellNum<=row.getLastCellNum();cellNum++){
            //获取每一列
            Cell cell = row.getCell(cellNum);
            if(cell == null){
                continue;
            }

            String cellValue = StringUtils.trimAllWhitespace(getValue(cell).toString());

            if(!StringUtils.isEmpty(cellValue)){
                if(headNames.contains(cellValue)){
                    return cellNum;
                }

                for(String alian : warehouseAlias){
                    if(Pattern.matches(alian, cellValue)){
                        return cellNum;
                    }
                }
            }
        }

        return result;
    }

    public static List<CovertResult> getCovertResults(Sheet sheet, ExcelRange dataRange){

        List<CovertResult> covertResults = new LinkedList<>();

        Row headROw = sheet.getRow(dataRange.getY0());

        int nameIndex = -1;
        int specifiIndex = -1;
        int materialIndex = -1;
        int priceIndex = -1;
        int originIndex = -1;
        int warehouseIndex = -1;

        List<String> nameAlias = ColumnNameAlias.columnNameAlias.get("品名");
        List<String> specialAlias = ColumnNameAlias.columnNameAlias.get("规格");
        List<String> materialAlias = ColumnNameAlias.columnNameAlias.get("材质");
        List<String> originAlias = ColumnNameAlias.columnNameAlias.get("产地");
        List<String> priceAlias = ColumnNameAlias.columnNameAlias.get("价格");
        List<String> warehouseAlias = ColumnNameAlias.columnNameAlias.get("仓库");

        for(int i=dataRange.getX0(); i<dataRange.getX1(); i++){

            Cell cell = headROw.getCell(i);
            if(cell == null){
                continue;
            }
            String s = StringUtils.trimAllWhitespace(getValue(cell).toString());

            if(nameAlias.contains(s)){
                nameIndex = i;
                continue;
            }

            if(specialAlias.contains(s)){
                specifiIndex = i;
                continue;
            }

            if(materialAlias.contains(s)){
                materialIndex = i;
                continue;
            }

            if(originAlias.contains(s)){
                originIndex = i;
                continue;
            }

            if(priceAlias.contains(s)){
                priceIndex = i;
                continue;
            }

            for(String alian : warehouseAlias){
                if( Pattern.matches(alian, s)){
                    warehouseIndex = i;
                    continue;
                }
            }

        }

        int blankRow = 0;

        for(int y=dataRange.getY0()+1; y<dataRange.getY1(); y++ ){

            Row row = sheet.getRow(y);
            if(row==null){
                logger.info("读取到空行 " + blankRow++);
                continue;
            }

            String name = "";
            String special = "";
            String material = "";
            Double price = -1d;
            String origin = "";
            String warehouse = "";

            try{
                name = row.getCell(nameIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析品名出错");
            }

            try{
                special = row.getCell(specifiIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析规格出错");
            }

            try{
                material = row.getCell(materialIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析材质出错");
            }
            try{
                price = row.getCell(priceIndex).getNumericCellValue();
            }catch (Exception e){
                logger.error("解析价格出错");
                continue;
            }
            try{
                origin = row.getCell(originIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析产地出错");
            }
            try{
                warehouse = row.getCell(warehouseIndex).getStringCellValue();
            }catch (Exception e){
                logger.error("解析仓库出错");
            }

            if(price!=null && price>0 && !StringUtils.isEmpty(material)){
                CovertResult covertResult = new CovertResult(name, special, material , price, origin, warehouse);
                covertResult.setSheetName(sheet.getSheetName());
                covertResults.add(covertResult);
            }

            if(blankRow>100){
                break;
            }
        }

        return covertResults;
    }
}
