package com.cryptocenter.andrey.owlsight.utils;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import java.nio.ByteBuffer;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CameraModelLoader implements ModelLoader<GetThumbnailRequest, ByteBuffer> {

    private final OwlsightRepository owlsightRepository;

    public CameraModelLoader(OwlsightRepository owlsightRepository) {
        this.owlsightRepository = owlsightRepository;
    }

    @Nullable
    @Override
    public LoadData<ByteBuffer> buildLoadData(@NonNull GetThumbnailRequest request, int width, int height, @NonNull Options options) {
        String key = String.format(Locale.getDefault(), "%s%d%d", request.getCamera().getCameraId(), width, height);
        return new ModelLoader.LoadData<>(new ObjectKey(key), new CameraDataFetcher(owlsightRepository, request));
    }

    @Override
    public boolean handles(@NonNull GetThumbnailRequest request) {
        return true;
    }
}
