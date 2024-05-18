package it.unimib.communimib.datasource.token;

import static it.unimib.communimib.util.Constants.TOKEN_PATH;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.Token;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class TokenRemoteDataSource implements ITokenRemoteDataSource{

    private final DatabaseReference databaseReference;

    public TokenRemoteDataSource() {
        this.databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    public void getAllToken(Callback addedCallback,
                            Callback changedCallback,
                            Callback removedCallback,
                            Callback cancelledCallback){
        databaseReference
                .child(Constants.TOKEN_PATH)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Token token = snapshot.getValue(Token.class);
                        token.setTid(snapshot.getKey());
                        addedCallback.onComplete(new Result.TokenSuccess(token));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Token token = snapshot.getValue(Token.class);
                        token.setTid(snapshot.getKey());
                        changedCallback.onComplete(new Result.TokenSuccess(token));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Token token = snapshot.getValue(Token.class);
                        token.setTid(snapshot.getKey());
                        removedCallback.onComplete(new Result.TokenSuccess(token));
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(ErrorMapper.TOKEN_RECOVERING_ERROR));
                    }
                });
    }

    public void sendRegistrationToServer(Token token, Callback callback) {

        String key = databaseReference.child(Constants.TOKEN_PATH).push().getKey();

        databaseReference.
                child(TOKEN_PATH).
                child(key).
                setValue(token).
                addOnCompleteListener(result -> {
                    if(result.isSuccessful()){
                        callback.onComplete(new Result.TokenSuccess(token));
                    } else {
                        callback.onComplete(new Result.Error(ErrorMapper.TOKEN_INSERT_ERROR));
                    }
                });

    }
}
