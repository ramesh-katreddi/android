package whatsdplan.com.custom_seekbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Ramesh on 11/5/16.
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        //Button to trigger
        View dialogTrigger = findViewById(R.id.dialog_trigger);
        if (dialogTrigger != null)
            dialogTrigger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //transclucent dialog
                    final Dialog dialog = new Dialog(LauncherActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_content);


                    //making dialog background transparent
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    // view to show count
                    final TextView countView = (TextView) dialog.findViewById(R.id.count_view);
                    CustomSeekbar seekbar = (CustomSeekbar) dialog.findViewById(R.id.custom_seekbar);

                    //Listen to progress updates and show text appropriately.
                    // Wantedly progress is sent and instead text to show is passed.
                    // Since caliberation logic is tight coupled with scale, so letting component handle everything
                    seekbar.setEventListener(new CustomSeekbar.IProgressUpdater() {
                        @Override
                        public void onProgress(String textToShow) {
                            countView.setText(textToShow);
                        }
                    });
                    dialog.show();

                }
            });
    }
}
