package com.appsbee.pairpost.pojo;

public class Discoder {
    private String pairpostId;
    private String id;
    private String firstName;
    private String lastName;
    private String userIamge;
    private String caption;
    private String created;
    private String post1Type;
    private String post1Url;
    private String post2Type;
    private String post2Url;
    private String totalLike;
    private String totalComments;
    private boolean isLike;
    
    
    public Discoder(String pairpostId,String id,String firstName,String lastName,String userIamge,String caption,String created,String post1Type,String post1Url,String post2Type,String post2Url,String totalLike,String totalComments,boolean isLike){
	this.pairpostId = pairpostId;
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.userIamge = userIamge;
	this.caption = caption;
	this.created = created;
	this.post1Type = post1Type;
	this.post1Url = post1Url;
	this.post2Type = post2Type;
	this.post2Url = post2Url;
	this.totalLike = totalLike;
	this.totalComments = totalComments;
	this.isLike = isLike;
	
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserIamge() {
        return userIamge;
    }

    public void setUserIamge(String userIamge) {
        this.userIamge = userIamge;
    }

    public String getPost1Type() {
        return post1Type;
    }

    public void setPost1Type(String post1Type) {
        this.post1Type = post1Type;
    }

    public String getPost1Url() {
        return post1Url;
    }

    public void setPost1Url(String post1Url) {
        this.post1Url = post1Url;
    }

    public String getPost2Type() {
        return post2Type;
    }

    public void setPost2Type(String post2Type) {
        this.post2Type = post2Type;
    }

    public String getPost2Url() {
        return post2Url;
    }

    public void setPost2Url(String post2Url) {
        this.post2Url = post2Url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }

    public String getPairpostId() {
        return pairpostId;
    }

    public void setPairpostId(String pairpostId) {
        this.pairpostId = pairpostId;
    }

   
}
