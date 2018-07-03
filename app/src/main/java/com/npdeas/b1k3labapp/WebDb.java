package com.npdeas.b1k3labapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NPDEAS on 07/05/2018.
 */

public class WebDb {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference databaseReference;
    private File file;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;

    private static final String USERS_FILE = "users";
    private static final String ROUTE_FILE = "route";
    private static final int OK = 0;

    public WebDb(final Activity activity) {
        this.activity = activity;
        FirebaseApp.initializeApp(activity);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //activity.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder(})
          //      .build(),OK);
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
            activity.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    0);

            /*mFirebaseAuth.signInWithEmailAndPassword("normaluser@gmail.com", "Microalgas")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                            } else {
                                Log.e("firebaseeeeeeeee ", task.getException().getMessage());
                            }
                        }
                    });*/
        } else {

            AuthUI.getInstance().signOut(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.i("deu", "piaaa vc nao acredita  no que rolou");

                    }else{
                        Log.i("nope", "piaaa vc nao acredita  no não que rolou");
                    }
                }
            });
            /*String userId = mFirebaseUser.getUid();
            NpDeasReader reader = new NpDeasReader();
            file = reader.getRootFile();
            File[] unsendedFiles = reader.getUnsendedFile();
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(USERS_FILE).child(userId);
            for (int i = 0; i < unsendedFiles.length; i++) {
                try {
                    File routeFile = reader.getTxtFileFromFolder(unsendedFiles[i]);
                    reader.setRouteFile(routeFile);
                    FileStruct aux;
                    int j = 0;
                    while ((aux = reader.getFileStruct()) != null) {
                        databaseReference
                                .child(unsendedFiles[i].getName().replace('.', '\\'))
                                .child(ROUTE_FILE)
                                .child(String.valueOf(j))
                                .setValue(aux);
                        j++;
                    }
                    reader.setSeverDbSended(routeFile);

                } catch (Exception e) {
                    Log.e("Database", e.getMessage());
                }
            }*/
        }
    }
}
