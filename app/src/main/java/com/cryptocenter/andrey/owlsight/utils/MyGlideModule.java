package com.cryptocenter.andrey.owlsight.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;

import java.nio.ByteBuffer;

import androidx.annotation.NonNull;
import toothpick.Toothpick;

@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        OwlsightRepository repository = Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class);
        registry.append(GetThumbnailRequest.class, ByteBuffer.class, new CameraModelLoaderFactory(repository));
    }
}
