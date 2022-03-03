package com.example.calsakay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Conversation extends AppCompatActivity implements DatabaseAccessCallback {

    List<ConversationModel> convo = new ArrayList<>();
    RecyclerView rvConvo;
    int chatMateId, userId;
    Messages messageData;
    String threadName, chatmateImage;
    ConversationRecViewAdapter convoAdapter = new ConversationRecViewAdapter();
    CircularProgressButton sendButton;
    EditText etMessage;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
    ImageView chatImage;
    TextView chatName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        DatabaseAccess dbAccess = new DatabaseAccess(this);

        rvConvo = findViewById(R.id.rvConversation);
        sendButton = findViewById(R.id.btMessageSend);
        etMessage = findViewById(R.id.etTypeMEssage);
        chatImage = findViewById(R.id.ivChatmateImage);
        chatName = findViewById(R.id.tvConvoName);
        getSupportActionBar().hide();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent = etMessage.getText().toString(), messageTimeStamp = dateFormat.format(new Date()), messageType = "outgoing";
                if(!messageContent.matches("")){
                    convo.add(new ConversationModel(messageContent, messageTimeStamp, messageType));
                    convoAdapter.notifyItemInserted(convo.size() - 1);
                    rvConvo.scrollToPosition(convo.size() - 1);
                    dbAccess.executeNonQuery("INSERT INTO `messages` " +
                            "(`message_id`, `sender`, `reciever`, `message`, `read`, `time`) " +
                            "VALUES (NULL, " + userId + ", " + chatMateId + ", '" + messageContent + "', " + 1 + ", CURRENT_TIMESTAMP)");
                    etMessage.setText("");
                }
            }
        });

        Intent intent = getIntent();
        messageData = (Messages) intent.getSerializableExtra("messageData");


        chatMateId = messageData.getChatMateId();
        userId = messageData.getPassengerId();
        chatmateImage = messageData.getChatmateImage();
        InputStream stream = new ByteArrayInputStream(Base64.decode(chatmateImage.getBytes(), Base64.DEFAULT));
        Bitmap chatImageBitmap = BitmapFactory.decodeStream(stream);
        chatImage.setImageBitmap(chatImageBitmap);
        chatName.setText(messageData.getThreadName());


// SELECT CONCAT(calsakay_tbl_users.first_name, ' ', calsakay_tbl_users.last_name) AS threadName, IF(messages.sender = 62, 'outgoing', 'ingoing') AS messageType, messages.sender, messages.reciever, messages.message_id, messages.message, messages.read, messages.time, messages.read FROM `messages` JOIN calsakay_tbl_users ON IF(messages.sender = 62, messages.reciever = calsakay_tbl_users.id, messages.sender = calsakay_tbl_users.id) WHERE (`reciever` = 62 AND `sender` = 78) OR (`reciever` = 78 AND `sender` = 62)

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 5000);
                dbAccess.executeQuery("SELECT CONCAT(calsakay_tbl_users.first_name, ' ', " +
                        "calsakay_tbl_users.last_name) AS threadName, " +
                        "IF(messages.sender = " + userId + ", 'outgoing', 'ingoing') AS messageType, " +
                        "messages.sender, messages.reciever, messages.message_id, messages.message, " +
                        "messages.read, messages.time, messages.read FROM `messages` JOIN calsakay_tbl_users " +
                        "ON IF(messages.sender = " + userId + ", messages.reciever = calsakay_tbl_users.id, messages.sender = calsakay_tbl_users.id) " +
                        "WHERE (`reciever` = " + userId + " AND `sender` = " + chatMateId + ") OR (`reciever` = " + chatMateId + " AND `sender` = " + userId + ")");
            }
        }, 3000);
    }

    @Override
    public void QueryResponse(List<String[]> data) {

        if(data != null){
            if(convo.size() == 0){
                for (String[] row : data) {
                    convo.add(new ConversationModel(row[5],row[7], row[1]));
                }

                convoAdapter.setConvo(convo);
                rvConvo.setAdapter(convoAdapter);
                LinearLayoutManager ll = new LinearLayoutManager(this);
                ll.setStackFromEnd(true);
                rvConvo.setLayoutManager(ll);
            } else if(convo.size() < data.size()){
                convo.add(new ConversationModel(data.get(data.size() - 1)[5],data.get(data.size() - 1)[7], data.get(data.size() - 1)[1]));
                convoAdapter.notifyItemInserted(convo.size() - 1);
                rvConvo.scrollToPosition(convo.size() - 1);
            }
        }
    }
}