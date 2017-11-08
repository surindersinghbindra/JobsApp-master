package com.example.util;

import java.io.Serializable;

public class Constant implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String SERVER_URL = "http://www.jobportal.pw/apps/global/";

    public static final String IMAGE_PATH = SERVER_URL + "images/";

    public static final String CATEGORY_URL = SERVER_URL + "api.php?cat_list";

    public static final String LATEST_URL = SERVER_URL + "api.php?latest";

    public static final String CATEGORY_ITEM_URL = SERVER_URL + "api.php?cat_id=";

    public static final String SINGLE_JOB_URL = SERVER_URL + "api.php?job_id=";

    public static final String APPLY_JOB_URL = SERVER_URL + "apply_job_api.php?user_id=";

    public static final String ABOUT_URL = SERVER_URL + "api.php";

    public static final String REGISTER_URL = SERVER_URL + "user_register_api.php?name=";

    public static final String REGISTER_FOR_PAYMENT_URL = SERVER_URL + "demo_register_api.php?name=";

    public static final String LOGIN_URL = SERVER_URL + "user_login_api.php?email=";

    public static final String FORGOT_PASSWORD_URL = SERVER_URL + "user_forgot_pass_api.php?email=";

    public static final String USER_PROFILE_URL = SERVER_URL + "user_profile_api.php?id=";

    public static final String USER_PROFILE_UPDATE_URL = SERVER_URL + "user_profile_update_api.php?name=";

    public static final String SEARCH_URL = SERVER_URL + "api.php?job_search=";

    public static final String ARRAY_NAME = "JOBS_APP";

    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_CID = "cid";
    public static final String CATEGORY_IMAGE = "category_image";

    public static final String JOB_ID = "id";
    public static final String JOB_NAME = "job_name";
    public static final String JOB_DESIGNATION = "job_designation";
    public static final String JOB_DESC = "job_desc";
    public static final String JOB_SALARY = "job_salary";
    public static final String JOB_COMPANY_NAME = "job_company_name";
    public static final String JOB_SITE = "job_company_website";
    public static final String JOB_PHONE_NO = "job_phone_number";
    public static final String JOB_MAIL = "job_mail";
    public static final String JOB_VACANCY = "job_vacancy";
    public static final String JOB_ADDRESS = "job_address";
    public static final String JOB_QUALIFICATION = "job_qualification";
    public static final String JOB_SKILL = "job_skill";
    public static final String JOB_IMAGE = "job_image";
    public static final String JOB_DATE = "job_date";

    public static final String APP_NAME = "app_name";
    public static final String APP_IMAGE = "app_logo";
    public static final String APP_VERSION = "app_version";
    public static final String APP_AUTHOR = "app_author";
    public static final String APP_CONTACT = "app_contact";
    public static final String APP_EMAIL = "app_email";
    public static final String APP_WEBSITE = "app_website";
    public static final String APP_DESC = "app_description";
    public static final String APP_PRIVACY_POLICY = "app_privacy_policy";

    public static final String USER_NAME = "name";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";
    public static final String USER_CITY = "city";
    public static final String USER_ADDRESS = "address";

    public static int GET_SUCCESS_MSG;
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";

    public static int AD_COUNT = 0;
    public static int AD_COUNT_SHOW = 5;
}
