/**
 * @author Ratul Ghosh
 * 27-Feb-2014
 * 
 */
package com.appsbee.pairpost.application;

import java.util.ArrayList;

import com.appsbee.pairpost.pojo.Discoder;

public class Constant
{
    public static String userId;
    public static String CallBackuserId = "0";
    public static String userName;
    public static String userImage;
    public static boolean isOnResumeCall = false;
    public static boolean isCallBackFromDiscoder = false;
    public static ArrayList<Discoder> feedArr = new ArrayList<Discoder>();
    public static ArrayList<Discoder> innerfeedArr = new ArrayList<Discoder>();

	public class Flag
	{
		public static final int FLAG_SUCCESS = 1;
		public static final int FLAG_FAILED = 0;
		public static final int FLAG_SESSION_EXPIRED = 2;
	}

	public class Url
	{
		public static final String BASE_URL = "http://clickfordevelopers.com/demo/pairpost/";
		public static final String PROFILE_IMAGE_URL = "http://clickfordevelopers.com/demo/pairpost/uploads/";
		public static final String IMAGE_URL = "http://clickfordevelopers.com/demo/pairpost/uploads/memberpost/";
		public static final String LOG_IN_URL = BASE_URL + "log_in.php";
		public static final String SIGN_UP_URL = BASE_URL + "sign_up.php";
		public static final String FORGOT_PASSWORD_URL = BASE_URL
				+ "forget_password.php";
		public static final String SEARCH_USER = BASE_URL + "search_user.php";
		public static final String ASSET_DIRECTORY = BASE_URL + "uploads/";
		public static final String UPDATE_BIO = BASE_URL + "update_bio.php";
		public static final String GET_USER_DATA = BASE_URL
				+ "user_details_pairpost.php";
		public static final String UPDATE_PROFILE = BASE_URL
				+ "update_profile.php";
		public static final String GET_ONE_USER_INFO = BASE_URL
				+ "show_profile.php";

		public static final String FOLLOW_USER = BASE_URL
				+ "insert_follower.php";

		public static final String UNFOLLOW_USER = BASE_URL
				+ "delete_following.php";
		public static final String POST_UPLOAD = BASE_URL+"member_post.php";
		public static final String DISCOVER_POST = BASE_URL+"show_posts.php";
		public static final String LIKE = BASE_URL+"pairpost_like.php";
		public static final String COMMENTS_LIST = BASE_URL+"comment_members.php";
		public static final String INPUT_COMMENTS = BASE_URL+"pairpost_comment.php";
		public static final String LIKE_LIST = BASE_URL+"like_members.php";
		public static final String POST_FEED = BASE_URL+"show_feeds.php";
		public static final String DELETE_COMMENT = BASE_URL+"delete_comment.php";
		public static final String DELETE_PAIR_POST = BASE_URL+"delete_pairpost.php";
	}

	public class ParamKey
	{

		public static final String KEY_NAME = "name";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_PASSWORD = "password";

	}
	
	public class ShredPrefKey
	{
	    	public static final String PREF_NAME = "pp_pref";
		public static final String FIRST_NAME = "fname";
		public static final String LAST_NAME = "lname";
		public static final String PHOTO = "photo";
		public static final String USER_ID = "user_id";
		public static final String SESSION = "session";

	}
}
