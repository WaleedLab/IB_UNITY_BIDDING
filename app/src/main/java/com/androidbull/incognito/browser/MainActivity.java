package com.androidbull.incognito.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.mediation.IInitializationListener;
import com.unity3d.mediation.IInterstitialAdLoadListener;
import com.unity3d.mediation.IInterstitialAdShowListener;
import com.unity3d.mediation.InitializationConfiguration;
import com.unity3d.mediation.InterstitialAd;
import com.unity3d.mediation.UnityMediation;
import com.unity3d.mediation.errors.LoadError;
import com.unity3d.mediation.errors.SdkInitializationError;
import com.unity3d.mediation.errors.ShowError;

public class MainActivity extends AppCompatActivity implements IInitializationListener {
    private static final String TAG = "MainActivity";
    private final String IB_GAME_ID = "4565731";
    private final String INTERSTITIAL_AD_UNIT_ID = "Interstitial_Android";
    InterstitialAd interstitialAd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        interstitialAd = new InterstitialAd(this, INTERSTITIAL_AD_UNIT_ID);
        Button mBtnClickMe = findViewById(R.id.btn_click);
        mBtnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                    @Override
                    public void onInterstitialShowed(InterstitialAd interstitialAd) {
                        // The ad has started to show.
                    }

                    @Override
                    public void onInterstitialClicked(InterstitialAd interstitialAd) {
                        // The user has selected the ad.
                    }

                    @Override
                    public void onInterstitialClosed(InterstitialAd interstitialAd) {
                        // The ad has finished showing.
                    }

                    @Override
                    public void onInterstitialFailedShow(InterstitialAd interstitialAd, ShowError error, String msg) {
                        // An error occurred during the ad playback.
                        Log.e(TAG, "onInterstitialFailedShow: error: " +msg );
                    }
                };

                interstitialAd.show(showListener);
            }
        });
//        loadAds();
        loadMediationAds();
    }

    private void loadMediationAds() {
        InitializationConfiguration configuration = InitializationConfiguration.builder()
                .setGameId(IB_GAME_ID)
                .setInitializationListener(this)
                .build();

        UnityMediation.initialize(configuration);

    }

    private void loadAds() {


        // Declare a new listener:
        final IUnityAdsInitializationListener myAdsListener = new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.i(TAG, "onInitializationComplete: unity initialisation completed");
                UnityAds.load("Banner_Android", new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String s) {
                        Log.i(TAG, "onUnityAdsAdLoaded: banner ad: " + s);
                        loadBannerAd();
                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                        Log.e(TAG, "onUnityAdsFailedToLoad: failed to load: " + s + "\n" + unityAdsLoadError.name());
                    }
                });
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
                Log.e(TAG, "onInitializationFailed: unity initialisation failed");
            }
        };
        // Initialize the SDK:
        UnityAds.initialize(this, "4565731", false, myAdsListener);


    }

    private void loadBannerAd() {
        UnityAds.load("Banner_Android", new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {
                Log.i(TAG, "onUnityAdsAdLoaded: banner ad: " + s);
            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                Log.e(TAG, "onUnityAdsFailedToLoad: failed to load: " + s + "\n" + unityAdsLoadError.name());
                loadBannerAd();
            }
        });
    }

    @Override
    public void onInitializationComplete() {
        Log.i(TAG, "onInitializationComplete: ");
// Implement a load listener interface:
        final IInterstitialAdLoadListener loadListener = new IInterstitialAdLoadListener() {
            @Override
            public void onInterstitialLoaded(InterstitialAd ad) {
                // Execute logic when the ad successfully loads.
                Log.i(TAG, "onInterstitialLoaded: ");
            }

            @Override
            public void onInterstitialFailedLoad(InterstitialAd ad, LoadError error, String msg) {
                Log.e(TAG, "onInterstitialFailedLoad: error: " + error.name() + "\n\nMessage: " + msg);
                // Execute logic when the ad fails to load.
            }
        };

// Load an ad:
        interstitialAd.load(loadListener);

    }

    @Override
    public void onInitializationFailed(SdkInitializationError sdkInitializationError, String s) {
        Log.e(TAG, "onInitializationFailed: error: " + sdkInitializationError + "\n\nString: " + s);
    }
}