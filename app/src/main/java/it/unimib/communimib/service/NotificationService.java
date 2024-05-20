package it.unimib.communimib.service;

import static it.unimib.communimib.util.Constants.CHANNEL_ID;
import static it.unimib.communimib.util.Constants.DATABASE;
import static it.unimib.communimib.util.Constants.TOKEN_PATH;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.R;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.ITokenRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.main.MainActivity;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.ServiceLocator;


public class NotificationService extends FirebaseMessagingService {

    private User user;
    private final ITokenRepository tokenRepository;
    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
        this.tokenRepository = ServiceLocator.getInstance().getTokenRepository();
    }

    public static void sendNotifications(String messageBody, List<Token> tokenList, User user) {
/*
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Gestisci la ricezione della notifica qui
        // Esempio: visualizza la notifica

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Nuova segnalazione")
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

*/
        /*
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(token)
                .build();
*/
/*
        Map<String, String> payload = new HashMap<>();
        payload.put("title", "Nuova segnalazione");
        payload.put("body", messageBody);

        for (Token token: tokenList) {
            if(!token.getUserId().equals(user.getUid())){
                FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token.getToken())
                        .setData(payload)
                        .build());
            }
        }

 */
        // Invia la notifica utilizzando Firebase Cloud Messaging

/*
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        createNotificationChannel();

        notificationManager.notify(0, builder.build());

*/
    }

    public void sendNotification(String messageBody, List<Token> tokenList, User user) {
        for (Token token : tokenList) {
            if(!token.getUserId().equals(user.getUid())){
                send(token.getToken(), messageBody);
            }
        }
    }

    private void send(String token, String body) {
        JSONObject payload = new JSONObject();
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", "Nuova segnalazione");
            notification.put("body", body);
            payload.put("to", token);
            payload.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(
                    "https://fcm.googleapis.com/fcm/send",
                    payload,
                    response -> {
                        Log.d("request", "successo");
                    },
                    error -> {
                        Log.d("request", "errore");
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    //headers.put("Authorization", "key=YOUR_SERVER_KEY");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private void handleNow() {
        //Log.d(TAG, "Short lived task is done.");
    }
    public static void getTokenFromFirebaseMessaging(Callback callback){
        //this.user = user;
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            callback.onComplete(new Result.TokenValueSuccess(task.getResult()));
                        }
                        //todo aggiungere errore

                    }
                });
    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        //Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.

        Token tokenObj = new Token(token, user.getUid());
        tokenRepository.sendRegistrationToServer(tokenObj, result -> {});

        //sendRegistrationToServer(token);
    }



    /*public static class MyWorker extends CoroutineScheduler.Worker {

        public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            // TODO(developer): add long running task here.
            return Result.success();
        }
    }*/
}
