package com.uxteam.starget.im_sys;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.uxteam.starget.R;

import cn.jiguang.imui.commons.ImageLoader;

public class AvatarLoader implements ImageLoader {
    private Context context;

    public AvatarLoader(Context context) {
        this.context = context;
    }
    @Override
    public void loadAvatarImage(ImageView avatarImageView, String string) {
        Glide.with(context)
                .load(string)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(avatarImageView);
    }

    @Override
    public void loadImage(ImageView imageView, String string) {

    }

    @Override
    public void loadVideo(ImageView imageCover, String uri) {

    }
}
