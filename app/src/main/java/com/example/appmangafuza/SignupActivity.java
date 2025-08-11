package com.example.appmanga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private CheckBox checkboxTermsConsent;
    private TextView textTermsAndConditions;
    private Button buttonRegister;
    private ImageView backArrow;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backArrow = findViewById(R.id.back_arrow);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextEmail = findViewById(R.id.edit_text_email);
        checkboxTermsConsent = findViewById(R.id.checkbox_terms_consent);
        textTermsAndConditions = findViewById(R.id.text_terms_and_conditions);
        buttonRegister = findViewById(R.id.button_register);

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ tất cả thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Mật khẩu và xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkboxTermsConsent.isChecked()) {
                Toast.makeText(SignupActivity.this, "Bạn phải đồng ý với Điều khoản sử dụng.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Map<String, Object> userProfile = new HashMap<>();
                                    userProfile.put("username", username);
                                    userProfile.put("email", email);
                                    userProfile.put("phone", phone);
                                    userProfile.put("coins", 0);
                                    userProfile.put("avatarUrl", "");
                                    userProfile.put("level", 1);

                                    db.collection("users").document(user.getUid())
                                            .set(userProfile)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> firestoreTask) {
                                                    if (firestoreTask.isSuccessful()) {
                                                        Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, "Đăng ký thành công nhưng không lưu được thông tin bổ sung: " + firestoreTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                String errorMessage = "Đăng ký thất bại.";
                                if (task.getException() != null) {
                                    errorMessage += " Lỗi: " + task.getException().getMessage();
                                }
                                Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        textTermsAndConditions.setOnClickListener(v -> {
            Toast.makeText(SignupActivity.this, "Mở trang Điều khoản sử dụng", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(SignupActivity.this, TermsAndConditionsActivity.class);
            // startActivity(intent);
        });
    }
}