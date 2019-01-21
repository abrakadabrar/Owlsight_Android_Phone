package com.cryptocenter.andrey.owlsight.data.model.api.response;

public class Response<T> {

    public interface Success<T> {
        void onSuccess(T result);
    }

    public interface Error {
        void onError(Throwable error);
    }

    public interface Failed {
        void onFailed();
    }

    public interface Exist {
        void onExist();
    }

    public interface Start {
        void onStart();
    }

    public interface Complete {
        void onComplete();
    }
}
