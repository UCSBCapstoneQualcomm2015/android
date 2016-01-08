package com.sniffit.sniffit.REST;

        import java.io.InputStream;
        import java.util.HashMap;
        import java.util.Map;

        import org.json.JSONObject;

        import com.squareup.okhttp.Request;
        import com.squareup.okhttp.Response;
        import com.squareup.okhttp.ResponseBody;

        import retrofit.Call;
        import retrofit.Retrofit;
        import retrofit.http.Body;
        import retrofit.http.DELETE;
        import retrofit.http.Field;
        import retrofit.http.FormUrlEncoded;
        import retrofit.http.GET;
        import retrofit.http.Header;
        import retrofit.http.Headers;
        import retrofit.http.POST;
        import retrofit.http.PUT;
        import retrofit.http.Path;


/**
 * Created by andrewpang on 11/2/15.
 */
public class ServerRequest {

//    private static final String base_url = "http://192.168.57.1:8080/";
 private static final String base_url = "http://10.0.3.2:8080/";

    //"http://ec2-52-27-212-208.us-west-2.compute.amazonaws.com/";

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Request request;
    Response response;
    String jsonData = "";

    public ServerRequest() {
    }

    public interface apiInterface{
        //Authentication
        @FormUrlEncoded
        @POST("api/login")
        Call<ResponseBody> loginUser(@Field("_csrf") String csrf, @Field("email") String email, @Field("password") String password);

        //RFID API calls
        @GET("api/user/{user_id}/rfidtags")
        Call<ResponseBody> getRFIDTags(@Header("x-access-token") String token, @Path("user_id") String userId);

        @GET("api/user/{user_id}/rfidtags/{rfid_tag_id}")
        Call<ResponseBody> getRFIDTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("rfid_tag_id") String tagId);

        @FormUrlEncoded
        @POST("api/user/{user_id}/rfidtags")
        Call<ResponseBody> postTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Field("_csrf") String csrf, @Field("tagId") String tagId, @Field("name") String name);

        @FormUrlEncoded
        @PUT("api/user/{user_id}/rfidtags/{rfid_tag_id}")
        Call<ResponseBody> putTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("rfid_tag_id") String oldTagId, @Field("tagId") String newTagId, @Field("name") String name);

        @DELETE("api/user/{user_id}/rfidtags/{rfid_tag_id}")
        Call<ResponseBody> deleteTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("rfid_tag_id") String tagId);

        //Rooms API calls
        @GET("api/user/{user_id}/rooms")
        Call<ResponseBody> getRooms(@Header("x-access-token") String token, @Path("user_id") String userId);

        @GET("api/user/{user_id}/rooms/{room_name}")
        Call<ResponseBody> getRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_name") String roomName);

        @FormUrlEncoded
        @POST("api/user/{user_id}/rooms")
        Call<ResponseBody> postRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Field("name") String name, @Field("length") String length,  @Field("width") String width);

        @FormUrlEncoded
        @PUT("api/user/{user_id}/rooms/{room_name}")
        Call<ResponseBody> putRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_name") String oldRoom, @Field("name") String name, @Field("length") String length,  @Field("width") String width);

        @FormUrlEncoded
        @DELETE("api/user/{user_id}/rooms/{room_name}")
        Call<ResponseBody> deleteRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_name") String roomName);

    }

    public void authenticate(String csrf, String email, String password, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);
        Call<ResponseBody> call = loginService.loginUser(csrf, email, password);
        call.enqueue(callback);
    }

    /*
        API helper function for general GET requests, with a "request" parameter that chooses which api call to make
     */
    public void getIds(String request, User user, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface apiService = retrofit.create(apiInterface.class);

        switch (request.toLowerCase()){
            case "rfid":
                Call<ResponseBody> getRFIDCall = apiService.getRFIDTags(user.getToken(), user.getUserId());
                getRFIDCall.enqueue(callback);
                break;

            case "rooms":
                Call<ResponseBody> getRoomCall = apiService.getRooms(user.getToken(), user.getUserId());
                getRoomCall.enqueue(callback);
                break;

        }
    }

    /*
        API helper function for a specific id's GET request, with a "request" parameter that chooses which api call to make
     */
    public void getId(String request, String id, User user, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface apiService = retrofit.create(apiInterface.class);

        switch (request.toLowerCase()){
            case "rfid":
                Call<ResponseBody> call = apiService.getRFIDTag(user.getToken(), user.getUserId(), id);
                call.enqueue(callback);
                break;

            case "rooms":
                Call<ResponseBody> getRoomCall = apiService.getRoom(user.getToken(), user.getUserId(), id);
                getRoomCall.enqueue(callback);
                break;

        }

    }

    /*
        API helper function for DELETE requests, with a "request" parameter that chooses which api call to make
     */
    public void deleteId(String request, String id, User user, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface apiService = retrofit.create(apiInterface.class);

        switch (request.toLowerCase()) {
            case "rfid":
                Call<ResponseBody> call = apiService.deleteTag(user.getToken(), user.getUserId(), id);
                call.enqueue(callback);
                break;

            case "rooms":
                Call<ResponseBody> roomCall = apiService.deleteRoom(user.getToken(), user.getUserId(), id);
                roomCall.enqueue(callback);
                break;
        }
    }

    public void postRFIDTag(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String csrf = "";
        String tagId = "1123414";
        String name = "and";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.postTag(user.getToken(), user.getUserId(), csrf, tagId, name);
        rfidCall.enqueue(callback);

    }

    public void putRFIDTag(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String tagId = "564eb237b6b88d075c73f68d";
        String newId = "lalalal";
        String name = "and";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.putTag(user.getToken(), user.getUserId(), tagId, newId, name);
        rfidCall.enqueue(callback);

    }

    public void postRoom(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String width = "123";
        String length = "1124";
        String name = "and";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.postRoom(user.getToken(), user.getUserId(), name, length, width);
        rfidCall.enqueue(callback);
    }

    public void putRoom(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String width = "123";
        String length = "1124";
        String name = "and";
        String oldName = "alkdsf";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.putRoom(user.getToken(), user.getUserId(), oldName, name, length, width);
        rfidCall.enqueue(callback);

    }



}





