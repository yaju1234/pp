/**
 * @author Ratul Ghosh
 * 26-Mar-2014
 * 
 */
package com.appsbee.pairpost.pojo;

public class pPairPostUser {
    private String memberId, memberName, memberImageUri, memberEmailId;

    public String getMemberId() {
	return memberId;
    }

    public void setMemberId(String memberId) {
	this.memberId = memberId;
    }

    public String getMemberName() {
	return memberName;
    }

    public void setMemberName(String memberName) {
	this.memberName = memberName;
    }

    public String getMemberImageUri() {
	return memberImageUri;
    }

    public void setMemberImageUri(String memberImageUri) {
	this.memberImageUri = memberImageUri;
    }

    public String getMemberEmailId() {
	return memberEmailId;
    }

    public void setMemberEmailId(String meberEmailId) {
	this.memberEmailId = meberEmailId;
    }

    @Override
    public boolean equals(Object o) {
	try {
	    pPairPostUser other = (pPairPostUser) o;
	    if (other.getMemberId() == memberId)
		return true;
	    else
		return false;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }
}
