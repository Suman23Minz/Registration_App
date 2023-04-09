package com.example.myfirebaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
//    defining the edit text variables

    private EditText fullName , email , dateOfBirth , mobile , password , confirmPassword ;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Regester");
        Toast.makeText(RegisterActivity.this,"You can login now ",Toast.LENGTH_SHORT).show();

//        finding the view by the ids ( box pannel )
        fullName = findViewById(R.id.et_enter_name);
        email = findViewById(R.id.et_enter_email);
        dateOfBirth = findViewById(R.id.et_enter_dob);
        mobile = findViewById(R.id.et_enter_mobile);
        password=findViewById(R.id.et_enter_password);
        confirmPassword=findViewById(R.id.et_enter_repassword);
        progressBar = findViewById(R.id.progressbar);


//        finding the view for the radio button
       radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
       radioGroupRegisterGender.clearCheck(); // these keeps the button unchecked if the activity started or paused

        Button  buttonRegister = findViewById(R.id.regbutton);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId(); // this will get the if id of the button that is selected
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

//                obtain the data entered by the user
                String textFullName = fullName.getText().toString();
                String textEmail = email.getText().toString();
                String textDoB = dateOfBirth.getText().toString();
                String textMobile = mobile.getText().toString();
                String textPwd = password.getText().toString();
                String textConfirmPwd = confirmPassword.getText().toString();
                String textGender; // cant obtain the value before verifing if any button was selected or not

//                coding the if else block
                /*
                TextUtil class checks if the test is empty or not if it is empty it should throw an error
                 */
                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this,"Please enter the full name ",Toast.LENGTH_LONG).show();
                    fullName.setError("Full Name Required ");
                    fullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this,"Please enter your email",Toast.LENGTH_LONG).show();
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){ // to check the email address pattern
                    Toast.makeText(RegisterActivity.this,"Please re-enter your email",Toast.LENGTH_LONG).show();
                    email.setError("It is not a valid email Address");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(textDoB)){
                    Toast.makeText(RegisterActivity.this,"Please enter DoB",Toast.LENGTH_LONG).show();
                    dateOfBirth.setError("DoB is required");
                    dateOfBirth.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId()==-1){
                    Toast.makeText(RegisterActivity.this,"Gender Not Selected ",Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is Required ");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)){
                    Toast.makeText(RegisterActivity.this,"Please enter Mobile Number ",Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile number is required");
                    mobile.requestFocus();
                } else if (textMobile.length()!=10){
                    Toast.makeText(RegisterActivity.this,"Please enter a valid mobile number ",Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile number should be 10 digits ");
                    mobile.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this,"Please enter your password ",Toast.LENGTH_LONG).show();
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (textPwd.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password should be more than 6 digits  ",Toast.LENGTH_LONG).show();
                    password.setError("Password Too Weak");
                    password.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this,"Please re-enter the password ",Toast.LENGTH_LONG).show();
                    confirmPassword.setError("please re-type the password");
                    confirmPassword.requestFocus();
                } else if (!textPwd.equals(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this,"Passwords Not Match ",Toast.LENGTH_LONG).show();
                    confirmPassword.setError("Please re-enter the password");
                    confirmPassword.requestFocus();
//                    clear the entered password
                    password.clearComposingText();
                    confirmPassword.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName,textEmail,textDoB,textGender,textPwd,textConfirmPwd);
                }

            }
        });

    }

//    Register user using the credentials are given
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textPwd, String textConfirmPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registration Succesfull",Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    send the verification Email
                    firebaseUser.sendEmailVerification();

                    /*

//                    open user profile after the successfull registration activity
                    Intent intent = new Intent(RegisterActivity.this,UserProfileActivity.class);
//                    to prevent the user from returning back to Register Activity on pressing back button after registration
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish(); // to close Register activity

                     */

                }
            }
        });
    }
}

