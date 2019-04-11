package com.cryptocenter.andrey.owlsight.notification;

import android.util.Log;

import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.google.firebase.messaging.FirebaseMessagingService;

import toothpick.Toothpick;

public class OwlsightMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        Preferences preferences = Toothpick.openScope(Scopes.APP).getInstance(Preferences.class);
        preferences.setFirebaseNotificationToken(s);
        Log.d("FirebaseToken", String.format("New token =%s", s));
    }

}
