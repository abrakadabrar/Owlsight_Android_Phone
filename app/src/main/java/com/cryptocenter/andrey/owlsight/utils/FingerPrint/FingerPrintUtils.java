package com.cryptocenter.andrey.owlsight.utils.FingerPrint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

public class FingerPrintUtils {

    public static boolean isFingerPrintAvailable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            /*   if (fingerprintManager != null) {
                return (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints());
            }*/
            boolean isFingerprintSupported = fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
            if (!isFingerprintSupported && SsdkVendorCheck.isSamsungDevice()) {
                Spass spass = new Spass();
                SpassFingerprint spassFingerprint = new SpassFingerprint(context);
                try {
                    spass.initialize(context);
                    isFingerprintSupported = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT) && spassFingerprint.hasRegisteredFinger();
                } catch (SsdkUnsupportedException | UnsupportedOperationException e) {
                    // Error handling
                }
            }
            return isFingerprintSupported;
        }
        return false;
    }

    public static boolean isFingerPrintForSamsungSdk(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            boolean isFingerprintSupported = fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
            if (!isFingerprintSupported && SsdkVendorCheck.isSamsungDevice()) {
                Spass spass = new Spass();
                SpassFingerprint spassFingerprint = new SpassFingerprint(context);
                try {
                    spass.initialize(context);
                    isFingerprintSupported = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT) && spassFingerprint.hasRegisteredFinger();
                    return isFingerprintSupported;
                } catch (SsdkUnsupportedException | UnsupportedOperationException e) {
                    // Error handling
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

}
