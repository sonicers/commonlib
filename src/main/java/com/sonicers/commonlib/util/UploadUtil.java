package com.sonicers.commonlib.util;

import android.util.Log;

import com.sonicers.commonlib.singleton.OkHttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadUtil {
    private static final String TAG = "UploadUtil";

    private UploadUtil uploadUtil;

    /**
     * post上传文件
     *
     * @param url
     * @param params
     * @param fileList
     * @return
     */
    public void upLoadFile(String url, Map<String, String> params, final OnUploadListener listener, List<File> fileList, Map<String, String> header) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        //加参数
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        //加文件
        for (File file : fileList) {
            if (file.exists()) {
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            }
        }
        RequestBody body = builder.build();
        Request.Builder rBuilder = new Request.Builder();
        rBuilder.url(url);
        rBuilder.post(body);
        if (header != null) {
            for (String key : header.keySet()) {
                rBuilder.addHeader(key, header.get(key));
            }
        }
        final Request request = rBuilder.build();

        OkHttpClient mOkHttpClient = OkHttpUtil.getInstance();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
                if (listener != null) {
                    listener.onUploadFailed(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: 上传成功");
                String content = request.body().toString();
                if (listener != null) {
                    listener.onUploadSuccess(content);
                }
            }
        });
    }

    public void upLoadFile(String url, final OnUploadListener listener, List<File> fileList) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);


        //加文件

        RequestBody body = builder.build();
        Request.Builder rBuilder = new Request.Builder();
        rBuilder.url(url);
        rBuilder.post(body);

        final Request request = rBuilder.build();

        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onUploadFailed(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = request.body().toString();
                if (listener != null) {
                    listener.onUploadSuccess(content);
                }
            }
        });
    }

    public interface OnUploadListener {

        void onUploadSuccess(String content);

        void onUploadFailed(IOException e);
    }
}
