package com.example.chatbotapp;

import static com.example.chatbotapp.R.drawable.artificial_intelligence;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

public class MainActivity extends AppCompatActivity {

    private EditText queryEditText;
    private ImageView sendQuery;
    private ImageView upload;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private LinearLayout chatResponse;
    private ChatFutures chatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });








        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }





        sendQuery = findViewById(R.id.sendMessage);
        queryEditText = findViewById(R.id.queryEditText);
        progressBar =findViewById(R.id.progressBar);
        chatResponse = findViewById(R.id.chatRespond);

            chatModel = getChatModel();




            //when the user pressed enter
        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Perform action on Enter key press
                   sendMessage();
                }

                return false;
            }
        });





        sendQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                sendMessage();
                }
            });
        }

    private void sendMessage() {
        progressBar.setVisibility(View.VISIBLE);
        String query = queryEditText.getText().toString();

        queryEditText.setText("");

        chatBody("You",query,getDrawable(R.drawable.woman));
        GeminiResp.getResponse(chatModel, query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                chatBody("AI",response,getDrawable(artificial_intelligence));

            }

            @Override
            public void onError(Throwable throwable) {
                chatBody("AI","please try again",getDrawable(artificial_intelligence));

                progressBar.setVisibility(View.GONE);

            }
        });
    }




    private ChatFutures getChatModel (){
        GeminiResp model = new GeminiResp();
        GenerativeModelFutures modelFutures = model.getModel();
        return  modelFutures.startChat();
    }

    private void chatBody(String username, String query, Drawable image) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_message,null);



        TextView name = view.findViewById(R.id.name);
        TextView message = view.findViewById(R.id.message_body);
        ImageView logo = view.findViewById(R.id.avatar);

        name.setText(username);
        message.setText(query);
        logo.setImageDrawable(image);

        chatResponse.addView(view);

        ScrollView scrollView = findViewById(R.id.scrollview);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }





}