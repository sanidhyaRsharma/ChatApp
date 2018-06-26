package com.example.chatapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatsFragment extends Fragment {

    private FirebaseListAdapter<ChatMessages> adapter;
    private EditText input;
    private View v;

    public ChatsFragment() {
        // Required empty public constructor
    }
    public void displayMessages(View view){

        ListView listOfMessages = view.findViewById(R.id.list_of_messages);
        FirebaseListOptions<ChatMessages> options = new FirebaseListOptions.Builder<ChatMessages>()
                .setLayout(R.layout.message)
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Conversations"), ChatMessages.class)
                .setLifecycleOwner(this)
                .build();

        adapter = new FirebaseListAdapter<ChatMessages>(options) {
            @Override
            protected void populateView(View v, ChatMessages model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chats, container, false);

        //displayMessages(v);
        FloatingActionButton fab = v.findViewById(R.id.fab);
        input = v.findViewById(R.id.input);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!input.getText().toString().equals("")) {
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Conversations")
                            .push()
                            .setValue(new ChatMessages(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );
                    // Clear the input
                    displayMessages(v);
                    input.setText("");
                }
            }
        });
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

}
