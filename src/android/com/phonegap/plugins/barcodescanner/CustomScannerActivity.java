package com.phonegap.plugins.barcodescanner;
/**
 * Created by Ahsanwarsi on 11/8/2016.
 * Copyright (c) 2016 Kwanso. All rights reserved.
 */

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
// import com.phonegap.plugins.barcodescanner.R;
import com.LogicSquare.Price12.R;

public class CustomScannerActivity extends Activity implements
        CompoundBarcodeView.TorchListener{

    private static final int BarCodeScannerViewControllerUserCanceledErrorCode = 99991;
    private static final String TAG = CustomScannerActivity.class.getSimpleName();


    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;
    private ToggleButton switchFlashlightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        /////
        barcodeScannerView = (CompoundBarcodeView)findViewById(R.id.barcode_view);
        barcodeScannerView.setTorchListener(this);

        switchFlashlightButton = (ToggleButton)findViewById(R.id.toggle_btn);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        switchFlashlightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state here
                if (isChecked) {
                    barcodeScannerView.setTorchOn();
                } else {
                    barcodeScannerView.setTorchOff();
                }
            }
        });

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
        ////
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        // necessary override..
    }


    @Override
    public void onTorchOff() {
        // necessary override..
    }

}
