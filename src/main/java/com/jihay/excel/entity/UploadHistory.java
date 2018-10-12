package com.jihay.excel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler", "fieldHandler"})
@Entity
public class UploadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(columnDefinition = "DATETIME")
    private Date uploadTime;

    private String uploadUser;

    @JsonIgnore
    @OneToMany(mappedBy = "uploadHistory")
    private List<CovertResult> covertResultList;

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public List<CovertResult> getCovertResultList() {
        return covertResultList;
    }

    public void setCovertResultList(List<CovertResult> covertResultList) {
        this.covertResultList = covertResultList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}