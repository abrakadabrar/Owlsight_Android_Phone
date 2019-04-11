package com.cryptocenter.andrey.owlsight.utils;

import android.util.Base64;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import java.nio.ByteBuffer;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class CameraDataFetcher implements DataFetcher<ByteBuffer> {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final OwlsightRepository owlsightRepository;
    private final GetThumbnailRequest request;

    @Inject
    public CameraDataFetcher(OwlsightRepository owlsightRepository, GetThumbnailRequest request) {
        this.owlsightRepository = owlsightRepository;
        this.request = request;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super ByteBuffer> callback) {
        if (!request.isUpdated()) {
            compositeDisposable.add(owlsightRepository.getCameraThumbnail(request.getCamera().getCameraId())
                    .map(thumbnailResponse -> {
                        byte[] decodedBytes = Base64.decode(thumbnailResponse.getData(), Base64.DEFAULT);
                        return ByteBuffer.wrap(decodedBytes);
                    })
                    .subscribe(callback::onDataReady, error -> callback.onLoadFailed(new Exception(error))));
        } else {
            compositeDisposable.add(owlsightRepository.getCameraThumbnailUpdated(request.getCamera().getCameraId())
                    .map(thumbnailResponse -> {
                        byte[] decodedBytes = Base64.decode(thumbnailResponse.getData(), Base64.DEFAULT);
                        return ByteBuffer.wrap(decodedBytes);
                    })
                    .subscribe(callback::onDataReady, error -> callback.onLoadFailed(new Exception(error))));
        }
    }

    @Override
    public void cleanup() {
        compositeDisposable.clear();
    }

    @Override
    public void cancel() {
        compositeDisposable.clear();
    }

    @NonNull
    @Override
    public Class<ByteBuffer> getDataClass() {
        return ByteBuffer.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
