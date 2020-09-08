package com.example.testapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class PermissionChecker {
    // 定数宣言
    private static final int PERMISSION_AT_RUNTIME_MIN_API = Build.VERSION_CODES.M;       // 権限確認が必要な最低APIレベル(23)

    // 権限チェックが必要かインストール端末のバージョンを確認する
    private static boolean isNeedCheckAPILevel(){
        // インストール端末がAPIレベル23(Android6)以上なら権限チェックをする
        if(Build.VERSION.SDK_INT >= PERMISSION_AT_RUNTIME_MIN_API){
            // 権限チェックが必要
            return true;
        }else{
            // 権限チェックが不要
            return false;
        }
    }

    // 対象の権限が許可されているか確認する
    private static boolean checkPermission(AppCompatActivity activity, java.lang.String permissionName){
        if(ContextCompat.checkSelfPermission(activity,
                permissionName)
                == PackageManager.PERMISSION_GRANTED){

            // 許可している
            return true;
        }else{
            // 拒否している
            return false;
        }
    }

    // 対象の権限設定が必要か確認する
    // true ：権限設定が必要
    // false：   //  が不要
    public static boolean isNeedSettingPermission(AppCompatActivity activity, java.lang.String permissionName){
        if( isNeedCheckAPILevel() ){
            return !checkPermission(activity, permissionName);
        }else{
            return false;
        }
    }

    public static boolean checkGrantResults(@NonNull int... grantResults) {
        if (grantResults.length == 0) throw new IllegalArgumentException("grantResults is empty");
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return true;
    }

    // ダイアログ表示
    public static void showAlertDialog(FragmentManager fragmentManager, String permission) {
        RuntimePermissionAlertDialogFragment dialog = RuntimePermissionAlertDialogFragment.newInstance(permission);
        dialog.show(fragmentManager, RuntimePermissionAlertDialogFragment.TAG);
    }

    // ダイアログ本体
    public static class RuntimePermissionAlertDialogFragment extends DialogFragment {
        public static final String TAG = "RuntimePermissionApplicationSettingsDialogFragment";
        private static final String ARG_PERMISSION_NAME = "permissionName";

        public static RuntimePermissionAlertDialogFragment newInstance(@NonNull String permission) {
            RuntimePermissionAlertDialogFragment fragment = new RuntimePermissionAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PERMISSION_NAME, permission);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String permission = getArguments().getString(ARG_PERMISSION_NAME);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(permission + " の権限がないので、アプリ情報の「許可」から設定してください")
                    .setPositiveButton("アプリ情報", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                            // システムのアプリ設定画面
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });
            return dialogBuilder.create();
        }
    }
}
