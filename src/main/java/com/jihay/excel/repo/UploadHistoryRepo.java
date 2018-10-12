package com.jihay.excel.repo;

import com.jihay.excel.entity.UploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadHistoryRepo extends JpaRepository<UploadHistory, Long> {
}
