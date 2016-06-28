package whatsdplan.com.chatscreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import whatsdplan.com.chatscreen.AppConstants;
import whatsdplan.com.chatscreen.IntentConstants;
import whatsdplan.com.chatscreen.R;
import whatsdplan.com.chatscreen.adapters.ChatListAdapter;
import whatsdplan.com.chatscreen.models.ChatMessageItem;
import whatsdplan.com.chatscreen.models.User;

/**
 * chat screen per user
 * <p/>
 * Created by Ramesh on 2/5/16.
 */
public class ChatActivity extends AppCompatActivity {


    //db object
    private Realm mRealmObj;

    // recycler adapter to show chat items
    private ChatListAdapter mRecyclerAdapter;

    // So person who is chatting is me. But who is on the other side.
    private User mOtherChatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        //Pass the user details from previous activity
        if (getIntent() != null) {
            mOtherChatter = getIntent().getParcelableExtra(IntentConstants.USER_WE_ARE_CHATTING_WITH);
        }

        if (mOtherChatter != null) {
            //set the otehr person name with whom i am chatting as the name of actionbar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mOtherChatter.getName());
            }

            // database object
            mRealmObj = Realm.getInstance(
                    new RealmConfiguration.Builder(this)
                            .name("chat_message_items.realm")
                            .build()
            );

            // Chat list handling starts here.

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_list);
            if (recyclerView != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                // Needed this as we stack the list items from bottom to top
                layoutManager.setReverseLayout(true);
                recyclerView.setLayoutManager(layoutManager);
                ArrayList<ChatMessageItem> items = new ArrayList<>();

                // This essentially means. get alll messages where I am the sender and otherchatter is receiver and I am receiver and otherchatter is sender.
                RealmResults<ChatMessageItem> senderChatMessageItems =
                        mRealmObj.where(ChatMessageItem.class).equalTo("senderUserId", mOtherChatter.getId()).equalTo("receiverUserId", AppConstants.ID).or().equalTo("senderUserId", AppConstants.ID).equalTo("receiverUserId", mOtherChatter.getId()).findAll().sort("time", Sort.DESCENDING);


                items.addAll(senderChatMessageItems);

/*
                items.add(new ChatItem("Long time no talk!!", mOtherChatter.getId()));
                items.add(new ChatItem("Hey How was your day? Do you wanna grab a bite later today? i see you are in neck of my woods!! lol", mSelfUserId));
                items.add(new ChatItem("12/23/2016", -1));
                items.add(new ChatItem("I would love to", mOtherChatter.getId()));
                items.add(new ChatItem("Hey I havent seen you in ages. I just saw that you were around here and was going to see if you wanted to go for lunch", mSelfUserId));

*/

                mRecyclerAdapter = new ChatListAdapter(this, items);
                recyclerView.setAdapter(mRecyclerAdapter);
            }


            // This is where user types chat messages.

            final EditText userTyped = (EditText) findViewById(R.id.chat_message);

            if (userTyped != null) {

                userTyped.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE || ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (actionId == KeyEvent.KEYCODE_ENTER))) {
                            Editable text = userTyped.getText();
                            if (text != null && text.length() > 0) {
                                // Perform action on pressing done button

                                //Add the item to db and also reflect it in reccyclerview
                                ChatMessageItem createdItem = addChatItem(userTyped.getText().toString(), AppConstants.ID, mOtherChatter.getId());
                                mRecyclerAdapter.addData(createdItem);

                                //Dummy item by another user.
                                ChatMessageItem secondPersonChatMessageItem = addChatItem(getString(R.string.other_user_response) + mRecyclerAdapter.getItemCount(), mOtherChatter.getId(), AppConstants.ID);
                                mRecyclerAdapter.addData(secondPersonChatMessageItem);

                                // reset the text
                                userTyped.setText("");
                            } else {
                                Toast.makeText(ChatActivity.this, R.string.invalid_chat_message, Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }

        }
    }

    /**
     * adds a chat item to db
     *
     * @param message        message that is typed
     * @param senderUserId   who has sent this message
     * @param receiverUserId who is receiving it.
     * @return returns the chatitem that is created now.
     */
    private ChatMessageItem addChatItem(String message, String senderUserId, String receiverUserId) {

        ChatMessageItem item = null;
        if (mRealmObj != null && message != null && !message.isEmpty()) {
            mRealmObj.beginTransaction();
            // Create an object
            item = mRealmObj.createObject(ChatMessageItem.class);
            // Looks like there is no way we can tell auto increment to realm. so finding this way.
            item.setId(mRealmObj.where(ChatMessageItem.class).max("id").intValue() + 1);
            item.setMessage(message);
            item.setSenderUserId(senderUserId);
            item.setReceiverUserId(receiverUserId);
            item.setTime(System.currentTimeMillis());

            mRealmObj.commitTransaction();
        }
        return item;
    }
}

