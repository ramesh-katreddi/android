/*
 * WhatsDplan CONFIDENTIAL
 * __________________
 *                        
 * WhatDplan Mobile Application 2015 
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of WhatDplan and its suppliers, if any. The
 * intellectual and technical concepts contained herein are proprietary to WhatDplan and its suppliers and may be
 * covered by U.S. and Foreign Patents,patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from WhatDplan.
 *                       
 */

package whatsdplan.com.chatscreen.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import whatsdplan.com.chatscreen.IntentConstants;
import whatsdplan.com.chatscreen.R;
import whatsdplan.com.chatscreen.activities.ChatActivity;
import whatsdplan.com.chatscreen.models.User;
import whatsdplan.com.chatscreen.thirdparty.RoundedImageView;

/**
 * user list
 */
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    private final Activity mActivity;
    /**
     * infalter is required for inflating the view users.
     */
    private final LayoutInflater inflater;

    private final List<User> users;

    /**
     * constructor
     *
     * @param activity pass the context of to be used by inflater
     * @param users    user to show
     */
    public UserRecyclerAdapter(Activity activity, ArrayList<User> users) {
        this.inflater = LayoutInflater.from(activity);
        this.mActivity = activity;
        this.users = users;

    }

    /**
     * view holder for the recycler view.
     *
     * @param parent parent of recycler view.
     * @return returns a view holder object which is like a template of how a view should look.
     * everytime only view is binded to viewholder but not created.
     */
    @Override
    public UserRecyclerAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        return (new UserViewHolder(view));
    }

    /**
     * BInding the users to the view hodler.
     * remember viewholder objects are not created everytime.
     * they are recycled and new users gets binded everytime.
     *
     * @param viewHolder view holder object for which users at following position needs to be adapted.
     * @param position   position of the list item.
     */
    @Override
    public void onBindViewHolder(final UserRecyclerAdapter.UserViewHolder viewHolder, int position) {

        final User curObject = users.get(position);
        if (viewHolder != null && curObject != null) {
            viewHolder.mUserName.setText(curObject.getName());
            viewHolder.mUserLastMessage.setText("As far as I know that works for us!");

            //TODO Remove
            int profilePicDrawable = R.drawable.ic_person_black_24dp;

            //Randomly picking some images that i included in drawables
            switch (position) {
                case 0:
                case 7:
                    profilePicDrawable = R.drawable.pic1;
                    break;
                case 1:
                case 9:
                    profilePicDrawable = R.drawable.pic2;
                    break;
                case 3:
                case 10:
                    profilePicDrawable = R.drawable.pic3;
                    break;
                case 5:
                case 11:
                    profilePicDrawable = R.drawable.prof4;
                    break;
            }
            viewHolder.mProfilePic.setImageResource(profilePicDrawable);


            // This gets enabled when the swipe action happens
            viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(viewHolder.getAdapterPosition());
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.user_info_dialog);
                    dialog.show();
                }
            });


            // On clicking this button move to chat screen
            viewHolder.mNavigateToChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActivity != null) {
                        Intent intent = new Intent(mActivity, ChatActivity.class);
                        intent.putExtra(IntentConstants.USER_WE_ARE_CHATTING_WITH, curObject);
                        mActivity.startActivity(intent);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    private void removeItem(int adapterPosition) {
        users.remove(adapterPosition);
        notifyDataSetChanged();
    }


    /**
     * this is the view holdr which represents one single list item.(should not call list item but in this context its fine).
     * notmally it is recycler view item.
     */

    class UserViewHolder extends RecyclerView.ViewHolder {
        /**
         * get all the view users.
         */

        final SwipeLayout swipeLayout;
        final TextView mUserName;
        final TextView mUserLastMessage;
        final RoundedImageView mProfilePic;
        final ImageView mNavigateToChat;
        final View mDeleteBtn;

        /**
         * constructor
         *
         * @param itemView view object that viewholder contains.
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            mProfilePic = (RoundedImageView) itemView.findViewById(R.id.profPic);
            mUserName = (TextView) itemView.findViewById(R.id.name);
            mUserLastMessage = (TextView) itemView.findViewById(R.id.last_message);
            mNavigateToChat = (ImageView) itemView.findViewById(R.id.navigate);
            mDeleteBtn = itemView.findViewById(R.id.delete_btn);
        }
    }
}


