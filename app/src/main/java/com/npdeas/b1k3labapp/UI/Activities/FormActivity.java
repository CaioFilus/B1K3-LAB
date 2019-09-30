package com.npdeas.b1k3labapp.UI.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.npdeas.b1k3labapp.Database.Enuns.Deficiency;
import com.npdeas.b1k3labapp.Database.Enuns.Gender;
import com.npdeas.b1k3labapp.Database.Enuns.Scholarity;
import com.npdeas.b1k3labapp.Database.FormCRUD;
import com.npdeas.b1k3labapp.Database.Structs.UserForm;
import com.npdeas.b1k3labapp.R;

public class FormActivity extends AppCompatActivity {

    private LinearLayout layoutWhatDefcy;

    private Spinner spinnerGender;
    private Spinner spinnerScholarity;
    private Spinner spinnerWhatDfcy;

    private EditText editTextAge;
    private EditText editTextIncome;
    private EditText editTextState;
    private EditText editTextCity;
    private EditText editTextDistrict;
    private Switch switchDeficiency;

    private Button buttonConfirmForm;

    private FormCRUD formCRUD;

    private ArrayAdapter<CharSequence> adapterGender;
    private ArrayAdapter<CharSequence> adapterScholarity;
    private ArrayAdapter<CharSequence> adapterWhatDefcy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        formCRUD = new FormCRUD(this);

        layoutWhatDefcy = findViewById(R.id.layoutWhatDefcy);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerScholarity = findViewById(R.id.spinnerScholarity);
        spinnerWhatDfcy = findViewById(R.id.spinnerWhatDefcy);

        editTextAge = findViewById(R.id.editTextAge);
        editTextIncome = findViewById(R.id.editTextIncome);
        editTextState = findViewById(R.id.editTextState);
        editTextCity = findViewById(R.id.editTextCity);
        editTextDistrict = findViewById(R.id.editTextDistrict);

        switchDeficiency = findViewById(R.id.switchDeficiency);
        buttonConfirmForm = findViewById(R.id.buttonConfirmForm);

        adapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapterScholarity = ArrayAdapter.createFromResource(this,
                R.array.scholarity, android.R.layout.simple_spinner_item);
        adapterWhatDefcy = ArrayAdapter.createFromResource(this,
                R.array.whatDefcy, android.R.layout.simple_spinner_item);

        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterScholarity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWhatDefcy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGender.setAdapter(adapterGender);
        spinnerScholarity.setAdapter(adapterScholarity);
        spinnerWhatDfcy.setAdapter(adapterWhatDefcy);

        switchDeficiency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibleState = isChecked ? View.VISIBLE : View.GONE;
                layoutWhatDefcy.setVisibility(visibleState);
            }
        });

        buttonConfirmForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
                builder.setTitle("Há valores não preenchidos");

                Gender gender = Gender.getValue((int)spinnerGender.getSelectedItemId());
                Scholarity scholarity = Scholarity.getValue((int)spinnerScholarity.getSelectedItemId());
                Deficiency whatDefcy = null;
                if(layoutWhatDefcy.getVisibility() == View.VISIBLE){
                    whatDefcy = Deficiency.getValue((int)spinnerWhatDfcy.getSelectedItemId());
                }
                int age;
                int income;
                try {
                    age = Integer.parseInt(editTextAge.getText().toString());
                    income = Integer.parseInt(editTextIncome.getText().toString());
                }catch (NumberFormatException e){
                    AlertDialog alertDialog = builder.show();
                    alertDialog.show();
                    return;
                }
                String state = editTextState.getText().toString();
                String city = editTextCity.getText().toString();
                String districty = editTextDistrict.getText().toString();
                if(state.length() != 0 && city.length() != 0 && districty.length() != 0 ){
                    UserForm userForm = new UserForm();
                    userForm.setGender(gender);
                    userForm.setAge(age);
                    userForm.setScholarity(scholarity);
                    userForm.setIncome(income);
                    userForm.setState(state);
                    userForm.setCity(city);
                    userForm.setDistrict(districty);
                    userForm.setWhatDefcy(whatDefcy);
                    formCRUD.setUserForm(userForm);
                    Toast.makeText(FormActivity.this,"Formulario Salvo", Toast.LENGTH_SHORT);
                    finish();
                }else{
                    AlertDialog alertDialog = builder.show();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        formCRUD.close();
    }
}
