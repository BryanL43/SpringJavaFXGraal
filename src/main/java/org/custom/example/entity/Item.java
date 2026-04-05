package org.custom.example.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

@Table("item")
public class Item {
    @Id
    private Long id;

    @NotNull
    private Long airlineId;

    @Size(max = 50)
    private String itemLocation;

    @NotBlank
    @Size(max = 50)
    private String itemCode;

    @NotBlank
    @Size(max = 200)
    private String itemDescription;

    @Min(0)
    private int unitsPerCarton;

    private Boolean perishable;

    private LocalDate expirationDate;

    @Min(0)
    private int goodsReceiptPcs;

    @Min(0)
    private int goodsIssuancePcs;

    private LocalDateTime deletedAt;

    @ReadOnlyProperty
    private LocalDateTime createdAt;

    protected Item() {}

    public Item(
        Long airlineId,
        String itemLocation,
        String itemCode,
        String itemDescription,
        int unitsPerCarton,
        Boolean perishable,
        LocalDate expirationDate,
        int goodsReceiptPcs,
        int goodsIssuancePcs
    ) {
        this.airlineId = airlineId;
        this.itemLocation = itemLocation;
        this.itemCode = itemCode;
        this.itemDescription = itemDescription;
        this.unitsPerCarton = unitsPerCarton;
        this.perishable = perishable;
        this.expirationDate = expirationDate;
        this.goodsReceiptPcs = goodsReceiptPcs;
        this.goodsIssuancePcs = goodsIssuancePcs;
    }

    // ========================================================================================= //
    // ACCESSORS
    // ========================================================================================= //
    public Long getId() {
        return id;
    }

    public Long getAirlineId() {
        return airlineId;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getUnitsPerCarton() {
        return unitsPerCarton;
    }

    public Boolean isPerishable() {
        return perishable;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getGoodsReceiptPcs() {
        return goodsReceiptPcs;
    }

    public int getGoodsIssuancePcs() {
        return goodsIssuancePcs;
    }

    public Boolean isDeleted() {
        return deletedAt != null;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ========================================================================================= //
    // MUTATORS
    // ========================================================================================= //
    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setUnitsPerCarton(int unitsPerCarton) {
        this.unitsPerCarton = unitsPerCarton;
    }

    public void setPerishable(Boolean perishable) {
        this.perishable = perishable;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setGoodsReceiptPcs(int goodsReceiptPcs) {
        this.goodsReceiptPcs = goodsReceiptPcs;
    }

    public void setGoodsIssuancePcs(int goodsIssuancePcs) {
        this.goodsIssuancePcs = goodsIssuancePcs;
    }

    // ========================================================================================= //
    // LIFECYCLES
    // ========================================================================================= //
    public void delete() {
        if (this.deletedAt == null) {
            this.deletedAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Item {" +
            " id=" + id + "," +
            " airlineId=" + airlineId + "," +
            " itemLocation=" + itemLocation + "," +
            " itemCode=" + itemCode + "," +
            " itemDescription=" + itemDescription + "," +
            " unitsPerCarton=" + unitsPerCarton + "," +
            " perishable=" + perishable + "," +
            " expirationDate=" + expirationDate + "," +
            " goodsReceiptPcs=" + goodsReceiptPcs + "," +
            " goodsIssuancePcs=" + goodsIssuancePcs + "," +
            " createdAt=" + createdAt +
            " }";
    }
}
