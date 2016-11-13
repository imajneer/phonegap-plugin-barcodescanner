package com.phonegap.plugins.barcodescanner;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.util.Log;

//
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.content.pm.PackageManager;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

public class BarcodeScanner extends CordovaPlugin {
  
  // public static final int REQUEST_CODE = "49374"; //0x0ba7c0de;
  private static final String SCAN = "scan";
  private static final String ENCODE = "encode";
  private static final String CANCELLED = "cancelled";
  private static final String FORMAT = "format";
  private static final String TEXT = "text";
  private static final String DATA = "data";
  private static final String TYPE = "type";
  private static final String PREFER_FRONTCAMERA = "preferFrontCamera";
  private static final String ORIENTATION = "orientation";
  private static final String SHOW_FLIP_CAMERA_BUTTON = "showFlipCameraButton";
  private static final String FORMATS = "formats";
  private static final String PROMPT = "prompt";
  private static final String TEXT_TYPE = "TEXT_TYPE";
  private static final String EMAIL_TYPE = "EMAIL_TYPE";
  private static final String PHONE_TYPE = "PHONE_TYPE";
  private static final String SMS_TYPE = "SMS_TYPE";
  private static final String LOG_TAG = "BarcodeScanner";
  private String [] permissions = { Manifest.permission.CAMERA };
  
  private CallbackContext callbackContext;
  private JSONArray requestArgs;
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
      this.callbackContext = callbackContext;
      this.requestArgs = data;
       Log.i("===========================", "================================");
      
      if(!hasPermisssion()) {
              requestPermissions(0);
            } else {
              scan();
            }

        return true;
    }
    
    public void scan() {
      this.cordova.setActivityResultCallback(this);
      new IntentIntegrator(this.cordova.getActivity()).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      Log.i("===========================", "Inside> onActivityResult");
      Log.i("==========>>>", "requestCode: "+requestCode);
      
      if(this.callbackContext == null){
        Log.i("==========>>>", "callbackContext > NULL");  
      }else{
        Log.i("==========>>>", "callbackContext > NOT NULL");
      }
      
      if(intent == null){
        Log.i("==========>>>", "intent > NULL");  
      }else{
        Log.i("==========>>>", "intent > NOT NULL");
      }
      
      // if (requestCode == REQUEST_CODE && this.callbackContext != null) {
        if (this.callbackContext != null) {
          Log.i("===========================", "Inside> REQUEST_CODE");
            if (resultCode == Activity.RESULT_OK) {
              Log.i("===========================", "Inside> Activity.RESULT_OK");
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, intent.getStringExtra("SCAN_RESULT"));
                    obj.put(FORMAT, intent.getStringExtra("SCAN_RESULT_FORMAT"));
                    obj.put(CANCELLED, false);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                Log.i("=============RESULT_OK==============", ""+obj.toString());
                this.callbackContext.success(obj);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("===========================", "Inside> Activity.RESULT_CANCELED");
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, "");
                    obj.put(FORMAT, "");
                    obj.put(CANCELLED, true);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                Log.i("=============RESULT_CANCELED==============", ""+obj.toString());
                this.callbackContext.success(obj);
            } else {
                //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
                Log.i("=============Unexpected error==============", "Unexpected error");
                this.callbackContext.error("Unexpected error");
            }
        }
    }
    
    
    /**
     * check application's permissions
     */
   public boolean hasPermisssion() {
       for(String p : permissions)
       {
           if(!PermissionHelper.hasPermission(this, p))
           {
               return false;
           }
       }
       return true;
   }

    /**
     * We override this so that we can access the permissions variable, which no longer exists in
     * the parent class, since we can't initialize it reliably in the constructor!
     *
     * @param requestCode The code to get request action
     */
   public void requestPermissions(int requestCode)
   {
       PermissionHelper.requestPermissions(this, requestCode, permissions);
   }

   /**
   * processes the result of permission request
   *
   * @param requestCode The code to get request action
   * @param permissions The collection of permissions
   * @param grantResults The result of grant
   */
  public void onRequestPermissionResult(int requestCode, String[] permissions,
                                         int[] grantResults) throws JSONException
   {
       PluginResult result;
       for (int r : grantResults) {
           if (r == PackageManager.PERMISSION_DENIED) {
               Log.d(LOG_TAG, "Permission Denied!");
               result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
               this.callbackContext.sendPluginResult(result);
               return;
           }
       }

       switch(requestCode)
       {
           case 0:
               scan();
               break;
       }
   }

}
