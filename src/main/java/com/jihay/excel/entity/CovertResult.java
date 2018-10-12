package com.jihay.excel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler", "fieldHandler"})
@Entity
public class CovertResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specifications;

    private String material;

    private Double price;

    private String origin;

    private String warehouse;

    private String sheetName;

    @ManyToOne(fetch = FetchType.EAGER)
    private UploadHistory uploadHistory;

    public CovertResult() {
    }

    public CovertResult(String name, String specifications, String material, Double price, String origin, String warehouse) {
        this.name = name;
        this.specifications = specifications;
        this.material = material;
        this.price = price;
        this.origin = origin;
        this.warehouse = warehouse;
    }

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

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public UploadHistory getUploadHistory() {
        return uploadHistory;
    }

    public void setUploadHistory(UploadHistory uploadHistory) {
        this.uploadHistory = uploadHistory;
    }
}
