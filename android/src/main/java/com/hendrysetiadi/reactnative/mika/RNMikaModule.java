
package com.hendrysetiadi.reactnative.mika;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.SparseArray;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RNMikaModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private final ReactApplicationContext reactContext;
    final SparseArray<Promise> mPromises;

    private String MIKA_PAYMENT_PACKAGE = "id.mikaapp.mika.charge";
    private String MIKA_VOID_PACKAGE = "id.mikaapp.mika.void";
    private String MIKA_PRINT_PACKAGE = "id.mikaapp.mika.print";
    private String MIKA_SETTLEMENT_PACKAGE = "id.mikaapp.mika.settlement";

    public RNMikaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mPromises = new SparseArray<>();
    }

    @Override
    public String getName() {
        return "RNMika";
    }


    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        HashMap<String, Object> constants = new HashMap<>();
        constants.put("OK", Activity.RESULT_OK);
        constants.put("CANCELED", Activity.RESULT_CANCELED);
        return constants;
    }


    @Override
    public void initialize() {
        super.initialize();
        getReactApplicationContext().addActivityEventListener(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        getReactApplicationContext().removeActivityEventListener(this);
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Promise promise = mPromises.get(requestCode);
        if (promise != null) {
            WritableMap result = new WritableNativeMap();
            result.putInt("resultCode", resultCode);
            if (data != null && data.getExtras() != null) {
                result.putMap("data", Arguments.fromBundle(data.getExtras()));
            }
            promise.resolve(result);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // Do Nothing
    }


    @ReactMethod
    public void resolveMikaActivity(Promise promise) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(MIKA_PAYMENT_PACKAGE);
        ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
        if (componentName == null) {
            promise.resolve(null);
            return;
        }

        WritableMap map = new WritableNativeMap();
        map.putString("class", componentName.getClassName());
        map.putString("package", componentName.getPackageName());
        promise.resolve(map);
    }


    /**
     * @param requestCode   Request Code: Random Number
     * @param username      MIKA Account Username
     * @param password      MIKA Account Password
     * @param acquirerId    MIKA Payment Channel Acquirer ID
     * @param amount        Amount to Charge
     * @param promise
     */
    @ReactMethod
    public void startMikaPayment(
        int requestCode,
        String username,
        String password,
        String acquirerId,
        int amount,
        Promise promise
    ) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(MIKA_PAYMENT_PACKAGE);
        intent.putExtra("USERNAME", username);
        intent.putExtra("PASSWORD", password);
        intent.putExtra("ACQUIRER_ID", acquirerId);
        intent.putExtra("AMOUNT", amount);

        activity.startActivityForResult(intent, requestCode);
        mPromises.put(requestCode, promise);
    }

    /**
     * @param requestCode   Request Code: Random Number
     * @param username      MIKA Account Username
     * @param password      MIKA Account Password
     * @param transactionId MIKA Payment Transaction ID
     * @param copyId        Receipt Copy ID: 1 = Customer Copy, 2 = Merchant Copy, and 3 = Bank Copy
     * @param promise
     */
    @ReactMethod
    public void startMikaPrint(
        int requestCode,
        String username,
        String password,
        String transactionId,
        int copyId,
        Promise promise
    ) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(MIKA_PRINT_PACKAGE);
        intent.putExtra("USERNAME", username);
        intent.putExtra("PASSWORD", password);
        intent.putExtra("TRANSACTION_ID", transactionId);
        intent.putExtra("COPY_ID", copyId);

        activity.startActivityForResult(intent, requestCode);
        mPromises.put(requestCode, promise);
    }

    /**
     * @param requestCode   Request Code: Random Number
     * @param username      MIKA Account Username
     * @param password      MIKA Account Password
     * @param promise
     */
    @ReactMethod
    public void startMikaSettlement(
        int requestCode,
        String username,
        String password,
        Promise promise
    ) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(MIKA_SETTLEMENT_PACKAGE);
        intent.putExtra("USERNAME", username);
        intent.putExtra("PASSWORD", password);

        activity.startActivityForResult(intent, requestCode);
        mPromises.put(requestCode, promise);
    }


    /* @ReactMethod
    public void startActivity(String action, ReadableMap data) {
        Activity activity = getReactApplicationContext().getCurrentActivity();
        Intent intent = new Intent(action);
        intent.putExtras(Arguments.toBundle(data));
        activity.startActivity(intent);
    }

    @ReactMethod
    public void startActivityForResult(int requestCode, String action, ReadableMap data, Promise promise) {
        Activity activity = getReactApplicationContext().getCurrentActivity();
        Intent intent = new Intent(action);
        intent.putExtras(Arguments.toBundle(data));
        activity.startActivityForResult(intent, requestCode);
        mPromises.put(requestCode, promise);
    }

    @ReactMethod
    public void finish(int result, String action, ReadableMap map) {
        Activity activity = getReactApplicationContext().getCurrentActivity();
        Intent intent = new Intent(action);
        intent.putExtras(Arguments.toBundle(map));
        activity.setResult(result, intent);
        activity.finish();
    } */
}