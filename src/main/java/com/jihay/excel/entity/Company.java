package com.jihay.excel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler", "fieldHandler"})
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<UploadHistory> uploadHistoryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UploadHistory> getUploadHistoryList() {
        return uploadHistoryList;
    }

    public void setUploadHistoryList(List<UploadHistory> uploadHistoryList) {
        this.uploadHistoryList = uploadHistoryList;
    }
}
