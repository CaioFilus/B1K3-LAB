package com.npdeas.b1k3labapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.npdeas.b1k3labapp.Route.Npdeas.RouteNode;
import com.npdeas.b1k3labapp.Route.Route;
import com.npdeas.b1k3labapp.Route.RouteUtils;

import java.io.File;
import java.util.ArrayList;
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

    private static final String USERS_TABLE = "users";
    private static final String ROUTE_TABLE = "routeInfo";
    public static final int REQUEST_WEB_DB_OK = 100;

    public WebDb(final Activity activity) {
        this.activity = activity;
        FirebaseApp.initializeApp(activity);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //activity.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder(})
        //      .build(),OK);
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {

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
            sendRoutes();
        }
    }

    public void sendRoutes(){
        /*AuthUI.getInstance().signOut(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("deu", "piaaa vc nao acredita  no que rolou");

                } else {
                    Log.i("nope", "piaaa vc nao acredita  no não que rolou");
                }
            }
        });*/
        String userId = mFirebaseUser.getUid();
        final ArrayList<Route> routes = RouteUtils.getUnsendedRoutes();

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(USERS_TABLE).child(userId);

        for (Route route : routes) {
            try {
                ArrayList<RouteNode> routeNodes = route.getRoute();
                int i = 0;
                for (RouteNode routeNode : routeNodes) {
                    databaseReference
                            .child(route.getDataTime().replace('.', '\\'))
                            .child(ROUTE_TABLE)
                            .child(String.valueOf(i))
                            .setValue(routeNode);
                    i++;
                }
                RouteUtils.setSendedRoutes(routes);

            } catch (Exception e) {
                Log.e("Database", e.getMessage());
            }
        }
    }

}
