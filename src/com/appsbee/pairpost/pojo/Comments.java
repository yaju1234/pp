package com.appsbee.pairpost.pojo;

public class Comments {
    private String commentID;
    private String id; 
    private String image;
    private String firstName;
    private String lastName;
    private String comment;
    private String time;

    public Comments(String commentID,String id,String image, String firstName, String lastName,    String comment, String time) {
	this.commentID = commentID;
	this.id = id;
	this.image = image;
	this.firstName = firstName;
	this.lastName = lastName;
	this.comment = comment;
	this.time = time;

    }

    
    public String getCommentID() {
        return commentID;
    }


    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
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

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

}
