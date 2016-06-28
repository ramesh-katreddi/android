package whatsdplan.com.chatscreen.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * What a chat item consists of
 * <p/>
 * Created by ramesh on 2/5/16.
 */
public class ChatMessageItem extends RealmObject {

    @PrimaryKey
    private long id; // for identifying chat item... If not useful can remove TODO

    //actual chat message
    private String message;

    //time at which its typed
    private long time;

    // who sent this message
    private String senderUserId;

    //who receives this message
    private String receiverUserId;

    public ChatMessageItem() {
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String userId) {
        this.senderUserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
