package com.npdeas.b1k3labapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.npdeas.b1k3labapp.Npdeas.FileStruct;
import com.npdeas.b1k3labapp.Npdeas.NpDeasReader;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

/**
 * Created by NPDEAS on 07/05/2018.
 */

public class WebDb extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference databaseReference;
    private File file;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;
    private String userName;
    private String photoUrl;

    private static final String USERS_FILE = "users";
    private static final String ROUTE_FILE = "route";

    public WebDb(final Activity activity) {
        this.activity = activity;
        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            mFirebaseAuth.signInWithEmailAndPassword("normaluser@gmail.com", "Microalgas")
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                            } else {
                                Log.i("firebaseeeeeeeee ", task.getException().getMessage());
                            }
                        }
                    });
        } else {
            String userId = mFirebaseUser.getUid();
            NpDeasReader reader = new NpDeasReader();
            file = reader.getRootFile();
            File[] unsendedFiles = reader.getUnsendedFile();
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(USERS_FILE).child(userId);
            for (int i = 0; i < unsendedFiles.length; i++) {
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
            }

        }
    }


}
