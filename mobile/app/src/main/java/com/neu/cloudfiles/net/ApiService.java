package com.neu.cloudfiles.net;

import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.DeleteFileDo;
import com.neu.cloudfiles.bean.RegisterDo;
import com.neu.cloudfiles.bean.SaveShareFileDo;
import com.neu.cloudfiles.bean.ShareFileDo;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;


public interface ApiService {
    @GET("/user/articles")
    Observable<DataResponse<Map<String, Object>>> getHomeArticles();

    @GET("/user/login")
    Observable<DataResponse<Map<String, UserLoginVo>>> login(@Query("username") String username, @Query("password") String password);

    @GET("/user/check")
    Observable<DataResponse<Map<String, UserVo>>> checkLogin();

    @GET("/file/listFiles")
    Observable<DataResponse<Map<String, Object>>> getUserFiles(@QueryMap Map<String, Object> fileDo);

    @Streaming
    @GET("/file/download")
    Call<ResponseBody> downloadFile(@QueryMap Map<String, Object> downloadFileQuery);

    @Multipart
    @POST("/file/upload")
    Observable<DataResponse<Map<String, Object>>> uploadFile(@PartMap Map<String, RequestBody> stringParam,
                                                             @PartMap Map<String, Long> longParam,
                                                             @PartMap Map<String, Integer> integerMap,
                                                             @Part MultipartBody.Part part);

    @POST("/file/delete")
    Observable<DataResponse<Map<String, Object>>> deleteFile(@Body DeleteFileDo fileDo);

    @POST("/share/shareFile")
    Observable<DataResponse<Map<String, Object>>> shareFile(@Body ShareFileDo fileDo);

    @GET("/share/shareLinks")
    Observable<DataResponse<Map<String, Object>>> getFileShares(@Query("userFileId") long userFileId);

    @GET("/share/shareFileList")
    Observable<DataResponse<Map<String, Object>>> getShareContent(@QueryMap Map<String, String> shareFileListDo);

    @POST("/share/save")
    Observable<DataResponse<Map<String, Object>>> saveShareFile(@Body SaveShareFileDo fileDo);

    @GET("/share/allShares")
    Observable<DataResponse<Map<String, Object>>> getAllShares();

    @GET("/share/banners")
    Observable<DataResponse<Map<String, Object>>> getBanners();

    @POST("/user/create")
    Observable<DataResponse<Map<String, Object>>> register(@Body RegisterDo registerDo);
}

