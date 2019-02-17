package com.cryptocenter.andrey.owlsight.data.api;

import com.cryptocenter.andrey.owlsight.data.model.api.response.StreamResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.RecordsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.GroupsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.MonitorsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.ThumbnailResponse;
import com.cryptocenter.andrey.owlsight.data.model.records.MotionsRoot;
import com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion.Example;
import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OwlsightAPI {

    @FormUrlEncoded
    @POST("/user/login/ajax/")
    Observable<Response<JsonObject>> login(@FieldMap HashMap<String, String> authData);

    @GET("/user/register/app-first-step")
    Observable<Response<JsonObject>> registerFirstStep(
            @Query("firstName") String firstName,
            @Query("lastName") String lastName,
            @Query("mail") String email,
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("rePassword") String rePassword);


    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("/api/papi/get-updated-thumbnail?&w=500&h=500")
    Observable<ThumbnailResponse> getCameraThumbnailUpdated(
            @Query("id") String id,
            @Header("Cookie") String cookie);


    @GET("/user/register/app-second-step")
    Observable<Response<JsonObject>> registerSecondStep(
            @Query("firstName") String firstName,
            @Query("lastName") String lastName,
            @Query("mail") String email,
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("rePassword") String rePassword,
            @Query("confirm") String confirm);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("api/papi/get-user-cams-to-json/")
    Observable<GroupsResponse> cameras(@Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("api/papi/add-group")
    Observable<JsonObject> addGroup(
            @Query("name") String name,
            @Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("api/papi/add-group")
    Observable<JsonObject> editGroup(
            @Query("name") String name,
            @Query("id") String groupId,
            @Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("/cabinet/view/delete-group")
    Observable<JsonObject> deleteGroup(
            @Query("ids") String ids,
            @Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("/cabinet/view/get-streams-info")
    Observable<JsonObject> getCamera(
            @Query("ids") String ids,
            @Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("/api/papi/get-thumbnail?&w=500&h=500")
    Observable<ThumbnailResponse> getCameraThumbnail(
            @Query("id") String id,
            @Header("Cookie") String cookie);

    @Headers({"Accept: */*", "Content-Type: application/json; charset=UTF-8"})
    @GET("/api/papi/user-cam-record-folders")
    Observable<RecordsResponse> getCameraRecords(
            @Query("id") String id,
            @Header("Cookie") String cookie);

    @GET("/api/papi/get-the-view")
    Observable<MonitorsResponse> getMonitors(
            @Header("Cookie") String cookie);

    @GET("/cabinet/view/get-streams-info")
    Observable<Response<JsonObject>> getMonitorInfo(
            @Query("ids") String ids,
            @Header("Cookie") String cookie);

    @GET("/api/papi/add-camera?tz=10800\"")
    Observable<StreamResponse> prepareStream(
            @Query("name") String name,
            @Header("Cookie") String cookie);

    @GET("/api/papi/add-camera?tz=10800\"")
    Observable<Response<JsonObject>> stopStream(
            @Query("id") String id,
            @Header("Cookie") String cookie);

    @GET("/api/papi/set-hello")
    Observable<Response<JsonObject>> statusStream(
            @Query("id") String id,
            @Header("Cookie") String cookie);

    @GET("/api/papi/user-cam-record-folders")
    Observable<Response<Example>> getRecords(
            @Query("id") String cameraId,
            @Query("folder") String folder,
            @Header("Cookie") String cookie);

    @GET("/api/papi/get-motion-for-period")
    Observable<Response<MotionsRoot>> getMotions(
            @Query("id") String cameraId,
            @Query("from") String from,
            @Query("to") String to,
            @Header("Cookie") String cookie);
}
