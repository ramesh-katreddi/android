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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import whatsdplan.com.chatscreen.AppConstants;
import whatsdplan.com.chatscreen.R;
import whatsdplan.com.chatscreen.models.ChatMessageItem;

/**
 * individual chat item
 */
public class ChatListAdapter extends RecyclerView.Adapter {

    private static final int CHAT_BY_ME = 0;
    private static final int CHAT_BY_FRIEND = 1;
    private static final int DATE_VIEW = 2;
    /**
     * infalter is required for inflating the view items.
     */
    private final LayoutInflater inflater;

    private final List<ChatMessageItem> items;

    /**
     * constructor
     *
     * @param activity pass the context in which this list is being shown
     * @param items    input chat items
     */
    public ChatListAdapter(Activity activity, ArrayList<ChatMessageItem> items) {
        this.inflater = LayoutInflater.from(activity);
        this.items = items;

    }

    /**
     * view holder for the recycler view.
     * <p/>
     * getItemType is overridden below to return the userId.
     * <p/>
     * User id = unique id means its me.
     * user Id is not equal to my unique id means its the people I am chatting with
     * user id is not present then its date entity.
     *
     * @param parent parent of recycler view.
     * @return returns a view holder object which is like a template of how a view should look.
     * everytime only view is binded to viewholder but not created.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == CHAT_BY_ME) {
            view = inflater.inflate(R.layout.chat_typed_by_me, parent, false);
        } else if (viewType == CHAT_BY_FRIEND) {
            view = inflater.inflate(R.layout.chat_typed_by_friend, parent, false);
        } else
            view = inflater.inflate(R.layout.chat_date, parent, false);
        return (new MyChatViewHolder(view));
    }

    /**
     * BInding the items to the view hodler.
     * remember viewholder objects are not created everytime.
     * they are recycled and new items gets binded everytime.
     *
     * @param viewHolder view holder object for which items at following position needs to be adapted.
     * @param position   position of the list item.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ChatMessageItem curObject = items.get(position);
        if (viewHolder != null && curObject != null) {
            if (viewHolder instanceof MyChatViewHolder) {
                MyChatViewHolder holder = (MyChatViewHolder) viewHolder;
                holder.mChatMessage.setText(curObject.getMessage());
                if (holder.mTimeStamp != null)
                    holder.mTimeStamp.setText(convertDateToString(curObject.getTime()));
            }
        }
    }

    /**
     * @param position position
     * @return User id = 0 means its me.
     * user Id = -1 show date.
     * user id > 0 means its the people I am chatting with
     */

    @Override
    public int getItemViewType(int position) {
        ChatMessageItem item = items.get(position);
        String userId = item.getSenderUserId();
        if (userId != null) {
            if (userId.equals(AppConstants.ID)) return CHAT_BY_ME;
            else return CHAT_BY_FRIEND;
        }
        return DATE_VIEW;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * add the newly entered chat item to the beginning of the list so that it comes at the bottom
     *
     * @param item chat item to be added
     */
    public void addData(ChatMessageItem item) {
        items.add(0, item);
        notifyDataSetChanged();
    }

    // Simple date conversion
    private String convertDateToString(long time) {
        SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("HH:mm", Locale.US);
        return dateformatyyyyMMdd.format(new Date(time));
    }


    /**
     * this is the view holdr which represents one single list item.(should not call list item but in this context its fine).
     * notmally it is recycler view item.
     */

    class MyChatViewHolder extends RecyclerView.ViewHolder {
        /**
         * get all the view items.
         */
        final TextView mChatMessage;
        final TextView mTimeStamp;

        /**
         * constructor
         *
         * @param itemView view object that viewholder contains.
         */
        public MyChatViewHolder(View itemView) {
            super(itemView);
            mChatMessage = (TextView) itemView.findViewById(R.id.chat_message);
            mTimeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
        }
    }
}


