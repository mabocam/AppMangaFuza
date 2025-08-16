package com.example.appmangafuza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appmangafuza.ResetPasswordActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewForgotPassword;
    private ImageView backArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các thành phần từ layout
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_button);
        textViewSignUp = findViewById(R.id.signup_text);
        textViewForgotPassword = findViewById(R.id.forgot_password_text);
        backArrowImageView = findViewById(R.id.back_arrow); // Ánh xạ nút back

        // Thiết lập lắng nghe sự kiện
        buttonLogin.setOnClickListener(v -> handleLogin());
        textViewSignUp.setOnClickListener(v -> handleSignUp());
        textViewForgotPassword.setOnClickListener(v -> handleForgotPassword());

        // Xử lý sự kiện khi nhấn nút back
        backArrowImageView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tên tài khoản và mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validateCredentials(username, password)) {
            // Đăng nhập thành công, chuyển sang MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng LoginActivity
        } else {
            Toast.makeText(this, "Tên tài khoản hoặc mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Giả lập logic xác thực
        return username.equals("user") && password.equals("pass");
    }

    private void handleSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void handleForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}