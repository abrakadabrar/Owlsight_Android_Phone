package com.cryptocenter.andrey.owlsight.data.repository.owlsight;

import com.cryptocenter.andrey.owlsight.data.api.OwlsightAPI;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.api.response.RecordsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.Response;
import com.cryptocenter.andrey.owlsight.data.model.api.response.StreamResponse;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.cryptocenter.andrey.owlsight.data.model.motion.DatumFramesMotions;
import com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion.Datum;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.utils.ResourcesUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.registerFirstStep(
                data.getName().split(" ")[0], data.getName().split(" ")[1], data.getEmail(),
                data.getPhone(), data.getPassword(), data.getConfirmPassword())
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
    public void registerSecondStep(
            RegisterData data,
            String confirmSms,
            Response.Start startListener,
            Response.Success<Void> successListener,
            Response.Failed failedListener,
            Response.Error errorListener,
            Response.Complete completeListener
    ) {
        api.registerSecondStep(
                data.getName().split(" ")[0], data.getName().split(" ")[1], data.getEmail(),
                data.getPhone(), data.getPassword(), data.getConfirmPassword(), confirmSms)
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
        api.stopStream(ResourcesUtils.getDeviceName(), preferences.getCookie())
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