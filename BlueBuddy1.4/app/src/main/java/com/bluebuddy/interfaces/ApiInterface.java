package com.bluebuddy.interfaces;

import com.bluebuddy.classes.ServerResponse;
import com.bluebuddy.classes.ServerResponseCE;
import com.bluebuddy.classes.ServerResponseCp1;
import com.bluebuddy.classes.ServerResponseCp3;
import com.bluebuddy.classes.ServerResponseCp4;
import com.bluebuddy.classes.ServerResponseCp6;
import com.bluebuddy.classes.ServerResponseCp7;
import com.bluebuddy.classes.ServerResponseFp;
import com.bluebuddy.classes.ServerResponseLoc;
import com.bluebuddy.classes.ServerResponseOtp;
import com.bluebuddy.classes.ServerResponseUnf;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllCharter;
import com.bluebuddy.models.AllChatHistory;
import com.bluebuddy.models.AllCourse;
import com.bluebuddy.models.AllMyListing;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllProduct;
import com.bluebuddy.models.AllReview;
import com.bluebuddy.models.AllService;
import com.bluebuddy.models.AllTrip;
import com.bluebuddy.models.BuddyResponse;
import com.bluebuddy.models.CharterDetailResponse;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.CommonResponseforBuddy;
import com.bluebuddy.models.CourseDetailResponse;
import com.bluebuddy.models.EventDetailsResponse;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.models.InviteBuddyResponse;
import com.bluebuddy.models.LoginStatus;
import com.bluebuddy.models.MyTripsResponse;
import com.bluebuddy.models.PeopleProfileResponse;
import com.bluebuddy.models.ProductDetailResponse;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;
import com.bluebuddy.models.ResendOtpModel;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ServiceDetailResponse;
import com.bluebuddy.models.Trip;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    //Registration
    @POST("api/v1/Users/create/")
    Call<ServerResponse> register(
            @Field("method") String method,
            @Field("email") String email,
            @Field("mobile_no") String mobile_no,
            @Field("password") String password,
            @Field("firebase_api_key") String firebase_api_key,
            @Field("social_id") String socialId,
            @Field("social") String social,
            @Field("is_social") String isSocial,
//            @Field("firebase_reg_id") String firebase_reg_id,
            @Field("device_type") String deviceType
    );

    @FormUrlEncoded
    //Firebase Registration
    @POST("api/v1/Profile/readdfirebasereg/")
    Call<ServerResponseCp7> registerFirebase(
            @Header("Authorizations") String Token,
            @Field("Firebase_API_key") String firebase_api_key,
            @Field("Firebase_reg_id") String firebase_reg_id,
            @Field("next_step") String next_step
    );

    @Multipart
    @POST("api/v1/Profile/update/")
    Call<CommonResponse> updateProfileMultipart(
            @Header("Authorizations") String Token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("user_location") RequestBody user_location,
            @Part("user_lat") RequestBody user_lat,
            @Part("user_long") RequestBody user_long,
            @Part("about") RequestBody about,
            @Part("website_link") RequestBody website_link,
            @Part("next_step") RequestBody pagetype,
            @Part MultipartBody.Part profile_img
    );

    @FormUrlEncoded
    // Upload Image
    @POST("api/v1/Profile/update/")
    Call<CommonResponse> updateMyProfile(
            @Header("Authorizations") String Token,
            @Field("profile_img") String profileimg,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("user_location") String user_location,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("about") String about,
            @Field("website_link") String website_link,
            @Field("next_step") String next_step
    );

    @FormUrlEncoded
    // Upload Image
    @Multipart
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp7> uploadImage(
            @Header("Authorizations") String Token,
            @Field("profile_img") String profile_img,
            @Field("next_step") int next_step
    );

    @Multipart
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp7> uploadImageMultipart(
            @Header("Authorizations") String Token,
//            @Part("next_step") RequestBody pagetype,
            @Part MultipartBody.Part image);

    /*
    * Call<UploadResult> uploadimage(@Part("fromApp") RequestBody fromApp,
                                   @Part("apptype") RequestBody apptype,
                                   @Part("pagetype") RequestBody pagetype,
                                   @Part("country_code") RequestBody country_code,
                                   @Part MultipartBody.Part image);*/

    //Login
    @FormUrlEncoded
    @POST("api/v1/User/login/")
    Call<LoginStatus> login(
            /*@Field("method") String method,*/
            @Field("email") String email,
            @Field("password") String password,
            @Field("firebase_api_key") String firebase_api_key,
            @Field("social_id") String socialId,
            @Field("social") String social,
            @Field("is_social") String isSocial,
            @Field("device_type") String deviceType
    );

   /* @GET("/sp/index.php")
    Call<ServerResponse> get(
            @Query("method") String method,
            @Query("username") String username,
            @Query("password") String password
    );*/

    //Otp
    @FormUrlEncoded
    @POST("api/v1/User/activate/")
    Call<ServerResponseOtp> Otp(

            @Field("user_id") String user_id,
            @Field("otp") String otp
    );

    //Otp
    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp1> Cp1(
            @Header("Authorizations") String Token,
            /* @Field("method") String method,*/
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("next_step") int next_step
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp1> Cp1fb(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile_no") String mobile_no,
            @Field("next_step") int next_step,
            @Field("firebase_api_key") String firebase_api_key,
            @Field("firebase_reg_id") String firebase_reg_id
    );

    //Certification details
    @FormUrlEncoded
    @POST("api/v1/Profile/certification/")
    Call<ServerResponseCp3> Cp3(
            @Header("Authorizations") String Token,
            @Field("next_step") String method,
            @Field("cert_agency") String cert_agency,
            @Field("cert_level") String cert_level,
            @Field("cert_no") String cert_no,
            @Field("cert_type") String cert_type
    );

    @FormUrlEncoded
    @POST("api/v1/Buddy/remove")
    Call<ServerResponseUnf> unf(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("buddy_id") String buddy_id
    );

    //flag reporting
    @FormUrlEncoded
    @POST("api/v1/Flag/create/")
    Call<FlagResponse> fr(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("type") String type,
            @Field("type_id") String type_id
    );

    //mpa
    @GET("api/v1/Common/countnotification/")
    Call<AfterLoginStatus> cn(
            @Header("Authorizations") String Token,
            @Query("method") String method
    );

    //chatHistory
    @GET("api/v1/Common/chathistory/")
    Call<AllChatHistory> ch(
            @Header("Authorizations") String Token,
            @Query("email") String email
    );

    //giving_review
    @FormUrlEncoded
    @POST("api/v1/Review/create/")
    Call<ReviewDetail> review(
            @Header("Authorizations") String Token,
            @Field("rev_title") String rev_title,
            @Field("rev_description") String rev_description,
            @Field("rev_rating") String rev_rating,
            @Field("rev_for") String rev_for,
            @Field("for_id") String for_id
    );

    //giving_review
    @FormUrlEncoded
    @POST("api/v1/Review/update/")
    Call<ReviewDetail> review_update(
            @Header("Authorizations") String Token,
            @Field("rev_title") String rev_title,
            @Field("rev_description") String rev_description,
            @Field("rev_rating") String rev_rating,
            @Field("rev_for") String rev_for,
            @Field("for_id") String for_id,
            @Field("id") String id
    );

    //Certification details
    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp4> Cp4(
            @Header("Authorizations") String Token,

            @Field("about") String about,
            @Field("next_step") int next_step

    );

    //Certification details
    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp6> Cp6(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("website_link") String website_link,
            @Field("next_step") int next_step
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseLoc> Loc(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("user_location") String user_location,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("next_step") int next_step
    );

    //Certification details
    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp7> Cp7(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("profile_img") String profile_img
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp6> socialurl(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("fb_url") String fb_url,
            @Field("next_step") int next_step
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp6> socialurl2(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("twtr_url") String twtr_url,
            @Field("next_step") int next_step
    );

    @GET("api/v1/Profile/mylist")
    Call<AllMyListing> allmylist(
            @Header("Authorizations") String Token
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp6> socialurl3(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("ingm_url") String ingm_url,
            @Field("next_step") int next_step
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/update/")
    Call<ServerResponseCp6> socialurl4(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("ggle_url") String ggle_url,
            @Field("next_step") int next_step
    );

    //mpa
    @GET("api/v1/Profile/details")
    Call<AfterLoginStatus> mpa(
            @Header("Authorizations") String Token
    );

    //mpa
//    @FormUrlEncoded
    @GET("api/v1/user/details")
    Call<PeopleProfileResponse> upa(
            @Header("Authorizations") String Token,
            @Query("user_id") String user_id
    );

    //Forgot password
    @FormUrlEncoded
    @POST("api/v1/Users/forgotpassword")
    Call<ServerResponseFp> Fp(
            @Field("method") String method,
            @Field("email") String email
    );

    //Certification details
    @FormUrlEncoded
    @POST("api/v1/Trip/create")
    Call<ServerResponseCE> Ce(
            @Header("Authorizations") String Token,
            @Field("trip_lat") String trip_lat,
            @Field("trip_long") String trip_long,
            @Field("trip_location") String trip_location,
            @Field("trip_dt") String trip_dt,
            @Field("trip_type") String trip_type,
            @Field("description") String description
    );

    //change password
    @FormUrlEncoded
    @POST("api/v1/profile/changepassword")
    Call<CommonResponse> changepass(
            @Header("Authorizations") String Token,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password

    );

    //DATE SEARCH
    // @FormUrlEncoded
    @POST("api/v1/Trip/alltrip")
    Call<AllTrip> srchdt(
            @Header("Authorizations") String Token,
            @Query("category") String category,
            @Query("dt1") String dt1,
            @Query("dt2") String dt2
    );

    @GET("api/v1/Trip/mytrip")
    Call<Trip> trip(
            @Header("Authorizations") String Token
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/alltrip")
    Call<AllTrip> alltrip(
            @Header("Authorizations") String Token,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/alltrip")
    Call<AllTrip> allTripSearch(
            @Header("Authorizations") String Token,
            @Field("category") String category,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("radius") String radius,
            @Field("dt1") String dt1,
            @Field("dt2") String dt2
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/details")
    Call<EventDetailsResponse> tripDetails(
            @Header("Authorizations") String Token,
            @Field("event_id") String event_id
    );

    @GET("api/v1/Charter/list")
    Call<AllCharter> allCharter(
            @Header("Authorizations") String Token
    );

    @FormUrlEncoded
    @POST("api/v1/Charter/list")
    Call<AllCharter> allCharterSearch(
            @Header("Authorizations") String Token,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("radius") String radius

    );


    @GET("api/v1/Charter/mylisting")
    Call<AllCharter> myCharter(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Service/mylisting")
    Call<AllService> myService(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Service/list")
    Call<AllService> allService(
            @Header("Authorizations") String Token
    );

    @FormUrlEncoded
    @POST("api/v1/Service/list")
    Call<AllService> allServiceSearch(
            @Header("Authorizations") String Token,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("radius") String radius
    );

    @GET("api/v1/Review/myreview")
    Call<AllReview> allReview(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Product/list")
    Call<AllProduct> allProduct(
            @Header("Authorizations") String Token,
            @Query("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Product/list")
    Call<AllProduct> allProductSearch(
            @Header("Authorizations") String Token,
            @Field("category") String category,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("radius") String radius
    );

    @GET("api/v1/Product/mylisting")
    Call<AllProduct> myProduct(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Profile/details")
    Call<AfterLoginStatus> myCertificate(
            @Header("Authorizations") String Token
    );

    @FormUrlEncoded
    @POST("api/v1/Course/list")
    Call<AllCourse> allCourse(
            @Header("Authorizations") String Token,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Course/list")
    Call<AllCourse> allCourseSearch(
            @Header("Authorizations") String Token,
            @Field("category") String category,
            @Field("user_lat") String user_lat,
            @Field("user_long") String user_long,
            @Field("radius") String radius
    );

    @GET("api/v1/Course/mylisting")
    Call<AllCourse> myCourse(
            @Header("Authorizations") String Token,
            @Query("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/charter/detail")
    Call<CharterDetailResponse> chrtDt(
            @Header("Authorizations") String Token,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/v1/Service/detail")
    Call<ServiceDetailResponse> serviceDt(
            @Header("Authorizations") String Token,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/v1/Product/detail")
    Call<ProductDetailResponse> productDt(
            @Header("Authorizations") String Token,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/v1/Product/delete_image")
    Call<ResponseBody> productImageDelete(
            @Header("Authorizations") String Token,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/v1/Course/detail")
    Call<CourseDetailResponse> courseDt(
            @Header("Authorizations") String Token,
            @Field("id") String id
    );
    /*
        trip_location, trip_type, description, trip_dt
    */

    /* send push notification */
    @POST("fcm/send")
    Call<PushNotification> push(
            @Header("Authorization") String serverKey,
            @Body PushRequest notification
    );

    /* Update trip details */
    @FormUrlEncoded
    @POST("api/v1/Trip/update/")
    Call<CommonResponse> updateTrip(
            @Header("Authorizations") String serverKey,
            /*  @Field("method") String method,*/
            @Field("trip_id") String trip_id,
            @Field("trip_location") String trip_location,
            @Field("trip_type") String trip_type,
            @Field("description") String description,
            @Field("trip_dt") String trip_dt,
            @Field("trip_lat") String trip_lat,
            @Field("trip_long") String trip_long
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/participate/")
    Call<CommonResponse> participateToEvent(
            @Header("Authorizations") String serverKey,
            @Field("event_id") String event_id
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/participatedelete")
    Call<CommonResponse> deleteParticipantFromEvent(
            @Header("Authorizations") String serverKey,
            @Field("event_id") String event_id,
            @Field("participant_id") String participant_id
    );


    @FormUrlEncoded
    @POST("api/v1/Common/delete")
    Call<CommonResponse> deleteapi(
            @Header("Authorizations") String Token,
            @Field("type") String type,
            @Field("data_id") String data_id

    );

    //charter insert
    @FormUrlEncoded
    @POST("api/v1/Charter/create/")
    Call<ServerResponsechrt> chrtinst(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("ch_name") String ch_name,
            @Field("ch_description") String ch_description,
            @Field("ch_location") String ch_location,
            @Field("ch_fullprice") String ch_fullprice,
            @Field("ch_halfprice") String ch_halfprice,
            @Field("ch_boattype") String ch_boattype,
            @Field("ch_capacity") String ch_capacity,
            @Field("ch_img") String ch_img,
            @Field("ch_lat") String ch_lat,
            @Field("ch_long") String ch_long

    );

    @Multipart
    @POST("api/v1/Charter/create/")
    Call<ServerResponsechrt> chrtinstMultipart(
            @Header("Authorizations") String Token,
            @Part("ch_name") RequestBody ch_name,
            @Part("ch_description") RequestBody ch_description,
            @Part("ch_location") RequestBody ch_location,
            @Part("ch_fullprice") RequestBody ch_fullprice,
            @Part("ch_halfprice") RequestBody ch_halfprice,
            @Part("ch_boattype") RequestBody ch_boattype,
            @Part("ch_capacity") RequestBody ch_capacity,
            @Part("ch_lat") RequestBody ch_lat,
            @Part("ch_long") RequestBody ch_long,
            @Part("ch_hide_details") RequestBody hideContact,
            @Part MultipartBody.Part profile_img
    );

    //Services insert
    @FormUrlEncoded
    @POST("api/v1/Service/create/")
    Call<ServerResponsechrt> serviceinst(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("sv_type") String sv_type,
            @Field("sv_location") String sv_location,
            @Field("sv_description") String sv_description,
            @Field("sv_img") String sv_img,
            @Field("sv_lat") String sv_lat,
            @Field("sv_long") String sv_long

    );

    @Multipart
    @POST("api/v1/Service/create/")
    Call<ServerResponsechrt> serviceinstMultipart(
            @Header("Authorizations") String Token,
            @Part("sv_type") RequestBody sv_type,
            @Part("sv_location") RequestBody sv_location,
            @Part("sv_description") RequestBody sv_description,
            @Part("sv_lat") RequestBody sv_lat,
            @Part("sv_long") RequestBody sv_long,
            @Part("sv_hide_details") RequestBody requestBodyHideContact,
            @Part MultipartBody.Part sv_img
    );


    @FormUrlEncoded
    @PUT(" api/v1/Service/update")
    Call<ServerResponsechrt> serviceupdate(
            @Header("Authorizations") String Token,
            @Field("sv_id") String sv_id,
            @Field("sv_type") String sv_type,
            @Field("sv_location") String sv_location,
            @Field("sv_description") String sv_description,
            @Field("sv_img") String sv_img,
            @Field("sv_lat") String sv_lat,
            @Field("sv_long") String sv_long
    );

    @Multipart
    @POST("api/v1/Service/update/")
    Call<ServerResponsechrt> serviceupdateMultipart(
            @Header("Authorizations") String Token,
            @Part("sv_id") RequestBody sv_id,
            @Part("sv_type") RequestBody sv_type,
            @Part("sv_location") RequestBody sv_location,
            @Part("sv_description") RequestBody sv_description,
            @Part("sv_lat") RequestBody sv_lat,
            @Part("sv_long") RequestBody sv_long,
            @Part("sv_hide_details") RequestBody requestBodyHideContact,
            @Part MultipartBody.Part image
    );

    @GET("api/v1/charter/detail")
    Call<MyTripsResponse> participatedList(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Buddy/list")
    Call<BuddyResponse> buddyList(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Buddy/list")
    Call<InviteBuddyResponse> invitebuddyList(
            @Header("Authorizations") String Token
    );

    //product insert
    @FormUrlEncoded
    @POST("api/v1/Product/create/")
    Call<ServerResponsechrt> productinst(
            @Header("Authorizations") String Token,
            @Field("method") String method,
            @Field("pr_img1") String pr_img1,
            @Field("pr_img2") String pr_img2,
            @Field("pr_img3") String pr_img3,
            @Field("pr_img4") String pr_img4,
            @Field("pr_name") String pr_name,
            @Field("pr_category") String pr_category,
            @Field("pr_description") String pr_description,
            @Field("pr_price") String pr_price,
            @Field("pr_location") String pr_location,
            @Field("pr_lat") String pr_lat,
            @Field("pr_long") String pr_long
    );

    @Multipart
    @POST("api/v1/Product/create/")
    Call<ServerResponsechrt> productinsttMultipart(
            @Header("Authorizations") String Token,
            @Part("pr_name") RequestBody pr_name,
            @Part("pr_category") RequestBody pr_category,
            @Part("pr_description") RequestBody pr_description,
            @Part("pr_price") RequestBody pr_price,
            @Part("pr_location") RequestBody pr_location,
            @Part("pr_lat") RequestBody pr_lat,
            @Part("pr_long") RequestBody pr_long,
            @Part("pr_hide_details") RequestBody pr_hide_details,
            @Part MultipartBody.Part pr_img1,
            @Part MultipartBody.Part pr_img2,
            @Part MultipartBody.Part pr_img3,
            @Part MultipartBody.Part pr_img4
    );

    //courses insert
    @FormUrlEncoded
    @POST("api/v1/Course/create/")
    Call<ServerResponsechrt> coursesinst(
            @Header("Authorizations") String Token,
            @Field("category") String category,
            @Field("agency_nm") String agency_nm,
            @Field("agency_url") String agency_url,
            @Field("co_duration") String co_duration,
            @Field("co_description") String co_description,
            @Field("co_price") String co_price,
            @Field("co_location") String co_location,
            @Field("co_img") String co_image,
            @Field("co_lat") String co_lat,
            @Field("co_long") String co_long
    );

    @Multipart
    @POST("api/v1/Course/create/")
    Call<ServerResponsechrt> coursesinstMultipart(
            @Header("Authorizations") String Token,
            @Part("category") RequestBody category,
            @Part("agency_nm") RequestBody agency_nm,
            @Part("agency_url") RequestBody agency_url,
            @Part("co_duration") RequestBody co_duration,
            @Part("co_description") RequestBody co_description,
            @Part("co_price") RequestBody co_price,
            @Part("co_location") RequestBody co_location,
            @Part("co_lat") RequestBody co_lat,
            @Part("co_long") RequestBody co_long,
            @Part MultipartBody.Part co_image
    );


    @FormUrlEncoded
    @POST("api/v1/Course/update/")
    Call<ServerResponsechrt> coursesupdate(
            @Header("Authorizations") String Token,
            @Field("co_id") String id,
            @Field("category") String category,
            @Field("agency_nm") String agency_nm,
            @Field("agency_url") String agency_url,
            @Field("co_duration") String co_duration,
            @Field("co_description") String co_description,
            @Field("co_price") String co_price,
            @Field("co_location") String co_location,
            @Field("co_lat") String co_lat,
            @Field("co_long") String co_long,
            @Field("co_img") String co_image
    );


    @FormUrlEncoded
    @PUT("api/v1/Course/update/")
    Call<ServerResponsechrt> coursesfeature(
            @Header("Authorizations") String Token,
            @Field("co_id") String id,
            @Field("co_is_featured") String co_is_featured
    );

    @FormUrlEncoded
    @POST("api/v1/Charter/update/")
    Call<ServerResponsechrt> Charterupdate(
            @Header("Authorizations") String Token,
            @Field("ch_id") String id,
            @Field("ch_name") String name,
            @Field("ch_location") String location,
            @Field("ch_fullprice") String full_price,
            @Field("ch_halfprice") String half_price,
            @Field("ch_boattype") String type_of_boat,
            @Field("ch_capacity") String capacity,
            @Field("ch_description") String description,
            @Field("ch_lat") String ch_lat,
            @Field("ch_long") String ch_long,
            @Field("ch_hide_details") String hideContact
    );

    @Multipart
    @POST("api/v1/Charter/update/")
    Call<ServerResponsechrt> CharterupdateMultiPart(
            @Header("Authorizations") String Token,
            @Part("ch_id") RequestBody id,
            @Part("ch_name") RequestBody name,
            @Part("ch_location") RequestBody location,
            @Part("ch_fullprice") RequestBody full_price,
            @Part("ch_halfprice") RequestBody half_price,
            @Part("ch_boattype") RequestBody type_of_boat,
            @Part("ch_capacity") RequestBody capacity,
            @Part("ch_description") RequestBody description,
            @Part("ch_lat") RequestBody ch_lat,
            @Part("ch_long") RequestBody ch_long,
            @Part("ch_hide_details") RequestBody hideContact,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("api/v1/Charter/update/")
    Call<ServerResponsechrt> Charterimgupdate(
            @Header("Authorizations") String Token,
            @Field("ch_id") String id,
            @Field("ch_img") String image
    );

    @Multipart
    @POST("api/v1/Charter/update/")
    Call<ServerResponsechrt> CharterimgupdateMultipart(
            @Header("Authorizations") String Token,
            @Part("ch_id") RequestBody ch_id,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @PUT("api/v1/Charter/update/")
    Call<ServerResponsechrt> Charterfeature(
            @Header("Authorizations") String Token,
            @Field("ch_id") String id,
            @Field("ch_is_featured") String ch_is_featured
    );

    @FormUrlEncoded
    @PUT("api/v1/Course/update/")
    Call<AllCourse> Courseimgupdate(
            @Header("Authorizations") String Token,
            @Field("co_id") String id,
            @Field("co_img") String image
    );

    @Multipart
    @POST("api/v1/Course/update/")
    Call<ServerResponsechrt> CourseimgupdateMultipart(
            @Header("Authorizations") String Token,
            @Part("co_id") RequestBody ch_id,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @PUT("api/v1/Product/update/")
    Call<ServerResponsechrt> Productimgupdate(
            @Header("Authorizations") String Token,
            @Field("pr_id") String id,
            @Field("pr_img1") String image

    );

    @Multipart
    @POST("api/v1/Product/update/")
    Call<ServerResponsechrt> ProductimgupdateMultipart(
            @Header("Authorizations") String Token,
            @Part("pr_id") RequestBody ch_id,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/v1/Product/update/")
    Call<ServerResponsechrt> ProductimgupdateMultipart(
            @Header("Authorizations") String Token,
            @Part("pr_id") RequestBody id,
            @Part("pr_name") RequestBody pr_name,
            @Part("pr_category") RequestBody pr_category,
            @Part("pr_description") RequestBody pr_description,
            @Part("pr_price") RequestBody pr_price,
            @Part("pr_location") RequestBody pr_location,
            @Part("pr_lat") RequestBody pr_lat,
            @Part("pr_long") RequestBody pr_long,
            @Part("pr_hide_details") RequestBody pr_hide_details,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4
    );


    @FormUrlEncoded
    @PUT("api/v1/Product/update/")
    Call<ServerResponsechrt> Productfeature(
            @Header("Authorizations") String Token,
            @Field("pr_id") String id,
            @Field("pr_is_featured") String pr_is_featured

    );

    @FormUrlEncoded
    @POST("api/v1/Product/update/")
    Call<ServerResponsechrt> Productdetailupdate(
            @Header("Authorizations") String Token,
            @Field("pr_id") String id,
            @Field("pr_name") String pr_name,
            @Field("pr_category") String pr_category,
            @Field("pr_description") String pr_description,
            @Field("pr_price") String pr_price,
            @Field("pr_location") String pr_location,
            @Field("pr_lat") String pr_lat,
            @Field("pr_long") String pr_long

    );

    @GET("api/v1/profile/notification")
    Call<AllNotice> allnotice(
            @Header("Authorizations") String Token
    );

    @GET("api/v1/Common/countnotification")
    Call<AllNotice> allnotice1(
            @Header("Authorizations") String Token
    );

//    @FormUrlEncoded
//    @POST("api/v1/profile/invitebuddy")
//    Call<CommonResponse> inviteBuddy(
//            @Header("Authorizations") String Token,
//            @Field("trip_id") String trip_id,
//            @Field("contact_list2") String contactList,
//            @Field("contact_list") JSONArray jsonContactList,
//            @Field("contact_list1") String[] inviteeList
//    );

    @FormUrlEncoded
    @POST("api/v1/profile/invitebuddy")
    Call<CommonResponse> inviteBuddy(
            @Header("Authorizations") String Token,
            @Field("trip_id") String trip_id,
            @Field("contact_list") String contactList
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/invite")
    Call<CommonResponseforBuddy> inviteBuddiesfriend(
            @Header("Authorizations") String Token,
            @Field("trip_id") String trip_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/v1/Buddy/create")
    Call<CommonResponse> addBuddy(
            @Header("Authorizations") String Token,
            @Field("buddy_id") String buddy_id
    );

    @FormUrlEncoded
    @POST("api/v1/Search")
    Call<AfterLoginStatus> allpeople1(
            @Header("Authorizations") String Token,
            @Field("location_lat") Double location_lat,
            @Field("location_long") Double location_long,
            @Field("radious") String radious,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/search/")
    Call<AllTrip> alltrip1(
            @Header("Authorizations") String Token,
            @Field("location_lat") Double location_lat,
            @Field("location_long") Double location_long,
            @Field("radius") String radius
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/search/")
    Call<AllTrip> alltrip18(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") String radius
    );

    @FormUrlEncoded
    @POST("api/v1/Charter/search/")
    Call<AllCharter> allcharter1(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") String radius
    );


    @FormUrlEncoded
    @POST("api/v1/Product/search")
    Call<AllProduct> allproduct1(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") String radius,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Service/search")
    Call<AllService> allservice1(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") String radius
    );

    @FormUrlEncoded
    @POST("api/v1/Course/search/")
    Call<AllCourse> allcourse1(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") String radius,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/search/")
    Call<AllTrip> alltrip11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") Integer radius
    );

    @FormUrlEncoded
    @POST("api/v1/Charter/search/")
    Call<AllCharter> allcharter11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") Integer radius
    );

    @FormUrlEncoded
    @POST("api/v1/Product/search")
    Call<AllProduct> allproduct11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") Integer radius,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Service/search")
    Call<AllService> allservice11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") Integer radius
    );

    @FormUrlEncoded
    @POST("api/v1/Course/search/")
    Call<AllCourse> allcourse11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radius") Integer radius,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api/v1/Search")
    Call<AfterLoginStatus> allpeople11(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radious") String radious,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("api/v1/Search")
    Call<AfterLoginStatus> allpeople(
            @Header("Authorizations") String Token,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long,
            @Field("radious") String radious,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("api/v1/Trip/participateaccept")
    Call<AfterLoginStatus> actnot(
            @Header("Authorizations") String Token,
            @Field("event_id") String event_id,
            @Field("status") String status,
            @Field("participant_id") String participant_id,
            @Field("notification_id") String notification_id
    );

    @FormUrlEncoded
    @POST("api/v1/Buddy/accept")
    Call<AfterLoginStatus> buddyacceptdec(
            @Header("Authorizations") String Token,
            @Field("user_id") String user_id,
            @Field("status") String status,
            @Field("notification_id") String notification_id
    );

    @FormUrlEncoded
    @POST("api/v1/Users/social")
    Call<CommonResponse> fblog(
            @Field("social_id") String social_id,
            @Field("social") String social,
            @Field("fname") String fname,
            @Field("lname") String lname
            , @Field("device_type") String deviceType
    );

    @FormUrlEncoded
    @POST("api/v1/Profile/notificationviewed")
    Call<AfterLoginStatus> chclr(
            @Header("Authorizations") String Token,
            @Field("notification_id") String notification_id
    );

    @FormUrlEncoded
    @POST("api/v1/User/resendotp/")
    Call<ResendOtpModel> resendOtp(
            @Field("user_id") String user_id,
            @Field("mobile_no") String mobile_no
    );

    @FormUrlEncoded
    @POST("api/v1/Common/chat")
    Call<String> sendChatNotification(
            @Header("Authorizations") String Token,
//            @Field("user_name") String UserName,
            @Field("email") String email,
            @Field("message") String message
//            , @Field("device_type") String deviceType
    );

}

