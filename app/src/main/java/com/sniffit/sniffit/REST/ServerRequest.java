package com.sniffit.sniffit.REST;

        import android.util.Log;

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
        Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password);

        @FormUrlEncoded
        @POST("api/signup")
        Call<ResponseBody> registerUser(@Field("email") String email, @Field("password") String password);

        //RFID API calls
        @GET("api/user/{user_id}/rfidtags")
        Call<ResponseBody> getRFIDTags(@Header("x-access-token") String token, @Path("user_id") String userId);

        @GET("api/user/{user_id}/rfidtags/{rfid_tag_id}")
        Call<ResponseBody> getRFIDTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("rfid_tag_id") String tagId);

        @FormUrlEncoded
        @POST("api/user/{user_id}/rfidtags")
        Call<ResponseBody> postTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Field("tagId") String tagId, @Field("name") String name);

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
        @PUT("api/user/{user_id}/rooms/{room_id}")
        Call<ResponseBody> putRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_id") String oldRoom, @Field("name") String name, @Field("length") String length,  @Field("width") String width);


        @DELETE("api/user/{user_id}/rooms/{room_id}")
        Call<ResponseBody> deleteRoom(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_id") String roomId);

        //Snapdragon API calls
        @GET("api/user/{user_id}/snapdragons/rooms/{room_id}")
        Call<ResponseBody> getSnapdragons(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_id") String roomId);

        @GET("api/user/{user_id}/snapdragons/{snapdragon_ip}")
        Call<ResponseBody> getSnapdragon(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("snapdragon_ip") String snapdragonIp);

        @FormUrlEncoded
        @POST("api/user/{user_id}/snapdragons")
        Call<ResponseBody> postSnapdragon(@Header("x-access-token") String token, @Path("user_id") String userId, @Field("name") String name, @Field("roomId") String roomId,  @Field("ipAddress") String ipAddress, @Field("xCoord") String xCoord, @Field("yCoord") String yCoord);

        @FormUrlEncoded
        @PUT("api/user/{user_id}/snapdragons/{snapdragon_ip}")
        Call<ResponseBody> putSnapdragon(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("snapdragon_ip") String snapdragonIp, @Field("name") String name, @Field("roomId") String roomId,  @Field("ipAddress") String ipAddress);

        @DELETE("api/user/{user_id}/snapdragons/{snapdragon_ip}")
        Call<ResponseBody> deleteSnapdragon(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("snapdragon_ip") String snapdragonIp);

        //Reference Tag API calls
        @GET("api/user/{user_id}/reftags/rooms/{room_id}")
        Call<ResponseBody> getReferenceTags(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("room_id") String roomId);

        @GET("api/user/{user_id}/reftags/{ref_tagId}")
        Call<ResponseBody> getReferenceTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("ref_tagId") String tagId);

        @FormUrlEncoded
        @POST("api/user/{user_id}/reftags/")
        Call<ResponseBody> postReferenceTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Field("name") String name, @Field("roomId") String roomId, @Field("tagId") String tagId, @Field("xCoord") String xCoord, @Field("yCoord") String yCoord);

        @FormUrlEncoded
        @PUT("api/user/{user_id}/reftags/{ref_tagId}")
        Call<ResponseBody> putReferenceTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("ref_tagId") String oldTagId, @Field("tagId") String newTagId, @Field("roomId") String roomId, @Field("x_position") String xPosition, @Field("y_position") String yPosition);

        @DELETE("api/user/{user_id}/reftags/{ref_tagId}")
        Call<ResponseBody> deleteReferenceTag(@Header("x-access-token") String token, @Path("user_id") String userId, @Path("ref_tagId") String tagId);


    }

    public void authenticate(String request, String email, String password, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);
        switch (request.toLowerCase()) {
            case "login":
                Call<ResponseBody> call = loginService.loginUser(email, password);
                call.enqueue(callback);
                break;
            case "register":
                Call<ResponseBody> registerCall = loginService.registerUser(email, password);
                registerCall.enqueue(callback);
                break;
        }
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
    API helper function for GET requests associated with a Room, which includes Snapdragons and Reference Tags
 */
    public void getRoomIds(String request, User user, String roomId, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface apiService = retrofit.create(apiInterface.class);

        switch (request.toLowerCase()){

            case "snapdragon":
                Call<ResponseBody> getSnapdragonCall = apiService.getSnapdragons(user.getToken(), user.getUserId(), roomId);
                getSnapdragonCall.enqueue(callback);
                break;

            case "reference":
                Call<ResponseBody> getReferenceCall = apiService.getReferenceTags(user.getToken(), user.getUserId(), roomId);
                getReferenceCall.enqueue(callback);
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

            case "snapdragon":
                Call<ResponseBody> getSnapdragonCall = apiService.getSnapdragon(user.getToken(), user.getUserId(), id);
                getSnapdragonCall.enqueue(callback);
                break;

            case "reference":
                Call<ResponseBody> getReferenceCall = apiService.getSnapdragon(user.getToken(), user.getUserId(), id);
                getReferenceCall.enqueue(callback);
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

            case "snapdragon":
                Call<ResponseBody> snapdragonCall = apiService.deleteSnapdragon(user.getToken(), user.getUserId(), id);
                snapdragonCall.enqueue(callback);
                break;

            case "reference":
                Call<ResponseBody> referenceCall = apiService.deleteSnapdragon(user.getToken(), user.getUserId(), id);
                referenceCall.enqueue(callback);
                break;
        }
    }

    public void postRFIDTag(User user, String myTagId, String myName, retrofit.Callback<ResponseBody> callback){
        String tagId = myTagId;
        String name = myName;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.postTag(user.getToken(), user.getUserId(), tagId, name);
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

    public void postRoom(User user, String roomName, String roomWidth, String roomLength, retrofit.Callback<ResponseBody> callback){
        String width = roomWidth;
        String length = roomLength;
        String name = roomName;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.postRoom(user.getToken(), user.getUserId(), name, length, width);
        rfidCall.enqueue(callback);
    }

    public void putRoom(User user, String roomName, String roomWidth, String roomLength,
                        String roomOldName, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String width = roomWidth;
        String length = roomLength;
        String name = roomName;
        String oldName = roomOldName;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> rfidCall = loginService.putRoom(user.getToken(), user.getUserId(), oldName, name, length, width);
        rfidCall.enqueue(callback);
    }

    public void postSnapdragon(User user, String name, String roomId, String ipAddress, String xCoord, String yCoord, retrofit.Callback<ResponseBody> callback){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> snapdragonCall = loginService.postSnapdragon(user.getToken(), user.getUserId(), name, roomId, ipAddress, xCoord, yCoord);
        snapdragonCall.enqueue(callback);
    }

    public void putSnapdragon(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String oldName = "23423";
        String name = "and";
        String roomId = "4321";
        String ipAddress = "123123";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> snapdragonCall = loginService.putSnapdragon(user.getToken(), user.getUserId(), oldName, name, roomId, ipAddress);
        snapdragonCall.enqueue(callback);
    }

    public void postReferenceTag(User user, String tagName, String roomId, String tagId, String xCoord, String yCoord, retrofit.Callback<ResponseBody> callback){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> snapdragonCall = loginService.postReferenceTag(user.getToken(), user.getUserId(), tagName, roomId, tagId, xCoord, yCoord);
        snapdragonCall.enqueue(callback);
    }

    public void putReferenceTag(User user, retrofit.Callback<ResponseBody> callback){
        //Hard coded
        String oldTagId = "23423";
        String tagId = "and";
        String roomId = "12313";
        String xPosition = "4321";
        String yPosition = "123123";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> snapdragonCall = loginService.putReferenceTag(user.getToken(), user.getUserId(), oldTagId, tagId, roomId, xPosition, yPosition);
        snapdragonCall.enqueue(callback);
    }
    //////////////////   DELETES /////////////////

    public void deleteRoom(User user, String roomId, retrofit.Callback<ResponseBody> callback) {
        String id = roomId;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> roomCall = loginService.deleteRoom(user.getToken(), user.getUserId(), id);
        roomCall.enqueue(callback);

    }

    public void deleteSnapdragon(User user, String snapIp, retrofit.Callback<ResponseBody> callback) {
        String ip = snapIp;
        Log.d("hi", ip);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> snapCall = loginService.deleteSnapdragon(user.getToken(), user.getUserId(), ip);
        snapCall.enqueue(callback);
    }

    public void deleteRefTag(User user, String refId, retrofit.Callback<ResponseBody> callback) {       //CHECK API CALL HERE: MAY BE WRONG
        String id = refId;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> refTagCall = loginService.deleteReferenceTag(user.getToken(), user.getUserId(), id);
        refTagCall.enqueue(callback);
    }

    public void deleteItem(User user, String itemId, retrofit.Callback<ResponseBody> callback) {       //CHECK API CALL HERE: MAY BE WRONG
        String id = itemId;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();
        apiInterface loginService = retrofit.create(apiInterface.class);

        Call<ResponseBody> refTagCall = loginService.deleteTag(user.getToken(), user.getUserId(), id);
        refTagCall.enqueue(callback);
    }


}





