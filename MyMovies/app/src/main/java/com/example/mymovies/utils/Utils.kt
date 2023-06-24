package com.example.mymovies.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mymovies.R

class Utils {
    companion object {
        private const val ANIMATION_DURATION = 75L

        fun attachImageURL(path: String): String {
            return "${Constants.IMAGE_URL}${path}"
        }

        fun changeHeart(heart: ImageView, full: Boolean) {

            val imageId =
                if (full) {
                    R.drawable.ic_empty_heart
                } else {
                    R.drawable.ic_full_heart
                }

            heart.setImageResource(imageId)

            val scaleX =
                ObjectAnimator.ofFloat(heart, "scaleX", 1f, 1.2f).setDuration(ANIMATION_DURATION)
            val scaleY =
                ObjectAnimator.ofFloat(heart, "scaleY", 1f, 1.2f).setDuration(ANIMATION_DURATION)
            scaleX.repeatCount = 1
            scaleX.repeatMode = ObjectAnimator.REVERSE
            scaleY.repeatCount = 1
            scaleY.repeatMode = ObjectAnimator.REVERSE

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.start()
        }

        fun pressedAnimation(view : View) {
            val scaleX =
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f).setDuration(ANIMATION_DURATION)
            val scaleY =
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f).setDuration(ANIMATION_DURATION)
            val alpha =
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f).setDuration(ANIMATION_DURATION)

            scaleX.repeatCount = 1
            scaleX.repeatMode = ObjectAnimator.REVERSE
            scaleY.repeatCount = 1
            scaleY.repeatMode = ObjectAnimator.REVERSE
            alpha.repeatCount = 1
            alpha.repeatMode = ObjectAnimator.REVERSE

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY, alpha)
            animatorSet.start()
        }
    }
}