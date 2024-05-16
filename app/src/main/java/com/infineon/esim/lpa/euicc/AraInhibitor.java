package com.infineon.esim.lpa.euicc;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AraInhibitor implements IXposedHookLoadPackage {
    private static final String LOG_TAG = "AraInhibitor";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d(LOG_TAG, "loadPackage..");
        try{
            XposedHelpers.findAndHookMethod("org.simalliance.openmobileapi.service.security.AccessControlEnforcer",
                                            lpparam.classLoader, "readSecurityProfile", new XC_MethodReplacement() {
               @Override
               protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                   XposedHelpers.setBooleanField(param.thisObject, "mUseArf", false);
                   XposedHelpers.setBooleanField(param.thisObject, "mUseAra", false);
                   XposedHelpers.setBooleanField(param.thisObject, "mFullAccess", true);
                   Log.d(LOG_TAG, "readSecurityProfile hooked..");
                   return null;
               }
           });
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Failed to hook readSecurityProfile..", t);
        }
    }
}
