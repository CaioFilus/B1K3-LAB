package com.npdeas.b1k3labapp.UI.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.npdeas.b1k3labapp.Database.UserCRUD;
import com.npdeas.b1k3labapp.Database.UserTask;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Webserver.LoginDAO;
import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Webserver.WebserviceDAO;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * Created by NPDEAS on 8/24/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private CircularProgressButton buttonLogin;
    private Button buttonSignUp;
    private Button buttonSignIn;

    private TextView viewErrorUser;
    private TextView viewErrorPass;
    private TextView viewErrorEmail;

    private ProgressBar progressBar;

    private LinearLayout layoutEmail;
    private LinearLayout layoutName;
    private LinearLayout layoutAuth;

    private LoginDAO loginDAO;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        viewErrorUser = findViewById(R.id.errorViewUser);
        viewErrorPass = findViewById(R.id.errorViewPass);
        viewErrorEmail = findViewById(R.id.errorViewEmail);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutAuth = findViewById(R.id.layoutAuth);
        layoutName = findViewById(R.id.layoutName);
        progressBar = findViewById(R.id.progressBarAuth);

        user = new User();

        progressBar.setVisibility(View.INVISIBLE);
        buttonSignIn.setSelected(true);
        layoutName.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLogin.startAnimation();
                if (checkFields()) {
                    //User user = new User();
                    user.setName(editTextName.getText().toString());
                    user.setPassword(editTextPassword.getText().toString());
                    user.setEmail(editTextEmail.getText().toString());

                    progressBar.setVisibility(View.VISIBLE);
                    layoutAuth.setEnabled(false);
                    if (buttonSignIn.isSelected()) {
                        new LoginTask().execute();
                    } else {
                        new RegisterTask().execute();
                    }
                } else {
                    buttonLogin.revertAnimation();
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp.setSelected(true);
                buttonSignIn.setSelected(false);
                layoutName.setVisibility(View.VISIBLE);
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp.setSelected(false);
                buttonSignIn.setSelected(true);
                layoutName.setVisibility(View.GONE);
            }
        });
        loginDAO = new LoginDAO(this);
    }

    private boolean checkFields() {
        String error = "";
        viewErrorEmail.setVisibility(View.INVISIBLE);
        viewErrorPass.setVisibility(View.INVISIBLE);
        viewErrorUser.setVisibility(View.INVISIBLE);
        if ((editTextEmail.getText().length() != 0) && (editTextPassword.getText().length() != 0)) {
            if (editTextPassword.getText().length() >= 8) {
                if (editTextEmail.getText().toString().contains("@")) {
                    if ((buttonSignIn.isSelected())) {
                        return true;
                    } else if ((editTextName.getText().length() != 0) && (!buttonSignIn.isSelected())) {
                        if (editTextName.getText().length() >= 4) {
                            return true;
                        } else {
                            error = "*Nome invalido";
                            viewErrorUser.setVisibility(View.VISIBLE);
                            viewErrorUser.setText(error);
                        }
                    } else {
                        error = "Nome em branco";
                        viewErrorUser.setVisibility(View.VISIBLE);
                        viewErrorUser.setText(error);
                    }
                } else {
                    error = "*Email Invalido";
                    viewErrorEmail.setVisibility(View.VISIBLE);
                    viewErrorEmail.setText(error);
                }
            } else {
                error = "*Senha deve ter no minimo 8 carateres";
                viewErrorPass.setVisibility(View.VISIBLE);
                viewErrorPass.setText(error);
            }

        } else {
            error = "*Campo em branco";
            viewErrorUser.setVisibility(View.VISIBLE);
            viewErrorPass.setVisibility(View.VISIBLE);
            viewErrorUser.setText(error);
            viewErrorPass.setText(error);
        }
        return false;
    }

    private void saveOnDb(User user) {
        UserCRUD userCRUD = new UserCRUD(this);
        userCRUD.setUser(user);
        WebserviceDAO.getInstance(LoginActivity.this).auth(user);//autorizando
    }

    @Override
    public void onBackPressed() {

    }

    public class LoginTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... strings) {
            User user = new User();
            user.email = editTextEmail.getText().toString();
            user.setPassword(editTextPassword.getText().toString());
            return loginDAO.signIn(user);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Toast.makeText(LoginActivity.this, "logado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                UserTask userTask = new UserTask(LoginActivity.this);
                userTask.insertTask(user);
                User.setCurrentUser(user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Erro " + loginDAO.getErrorString(), Toast.LENGTH_SHORT).show();
            }
            buttonLogin.revertAnimation();
        }
    }

    public class RegisterTask extends AsyncTask<Void, Void, User> {


        @Override
        protected User doInBackground(Void... strings) {
            loginDAO = new LoginDAO(LoginActivity.this);
            User user = new User();
            user.email = editTextEmail.getText().toString();
            user.setPassword(editTextPassword.getText().toString());
            user.setName(editTextName.getText().toString());
            return loginDAO.signUp(user);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Toast.makeText(LoginActivity.this, "Registrado, favor logar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Erro " + loginDAO.getErrorString(), Toast.LENGTH_SHORT).show();
            }
            buttonLogin.revertAnimation();
        }
    }
}