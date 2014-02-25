package com.tesco.adapter.dao;

/**
 * Created with IntelliJ IDEA.
 * User: xy66
 * Date: 11/02/14
 * Time: 22:26
 * Product Representation Class
 */

public class Product {

    private String id;
    private String tpnb;
    private String catid;
    private String productDescription;
    private float height;
    private float length;
    private Boolean isHazardous;

    //Introducing the dummy constructor
    public Product(){
    }

    public Product(String setId){
        this.id = setId;
        this.tpnb = "1234";
        this.catid = "a1234-123";
        this.productDescription = "This is an example product";
        this.height = 24;
        this.length = 100;
        this.isHazardous = false;
    }

    public String getId() {
        return id;
    }

    public String getTpnb() {
        return tpnb;
    }

    public String getCatid() {
        return catid;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public float getHeight() {
        return height;
    }

    public float getLength() {
        return length;
    }

    public Boolean getHazardous() {
        return isHazardous;
    }

    public void setId(String id) {
        this.id =id;
    }

    public void setTpnb(String tpnb) {
        this.tpnb = tpnb;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setHazardous(Boolean isHazardous) {
        this.isHazardous = isHazardous;
    }

}
