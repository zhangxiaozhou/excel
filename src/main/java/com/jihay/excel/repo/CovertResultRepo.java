package com.jihay.excel.repo;

import com.jihay.excel.entity.CovertResult;
import com.jihay.excel.entity.UploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CovertResultRepo extends JpaRepository<CovertResult, Long> {

    List<CovertResult> findAllByUploadHistory(UploadHistory uploadHistory);
}
