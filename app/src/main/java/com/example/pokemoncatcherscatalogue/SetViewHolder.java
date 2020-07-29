package com.example.pokemoncatcherscatalogue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import models.Set;

public class SetViewHolder extends ChildViewHolder {

    private static final long FADE_DURATION = 1250;
    private TextView tvSetName;
    private ImageView ivLogo;
    private RelativeLayout rlSet;
    private Button btnStatistics;
    private Context context;

    public SetViewHolder(View itemView) {
        super(itemView);

        // Get context
        context = itemView.getContext();

        // Link up views
        tvSetName = itemView.findViewById(R.id.tvSetName);
        ivLogo = itemView.findViewById(R.id.ivLogo);
        rlSet = itemView.findViewById(R.id.rlSet);
        btnStatistics = itemView.findViewById(R.id.btnStatistics);
    }

    public void setAll(final Set set) {
        // Put in the data
        tvSetName.setText(set.getName());
        tvSetName.setText(set.getName());
        Glide.with(context).load(set.getLogoUrl()).into(ivLogo);

        // Set up the on click listener for rlSet
        rlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start an intent to go to the SingleSetActivity
                Intent intent = new Intent(context, SingleSetActivity.class);
                intent.putExtra("code", set.getCode());
                intent.putExtra("logo", set.getLogoUrl());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, (View)ivLogo, "Image of logo");
                context.startActivity(intent, options.toBundle());
            }
        });

        // Set on click listener to go to the SetStatistics activity
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SetStatistics.class);
                intent.putExtra("code", set.getCode());
                intent.putExtra("logo", set.getLogoUrl());
                intent.putExtra("symbol", set.getSymbolUrl());
                intent.putExtra("name", set.getName());
                intent.putExtra("total", set.getTotalCards());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, (View)ivLogo, "Image of logo");
                context.startActivity(intent, options.toBundle());
            }
        });

        // Set the view to fade in
        setFadeAnimation(itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
