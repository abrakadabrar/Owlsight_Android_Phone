package com.cryptocenter.andrey.owlsight.utils.FingerPrint;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;

import com.samsung.android.sdk.pass.SpassFingerprint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static android.content.Context.KEYGUARD_SERVICE;

public class Fingerprint {

    private Context mContext;
    private CancellationSignal mCancellationSignal;
    private OnAuthenticationFingerprintCallback mCallback;
    private SpassFingerprint mSpassFingerprint;

    private int mErrorCount = 0;
    private boolean needRetryIdentify = false;

    private final static String TAG = "FingerprintView";

    public Fingerprint(Context context) {
        mContext = context;
        mSpassFingerprint = new SpassFingerprint(context);
    }

    public enum SensorState {
        NOT_SUPPORTED,
        NOT_BLOCKED, // если устройство не защищено пином, рисунком или паролем
        NO_FINGERPRINTS, // если на устройстве нет отпечатков
        READY
    }

    public void setOnAuthenticationFingerprintCallback(OnAuthenticationFingerprintCallback callback) {
        mCallback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static SensorState checkSensorState(@NonNull Context context) {
        if (checkFingerprintCompatibility(context)) {
            KeyguardManager keyguardManager =
                    (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            if (!keyguardManager.isKeyguardSecure()) {
                return SensorState.NOT_BLOCKED;
            }
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            //if (!fingerprintManager.hasEnrolledFingerprints()) {
            if (!FingerPrintUtils.isFingerPrintAvailable(context)) {
                return SensorState.NO_FINGERPRINTS;
            }
            return SensorState.READY;
        } else {
            return SensorState.NOT_SUPPORTED;
        }
    }

    public void cancelScanning() {
        mCancellationSignal.cancel();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSensorStateAt(@NonNull SensorState state, @NonNull Context context) {
        return checkSensorState(context) == state;
    }

    public static boolean checkFingerprintCompatibility(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                return FingerPrintUtils.isFingerPrintAvailable(context);
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(Context context, FingerprintManager.CryptoObject cryptoObject) {
        mCancellationSignal = new CancellationSignal();
        if (FingerPrintUtils.isFingerPrintAvailable(context)) {
            if (!FingerPrintUtils.isFingerPrintForSamsungSdk(context)) {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                fingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (mCallback != null) {
                            if(errorCode != 5){ // TODO: quick costil
                                mCallback.onAcuthenticationFingerprintError(!TextUtils.isEmpty(errString) ? errString.toString() : null);
                            }
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                        //if (mCallback != null) {
                        // mCallback.onAcuthenticationFingerprintError(!TextUtils.isEmpty(helpString) ? helpString.toString() : null);
                        //}
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        if (mCallback != null) {
                            mCallback.onAuthenticationFingerprintSucceeded();
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        mErrorCount++;
                        boolean isCanceled = false;
                        if (mErrorCount == 5) {
                            mErrorCount = 0;
                            cancelScanning();
                            isCanceled = true;
                        }
                        if (mCallback != null) {
                            mCallback.onAuthenticationFingerprintFailed(isCanceled);
                        }
                    }
                }, null);
            } else {

                startIdentify();
            }
        } else {
            Log.e(TAG, "Fingerprint scanner not detected or no fingerprint enrolled. Use FingerprintView#isAvailable(Context) before.");

        }
    }

    private void startIdentify() {
        if (mSpassFingerprint != null) {
            mSpassFingerprint.startIdentify(mIdentifyListener);
        }
    }

    private SpassFingerprint.IdentifyListener mIdentifyListener = new SpassFingerprint.IdentifyListener() {
        @Override
        public void onFinished(int eventStatus) {

            if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
                if (mCallback != null) {
                    mCallback.onAuthenticationFingerprintSucceeded();
                }
                mErrorCount = 0;
                needRetryIdentify = false;
            } else if (eventStatus == SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS) {

            } else if (eventStatus == SpassFingerprint.STATUS_OPERATION_DENIED) {

            } else if (eventStatus == SpassFingerprint.STATUS_USER_CANCELLED) {

            } else if (eventStatus == SpassFingerprint.STATUS_TIMEOUT_FAILED) {
                needRetryIdentify = true;
            } else if (eventStatus == SpassFingerprint.STATUS_QUALITY_FAILED) {
                mErrorCount++;
                needRetryIdentify = true;
                boolean isCanceled = false;
                if (mErrorCount >= 5) {
                    mErrorCount = 0;
                    cancelScanning();
                    isCanceled = true;
                    needRetryIdentify = false;
                }
                if (mCallback != null) {
                    mCallback.onAuthenticationFingerprintFailed(isCanceled);
                }

            }
        }

        @Override
        public void onReady() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onCompleted() {
            if (needRetryIdentify) {
                startIdentify();
            }
        }
    };

    public void cancel() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
        }
    }

    public interface OnAuthenticationFingerprintCallback {

        void onAuthenticationFingerprintSucceeded();

        void onAuthenticationFingerprintFailed(boolean isCanceled);

        void onAcuthenticationFingerprintError(String error);
    }

}
