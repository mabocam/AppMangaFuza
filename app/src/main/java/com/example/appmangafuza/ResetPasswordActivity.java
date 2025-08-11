package com.example.appmanga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editTextEmailReset;
    private Button buttonSendResetLink;
    private TextView textBackToLogin;
    private ImageView backArrow;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        backArrow = findViewById(R.id.back_arrow);
        editTextEmailReset = findViewById(R.id.edit_text_email_reset);
        buttonSendResetLink = findViewById(R.id.button_send_reset_link);
        textBackToLogin = findViewById(R.id.text_back_to_login);

        backArrow.setOnClickListener(v -> {
            finish();
        });

        buttonSendResetLink.setOnClickListener(v -> {
            String email = editTextEmailReset.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập email của bạn.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn.", Toast.LENGTH_LONG).show();
                                // Tùy chọn: Chuyển về màn hình đăng nhập sau khi gửi thành công
                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String errorMessage = "Gửi yêu cầu thất bại.";
                                if (task.getException() != null) {
                                    errorMessage += " Lỗi: " + task.getException().getMessage();
                                }
                                Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        textBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}