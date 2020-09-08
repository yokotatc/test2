package com.example.testapplication;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;

public class DirectoryPathFinder {
    // 指定したdirectryNameの場所から画像ファイルのパスをリスト化してフルパスで取得する
    // ※取得するファイルの拡張子をしていする場合は引数extensuionsに設定(全ファイル取得の場合はnull指定)
    public static ArrayList<String> getDirectoryPathList(Activity activity, String directoryName){
        String fullPath = "";
        ArrayList<String> refDirectoryPathList = new ArrayList<>();
        File dir = activity.getExternalFilesDir(directoryName);
        File[] files = dir.listFiles();
        if( files == null ){
            return null;
        }

        for(int fileCnt = 0; fileCnt < files.length; fileCnt++){
            fullPath = String.valueOf(files[fileCnt]);

            if(!files[fileCnt].isDirectory()){
                // ファイルの為対象外
                continue;
            }

            // ディレクトリであればそのパスを登録する
            refDirectoryPathList.add(fullPath);
        }

        return refDirectoryPathList;
    }

    // 引数directoryPathに設定された最終階層のディレクトリ名を抽出する
    public static String getLastDirectoryName(String directoryPath){
        // ディレクトリ名取得
        String directoryName = "";
        int lastPosition = directoryPath.lastIndexOf("/");
        if (lastPosition != -1) {
            directoryName = directoryPath.substring(lastPosition + 1);
            return directoryName;
        }
        return "";
    }
}
