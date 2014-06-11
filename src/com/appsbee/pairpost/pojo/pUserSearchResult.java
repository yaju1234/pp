/**
 * @author Ratul Ghosh
 * 26-Mar-2014
 * 
 */
package com.appsbee.pairpost.pojo;

public class pUserSearchResult
{
	private String memberId, memberName, memberEmail, memberImageUrl;
	private boolean isFollowing;

	public String getMemberId()
	{
		return memberId;
	}

	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}

	public String getMemberName()
	{
		return memberName;
	}

	public void setMemberName(String memberName)
	{
		this.memberName = memberName;
	}

	public String getMemberEmail()
	{
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail)
	{
		this.memberEmail = memberEmail;
	}

	public String getMemberImageUrl()
	{
		return memberImageUrl;
	}

	public void setMemberImageUrl(String memberImageUrl)
	{
		this.memberImageUrl = memberImageUrl;
	}

	public boolean isFollowing()
	{
		return isFollowing;
	}

	public void setFollowing(boolean isFollowing)
	{
		this.isFollowing = isFollowing;
	}

}
