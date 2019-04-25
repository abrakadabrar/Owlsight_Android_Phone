package com.cryptocenter.andrey.owlsight.data.repository.owlsight;

import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.api.response.AddCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.RecordsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.Response;
import com.cryptocenter.andrey.owlsight.data.model.api.response.StreamResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.TestCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.ThumbnailResponse;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.cryptocenter.andrey.owlsight.data.model.motion.DatumFramesMotions;
import com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion.Datum;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public interface OwlsightRepository {

    void login(String email, String password,
               Response.Start startListener,
               Response.Success<Void> successListener,
               Response.Failed failedListener,
               Response.Error errorListener,
               Response.Complete completeListener);

    void registerFirstStep(
            RegisterData registerData,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.FailedWithMessage failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void registerSecondStep(
            RegisterData registerData, String confirmSms,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.FailedWithMessage failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getGroupsCameras(
            Response.Start startListener,
            Response.Success<List<Group>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getCameraThumbnailUpdated(
            String id,
            Response.Success<String> successListener);

    void addGroup(
            String title,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void addMotionNotification(
            Integer cameraId,
            String token,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void deleteMotionNotification(
            Integer cameraId,
            String token,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void deleteCamera(
            Integer id,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void editGroup(
            String id, String title,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void deleteGroup(
            String id,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getCamera(
            String id,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getCameraThumbnail(
            String id,
            Response.Success<String> successListener);

    void getCameraRecords(
            String id,
            Response.Start startListener,
            Response.Success<RecordsResponse> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getMonitors(
            Response.Start startListener,
            Response.Success<List<Monitor>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void getMonitorInfo(
            MonitorCamera camera,
            Response.Success<MonitorCamera> successListener,
            Response.Failed failedListener,
            Response.Error errorListener);

    void prepareStream(
            Response.Start startListener,
            Response.Success<StreamResponse> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void stopStream(
            String id,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void statusStream(
            String id,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener);

    void testCamera(String streamLink,
                    Response.Start startListener,
                    Response.Success<TestCameraResponse> successListener,
                    Response.Failed failedListener,
                    Response.Error errorListener,
                    Response.Complete completeListener);

    void addCamera(
            int groupId, String cameraName,
            String host, int port,
            String request, String login,
            String password, String tz,
            Response.Start startListener,
            Response.Success<AddCameraResponse> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    void records(
            String id,
            String folder,
            Response.Start startListener,
            Response.Success<List<Datum>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    Disposable motions(
            String id,
            String from,
            String to,
            Response.Start startListener,
            Response.Success<List<DatumFramesMotions>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener);

    Observable<ResponseBody> downloadVideo(String path, String id);

    Observable<ThumbnailResponse> getCameraThumbnail(String id);

    Observable<ThumbnailResponse> getCameraThumbnailUpdated(String id);

    Observable<retrofit2.Response<JsonObject>> resetPassword(String email);
}