package com.techiguru.ngo_application;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupDynamicGreeting();
    }

    private void setupDynamicGreeting() {
        TextView greetingText = findViewById(R.id.tv_greeting_dynamic);
        TextView userNameText = findViewById(R.id.tv_user_name);
        
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (timeOfDay < 12) {
            greeting = "Good Morning,";
        } else if (timeOfDay < 16) {
            greeting = "Good Afternoon,";
        } else if (timeOfDay < 21) {
            greeting = "Good Evening,";
        } else {
            greeting = "Good Night,";
        }

        greetingText.setText(greeting);
        userNameText.setText(getString(R.string.user_name_placeholder));
    }
}