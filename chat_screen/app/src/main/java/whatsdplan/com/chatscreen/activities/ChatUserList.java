package whatsdplan.com.chatscreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import whatsdplan.com.chatscreen.R;
import whatsdplan.com.chatscreen.adapters.UserRecyclerAdapter;
import whatsdplan.com.chatscreen.models.User;

/**
 * User list
 * Created by Ramesh on 3/5/16.
 */
public class ChatUserList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        // User list handling
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.user_list);
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //DUmmy data
            //TODO remove
            ArrayList<User> users = new ArrayList<>();
            users.add(createDummyUser("aLake", "Alex Lake"));
            users.add(createDummyUser("bYoung", "Barry Young"));
            users.add(createDummyUser("LMartis", "Lilly Martinson"));
            users.add(createDummyUser("sammyL", "Sammy Lee"));
            users.add(createDummyUser("Tford", "Tania Ford"));
            users.add(createDummyUser("RameshKu", "Ramesh Kumar"));
            users.add(createDummyUser("aLake", "Alex Lake"));
            users.add(createDummyUser("bYOund", "Barry Young"));
            users.add(createDummyUser("lMartis", "Lilly Martinson"));
            users.add(createDummyUser("sammyL", "Sammy Lee"));
            users.add(createDummyUser("tFord", "Tania Ford"));
            users.add(createDummyUser("RameshKu", "Ramesh Kumar"));

            recyclerView.setAdapter(new UserRecyclerAdapter(this, users));
        }

    }

    private User createDummyUser(String userId, String userName) {
        User user = new User(userId);
        user.setName(userName);
        return user;
    }
}
