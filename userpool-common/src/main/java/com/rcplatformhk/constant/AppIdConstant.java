package com.rcplatformhk.constant;

/**
 * Created by Yang Peng on 2017/7/10.
 *
 * @Description: appId
 */
public class AppIdConstant {

	public static final Integer OLD_APP_ID = 19999;

	public static final Integer NEW_APP_ID = 20000;

	public static final Integer YAAR_APP_ID = 66666;

	/**
	 * id+second的和
	 */
	public static final Integer MAX_ID = 99999999;

	public static Integer getRealAppId(int appId) {
		return appId > NEW_APP_ID ? YAAR_APP_ID : appId;
	}


	public static boolean isYaar(int appId) {
		return appId > NEW_APP_ID;
	}


}
