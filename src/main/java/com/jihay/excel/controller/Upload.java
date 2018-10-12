package com.jihay.excel.controller;

import com.jihay.excel.entity.Company;
import com.jihay.excel.entity.CovertResult;
import com.jihay.excel.entity.UploadHistory;
import com.jihay.excel.form.UploadForm;
import com.jihay.excel.repo.CompanyRepo;
import com.jihay.excel.repo.CovertResultRepo;
import com.jihay.excel.repo.UploadHistoryRepo;
import com.jihay.excel.util.ExcelRange;
import com.jihay.excel.util.ExcelUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class Upload {

    @Resource
    private CompanyRepo companyRepo;

    @Resource
    private UploadHistoryRepo uploadHistoryRepo;

    @Resource
    private CovertResultRepo covertResultRepo;

    @RequestMapping("/excel")
    public String excel(UploadForm uploadForm, Model model) throws IOException{

        List<CovertResult> covertResultList = new LinkedList<>();

        Company company = companyRepo.getOne(uploadForm.getCompanyId());

        UploadHistory uploadHistory = new UploadHistory();
        uploadHistory.setUploadTime(new Date());
        uploadHistory.setUploadUser("");
        uploadHistory.setCompany(company);
        uploadHistoryRepo.save(uploadHistory);

        for(MultipartFile file : uploadForm.getFiles()){

            uploadHistory.setFileName( uploadHistory.getFileName() + "," + file.getOriginalFilename());

            InputStream inputStream = file.getInputStream();

            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(inputStream);
            } catch (InvalidFormatException e) {
                e.printStackTrace();
                return "uploadResult";
            }

            List<Sheet> allSheet = ExcelUtils.getAllSheet(workbook);

            if(allSheet!=null && !allSheet.isEmpty()){
                for(Sheet sheet : allSheet){

                    List<ExcelRange> dataRanges = ExcelUtils.getDataRange(sheet);
                    if(!dataRanges.isEmpty()){
                        for(ExcelRange excelRange : dataRanges){
                            List<CovertResult> covertResults = ExcelUtils.getCovertResults(sheet, excelRange);
                            for(CovertResult covertResult : covertResults){
                                covertResult.setUploadHistory(uploadHistory);
                            }
                            covertResultList.addAll(covertResults);
                        }
                    }else{
                        List<CovertResult> covertResults = ExcelUtils.getCovertResults(sheet);
                        for(CovertResult covertResult : covertResults){
                            covertResult.setUploadHistory(uploadHistory);
                        }
                        covertResultList.addAll(covertResults);
                    }
                }
            }
            inputStream.close();
        }

        covertResultRepo.saveAll(covertResultList);

        model.addAttribute("uploadHistoryId", uploadHistory.getId());

        return "uploadResult";
    }

}
