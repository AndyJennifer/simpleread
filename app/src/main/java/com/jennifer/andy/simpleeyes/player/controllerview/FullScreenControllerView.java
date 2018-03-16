package com.jennifer.andy.simpleeyes.player.controllerview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;

import com.jennifer.andy.simpleeyes.R;
import com.jennifer.andy.simpleeyes.entity.Content;
import com.jennifer.andy.simpleeyes.player.IjkMediaController;
import com.jennifer.andy.simpleeyes.widget.font.CustomFontTextView;

/**
 * Author:  andy.xwt
 * Date:    2018/3/13 17:19
 * Description:全屏视频控制器
 */


public class FullScreenControllerView extends ControllerView implements View.OnClickListener {

    private ImageView mMinScreen;
    private ImageView mPauseButton;
    private ImageView mNextButton;
    private SeekBar mProgress;

    private CustomFontTextView mTitle;
    private CustomFontTextView mEndTime;
    private CustomFontTextView mCurrentTime;


    public FullScreenControllerView(MediaController.MediaPlayerControl player, IjkMediaController controller, Content currentVideoInfo, Context context) {
        super(player, controller, currentVideoInfo, context);
    }

    @Override
    public void initView(View rootView) {
        mMinScreen = rootView.findViewById(R.id.iv_min_screen);
        mProgress = rootView.findViewById(R.id.sb_progress);
        mTitle = rootView.findViewById(R.id.tv_title);
        mPauseButton = rootView.findViewById(R.id.iv_pause);
        mNextButton = rootView.findViewById(R.id.iv_next);
        mProgress = rootView.findViewById(R.id.sb_progress);
        mCurrentTime = rootView.findViewById(R.id.tv_currentTime);
        mEndTime = rootView.findViewById(R.id.tv_end_time);

        //初始化标题
        mTitle.setText(mCurrentVideoInfo.getData().getTitle());

        //初始化播放时间
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        mCurrentTime.setText(stringForTime(position));
        mEndTime.setText(stringForTime(duration));

        //初始化进度条
        mProgress.setMax(1000);
        if (duration >= 0 && mPlayer.getBufferPercentage() > 0) {
            long progress = 1000L * position / duration;
            int secondProgress = mPlayer.getBufferPercentage() * 10;
            mProgress.setProgress((int) progress);
            mProgress.setSecondaryProgress(secondProgress);
        }
    }

    @Override
    public void initControllerListener() {
        mMinScreen.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pause://暂停按钮
                togglePause();
                mController.show();
                break;
            case R.id.iv_next://下一个
                mController.getControllerListener().onNextClick();
                break;
            case R.id.iv_min_screen://返回小界面
                mController.changeControllerView(new TinyControllerView(mPlayer, mController, mCurrentVideoInfo, mContext));
                break;
        }

    }


    @Override
    public void updateProgress(int progress, int secondaryProgress) {
        mProgress.setProgress(progress);
        mProgress.setSecondaryProgress(secondaryProgress);
    }

    @Override
    public void updateTime(String currentTime, String endTime) {
        mCurrentTime.setText(currentTime);
        mEndTime.setText(endTime);
    }

    @Override
    public void updateTogglePauseUI(boolean isPlaying) {
        if (isPlaying) {
            mPauseButton.setImageResource(R.drawable.ic_player_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.ic_player_play);
        }
    }

    @Override
    public void hideNextButton() {
        if (mNextButton != null && mNextButton.getVisibility() == View.VISIBLE) {
            mNextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int setControllerLayoutId() {
        return R.layout.layout_media_controller_full_screen;
    }


}
