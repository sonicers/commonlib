package com.sonicers.commonlib.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UploadUtil {
    private static final String TAG = "UploadUtil";

    private UploadUtil uploadUtil;

    public static void upLoadFile(String url, final OnUploadListener listener, List<File> fileList) {

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

    public static ResponseBody upload(String url, String filePath, String fileName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body();
    }
}
