package com.cryptocenter.andrey.owlsight.utils;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import java.nio.ByteBuffer;

import androidx.annotation.NonNull;

public class CameraModelLoaderFactory implements ModelLoaderFactory<GetThumbnailRequest, ByteBuffer> {

    private final OwlsightRepository owlsightRepository;

    public CameraModelLoaderFactory(OwlsightRepository owlsightRepository){
        this.owlsightRepository = owlsightRepository;
    }
    @NonNull
    @Override
    public ModelLoader<GetThumbnailRequest, ByteBuffer> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new CameraModelLoader(owlsightRepository);
    }

    @Override
    public void teardown() {

    }
}
