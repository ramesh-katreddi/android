package whatsdplan.com.custom_seekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Ramesh on 11/5/16.
 */
public class CustomSeekbar extends LinearLayout {


    //What is the maximum progress that can be attained.
    //Please have a look at Caliberation logic to know how exactly this is calculated
    private static final int MAX_PROGRESS = 240;
    //Number of intervals we are showing
    private static final int NUMBER_OF_INTERVALS = 5;

    //Context in which this seekbar is present
    private Context mContext;

    //infalter
    private LayoutInflater mInflater;

    //COmmunication between seekbar and activity
    private IProgressUpdater mProgressUpdater;


    public CustomSeekbar(Context context) {
        super(context);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        mContext = context;
        if (context != null) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (mInflater != null) {
                mInflater.inflate(R.layout.custom_component_seekbar, this, true);


                // First child is seekbar
                // second child is the scale which in this case is a linear layout
                SeekBar seekBar = (SeekBar) getChildAt(0);

                LinearLayout seekbarIntervals = (LinearLayout) getChildAt(1);
                if (seekBar != null) {

                    seekBar.setMax(MAX_PROGRESS);
                    seekBar.setProgress(MAX_PROGRESS / NUMBER_OF_INTERVALS);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            Log.e("SEEKBAR progress", String.valueOf(progress));
                            caliberate(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {


                        }
                    });
                    addIntervalLabel(seekbarIntervals, 15, "Mins");
                    addIntervalLabel(seekbarIntervals, 30, "Mins");
                    addIntervalLabel(seekbarIntervals, 1, "Hr");
                    addIntervalLabel(seekbarIntervals, 2, "Hrs");
                    addIntervalLabel(seekbarIntervals, 4, "Hrs");
                    addIntervalLabel(seekbarIntervals, 8, "Hrs");
                }
            }
        }
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setEventListener(IProgressUpdater eventListener) {
        this.mProgressUpdater = eventListener;
    }


    /**
     * Max progress value is 240.
     * this is the way it is decided.
     *
     *  possible step size for each step should be LCM of all steps sizes required:
     *  15 - 30 --> 3
     *  30 - 60 --> 6
     *  1hr - 2hrs -> 4
     *  2 hrs - 4hrs -> 8
     *  4hrs - 8hrs -> 16
     *  lcm of 3 6 4 8 16 is 48
     *  So 5 steps of 48 size of each is 240. The max value.
     *
     * 0  = 15 mins
     * 16 = 20 mins
     * 32 = 25 mins
     *
     * 48 = 30 mins
     * 56 = 35 mins
     * 64 = 40 mins
     * 72 = 45 mins
     * 80 = 50 mins
     * 88 = 55 mins
     *
     * 96 = 1 hour
     * 108 = 1 hour 15 mins
     * 120 = 1 hour 30 mins
     * 132 = 1 hour 45 mins
     *
     * 144 = 2 hours
     * 150  = 2 hours 15 mins
     * 156  = 2 hours 30 mins
     * 162  = 2 hours 45 mins
     * 168  = 3 hours 00 mins
     * 174  = 3 hours 15 mins
     * 180  = 3 hours 30 mins
     * 186  = 3 hours 45 mins
     *
     * 192 = 4 hours
     * 195 = 4 hours 15 mins
     * 198 = 4 hours 30 mins
     * 201 = 4 hours 45 mins
     * 204 = 5 hours
     * 207 = 5 hours 15 mins
     * 210 = 5 hours 30 mins
     * 213 = 5 hours 45 mins
     * 216 = 6 hours
     * 219 = 6 hours 15 mins
     * 222 = 6 hours 30 mins
     * 225 = 6 hours 45 mins
     * 228 = 7 hours
     * 231 = 7 hours 15 mins
     * 234 = 7 hours 30 mins
     * 237 = 7 hours 45 mins
     * 240 = 8 hours
     *
     *
     */

    /**
     * caliberation of seekbar
     *
     * @param progress progress made so far
     */
    private void caliberate(int progress) {

        String textToShow = null;
        int intervalSize = MAX_PROGRESS / NUMBER_OF_INTERVALS; // 240/5 = 48
        int whichInterval = progress / intervalSize;  // Current progress falls under which interval
        progress = progress - intervalSize * whichInterval;
        Log.e("SEEKBAR interval", String.valueOf(whichInterval));
        switch (whichInterval) {
            case 0: // 15-30 mins
            case 1: // 30-60 mins
                /**
                 * case 5 minutes interval
                 */

                // case interval 15-30 mins
                int stepSize = 16; // refer to previous list
                int beginWithMinute = 15;

                if (whichInterval == 1) { // 30 - 60 mins
                    stepSize = 8;
                    beginWithMinute = 30;
                }

           /*     if (progress % stepSize == 0) */{
                    int minutes = beginWithMinute + 5 * (progress / stepSize);
                    textToShow = String.format(mContext.getString(R.string.minutes_template), minutes);
                }
                break;
            case 2:
            case 3:
            case 4:
                /**
                 * case 15 minutes interval
                 */

                // case 1hrs - 2hrs
                int stepSizeFor15 = 12;
                int beginWithHour = 1;
                if (whichInterval == 3) { // Case 2hrs - 4hrs
                    stepSizeFor15 = 6;
                    beginWithHour = 2;
                } else if (whichInterval == 4) { // case 4hrs - 8hrs
                    stepSizeFor15 = 3;
                    beginWithHour = 4;
                }

  /*              if (progress % stepSizeFor15 == 0) */{
                    int currentStep = progress / stepSizeFor15;
                    int hours = currentStep / 4 + beginWithHour;
                    int minutes = (currentStep % 4 * 15);
                    if (minutes == 0) {
                        textToShow = String.format(mContext.getString(R.string.hours_template), hours);
                    } else {
                        textToShow = String.format(mContext.getString(R.string.horus_mins_template), hours, minutes);
                    }
                }
                break;
            case 5:
                /**
                 * 8hrs
                 */
                textToShow = String.format(mContext.getString(R.string.hours_template), 8);
                break;
        }
        if (textToShow != null)
            Log.e("SEEKBAR TEXT", textToShow);

        if (textToShow != null && mProgressUpdater != null)
            mProgressUpdater.onProgress(textToShow);

    }

    /**
     * Add interval labell to linear layout of intervals with equal margins.
     *
     * @param seekbarIntervals linear layout to show scale for seekbar
     * @param i                time ex. 30
     * @param units            time units minutes .
     */
    private void addIntervalLabel(LinearLayout seekbarIntervals, int i, String units) {

        if (mInflater != null) {
            LinearLayout labelParent = (LinearLayout) mInflater.inflate(R.layout.seekbar_step_label, seekbarIntervals, false);
            seekbarIntervals.addView(labelParent);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) labelParent.getLayoutParams();
            if (params != null) {
                params.weight = 1.0f;
            }
            TextView timeView = (TextView) labelParent.findViewById(R.id.time);
            TextView timeUnitsView = (TextView) labelParent.findViewById(R.id.time_units);
            timeView.setText(String.valueOf(i));
            timeUnitsView.setText(units);
        }
    }


    public interface IProgressUpdater {
        void onProgress(String textToShow);
    }

}
