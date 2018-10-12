package com.jihay.excel.api;

import com.jihay.excel.entity.CovertResult;
import com.jihay.excel.entity.UploadHistory;
import com.jihay.excel.form.CovertResultForm;
import com.jihay.excel.repo.CovertResultRepo;
import com.jihay.excel.repo.UploadHistoryRepo;
import com.jihay.excel.service.CovertResultService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/covertResult")
public class CovertResoutController {

    @Resource
    private CovertResultRepo covertResultRepo;

    @Resource
    private UploadHistoryRepo uploadHistoryRepo;

    @Resource
    CovertResultService covertResultService;

    @RequestMapping("/listByHistoryId")
    public List<CovertResult> listByHistoryId(CovertResultForm covertResultForm){

        CovertResult covertResult = new CovertResult();
        covertResult.setUploadHistory(uploadHistoryRepo.getOne(covertResultForm.getUploadHistoryId()));
        covertResult.setMaterial(covertResultForm.getMaterial());
        covertResult.setSpecifications(covertResultForm.getSpecifications());

        List<CovertResult> allByUploadHistory = covertResultService.query(covertResult);

        return allByUploadHistory;
    }
}
