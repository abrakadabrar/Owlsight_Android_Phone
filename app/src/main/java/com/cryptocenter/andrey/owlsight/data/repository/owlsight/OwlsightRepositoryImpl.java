package com.cryptocenter.andrey.owlsight.data.repository.owlsight;

import com.annimon.stream.Stream;
import com.cryptocenter.andrey.owlsight.data.api.OwlsightAPI;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.api.response.AddCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.ErrorBody;
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
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.utils.ResourcesUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OwlsightRepositoryImpl implements OwlsightRepository {

    private OwlsightAPI api;
    private Preferences preferences;

    public OwlsightRepositoryImpl(OwlsightAPI api, Preferences preferences) {
        this.api = api;
        this.preferences = preferences;
    }

    @Override
    public void login(
            String email,
            String password,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("m", email);
        map.put("p", password);

        api.login(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    String cookie = response.headers().get("Set-Cookie");
                    cookie = cookie.split(";")[0];
                    preferences.saveCookie(cookie);
                    return response.code();
                })
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        code -> {
                            if (code == 200) successListener.onSuccess(null);
                            if (code == 401) failedListener.onFailed();
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getCameraThumbnailUpdated(
            String id,
            Response.Success<String> successListener
    ) {
        api.getCameraThumbnailUpdated(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getSuccess()) {
                                successListener.onSuccess(response.getData());
                            }
                        }, throwable -> {
                        }
                );
    }


    @Override
    public void registerFirstStep(
            RegisterData data,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.FailedWithMessage failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.registerFirstStep(data.getName(), data.getEmail(),
                data.getPhone(), data.getPassword(), data.getConfirmPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    String cookie = response.headers().get("Set-Cookie");
                    cookie = cookie.split(";")[0];
                    preferences.saveCookie(cookie);
                    return response;
                })
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.code() == 200) successListener.onSuccess(null);
                            if (response.code() == 401) {
                                StringBuilder errors = new StringBuilder();
                                String errorBodyString = response.errorBody().string();
                                ErrorBody errorBody = new Gson().fromJson(errorBodyString, ErrorBody.class);
                                Stream.ofNullable(errorBody.getErrors())
                                        .forEach(error -> errors.append(error).append("\n\n"));
                                failedListener.onFailed(errors.toString());
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void registerSecondStep(
            RegisterData data,
            String confirmSms,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.FailedWithMessage failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.registerSecondStep(
                data.getName(), data.getEmail(),
                data.getPhone(), data.getPassword(), data.getConfirmPassword(), confirmSms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    String cookie = response.headers().get("Set-Cookie");
                    cookie = cookie.split(";")[0];
                    preferences.saveCookie(cookie);
                    return response;
                })
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.code() == 200) successListener.onSuccess(null);
                            if (response.code() == 401) {
                                StringBuilder errors = new StringBuilder();
                                String errorBodyString = response.errorBody().string();
                                ErrorBody errorBody = new Gson().fromJson(errorBodyString, ErrorBody.class);
                                Stream.ofNullable(errorBody.getErrors())
                                        .forEach(error -> errors.append(error).append("\n\n"));
                                failedListener.onFailed(errors.toString());
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getGroupsCameras(
            Response.Start startListener,
            Response.Success<List<Group>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.cameras(preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> successListener.onSuccess(response.getData()),
                        errorListener::onError
                );
    }

    @Override
    public void addGroup(
            String title,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.addGroup(title, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response.get("success").toString().equals("true"))
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        isSuccess -> {
                            if (isSuccess) {
                                successListener.onSuccess(title);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void addMotionNotification(Integer cameraId, String token, Response.Start startListener, Response.Success<String> successListener, Response.Failed failedListener, Response.Error errorListener, Response.Complete completeListener) {
        api.addMotionNotification(cameraId, token, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {

                }, error -> {

                });
    }

    @Override
    public void deleteMotionNotification(Integer cameraId, String token, Response.Start startListener, Response.Success<String> successListener, Response.Failed failedListener, Response.Error errorListener, Response.Complete completeListener) {
        api.deleteMotionNotification(cameraId, token, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {

                }, error -> {

                });
    }

    @Override
    public void deleteCamera(Integer id, Response.Start startListener, Response.Success<Void> successListener, Response.Failed failedListener, Response.Error errorListener, Response.Complete completeListener) {
        api.deleteCamera(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .map(response -> response.get("success").toString().equals("true"))
                .subscribe(isSuccess -> {
                    if (isSuccess) {
                        successListener.onSuccess(null);
                    } else {
                        failedListener.onFailed();
                    }
                }, error -> {

                });
    }

    @Override
    public void editGroup(
            String id, String title,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.editGroup(title, id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response.get("success").toString().equals("true"))
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        isSuccess -> {
                            if (isSuccess) {
                                successListener.onSuccess(title);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void deleteGroup(
            String id,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.deleteGroup(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response.get("success").getAsBoolean())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        isSuccess -> {
                            if (isSuccess) {
                                successListener.onSuccess(null);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getCamera(
            String id,
            Response.Start startListener,
            Response.Success<String> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.getCamera(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response.toString().contains("Работает"))
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        isSuccess -> {
                            if (isSuccess) {
                                successListener.onSuccess(id);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getCameraThumbnail(
            String id,
            Response.Success<String> successListener
    ) {
        api.getCameraThumbnail(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getSuccess()) {
                                successListener.onSuccess(response.getData());
                            }
                        }, throwable -> {
                        }
                );
    }

    @Override
    public void getCameraRecords(
            String id,
            Response.Start startListener,
            Response.Success<RecordsResponse> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.getCameraRecords(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.getSuccess()) {
                                successListener.onSuccess(response);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getMonitors(
            Response.Start startListener,
            Response.Success<List<Monitor>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.getMonitors(preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.getSuccess()) {
                                successListener.onSuccess(response.getData());
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void getMonitorInfo(
            MonitorCamera monitorCamera,
            Response.Success<MonitorCamera> successListener,
            Response.Failed failedListener,
            Response.Error errorListener
    ) {
        api.getMonitorInfo(monitorCamera.getId(), preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (!response.isSuccessful() || response.body() == null) return false;
                    return response.body().get("camResult").toString().contains("Работает");
                })
                .subscribe(
                        isRunning -> {
                            monitorCamera.setRunning(isRunning);
                            successListener.onSuccess(monitorCamera);
                        },
                        errorListener::onError
                );
    }

    @Override
    public void prepareStream(
            Response.Start startListener,
            Response.Success<StreamResponse> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.prepareStream(ResourcesUtils.getDeviceName(), preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.getSuccess() && response.getData() != null) {
                                successListener.onSuccess(response);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void stopStream(
            String id,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.stopStream(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(null);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void statusStream(
            String id,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener
    ) {
        api.statusStream(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(null);
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }


    @Override
    public void records(
            String id,
            String folder,
            Response.Start startListener,
            Response.Success<List<Datum>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.getRecords(id, folder, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(response.body().getData());
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public void testCamera(String streamLink,
                           Response.Start startListener,
                           Response.Success<TestCameraResponse> successListener,
                           Response.Failed failedListener,
                           Response.Error errorListener,
                           Response.Complete completeListener
    ) {
        api.testCamera(streamLink,
                preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(response.body());
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    @Override
    public Observable<ResponseBody> downloadVideo(String path, String id) {
        return api.getFile(path, id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public Observable<ThumbnailResponse> getCameraThumbnail(String id) {
        return api.getCameraThumbnail(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ThumbnailResponse> getCameraThumbnailUpdated(String id) {
        return api.getCameraThumbnailUpdated(id, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void addCamera(int groupId, String cameraName,
                          String host, int port,
                          String request, String login,
                          String password, String tz,
                          Response.Start startListener,
                          Response.Success<AddCameraResponse> successListener,
                          Response.Failed failedListener, Response.Error errorListener,
                          Response.Complete completeListener) {
        api.addCamera(getAddCameraParams(groupId, cameraName, host,
                port, request, login, password, tz), preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(() -> completeListener.onComplete())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(response.body());
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }

    public static LinkedHashMap<String, String> getAddCameraParams(int groupId, String cameraName,
                                                                   String host, int port,
                                                                   String request, String login,
                                                                   String password, String tz) {
        LinkedHashMap<String, String> map = new LinkedHashMap();
        map.put("groupId", String.valueOf(groupId));
        map.put("cameraName", cameraName);
        map.put("host", host);
        map.put("port", String.valueOf(port));
        map.put("request", request);
        map.put("login", login);
        map.put("password", password);
        map.put("tz", tz);
        return map;
    }

    @Override
    public Disposable motions(
            String id,
            String from,
            String to,
            Response.Start startListener,
            Response.Success<List<DatumFramesMotions>> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        return api.getMotions(id, from, to, preferences.getCookie())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> startListener.onStart())
                .doFinally(completeListener::onComplete)
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                successListener.onSuccess(response.body().getData());
                            } else {
                                failedListener.onFailed();
                            }
                        },
                        errorListener::onError
                );
    }
}