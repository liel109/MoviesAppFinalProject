package com.example.mymovies.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
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

        fun scaleInAnimation(window: Window, pressedLocation : IntArray, displaySize : IntArray){
            val centerX = displaySize[0] / 2
            val centerY = displaySize[1] / 2
            val translationX = pressedLocation[0] - centerX
            val translationY = pressedLocation[1] - centerY
            val translateAnimatorX = ObjectAnimator.ofFloat(window.decorView, "translationX", translationX.toFloat() ,0f)
            val translateAnimatorY = ObjectAnimator.ofFloat(window.decorView, "translationY", translationY.toFloat(), 0f)
            val scaleAnimatorX = ObjectAnimator.ofFloat(window.decorView, "scaleX", 0.95f, 1f)
            val scaleAnimatorY = ObjectAnimator.ofFloat(window.decorView, "scaleY", 0.2f, 1f)
            val alphaAnimator = ObjectAnimator.ofFloat(window.decorView, "alpha", 1f, 1f)

            val animatorSet = AnimatorSet().apply {
                playTogether(translateAnimatorX, translateAnimatorY, scaleAnimatorX, scaleAnimatorY, alphaAnimator)
                duration = Constants.POP_ANIMATION_DURATION
            }
            animatorSet.start()
        }

        fun parseMovieLength(length : Int, context : Context) : String{
            return "${length/60}${context.getString(R.string.length_hours)} ${length%60}${context.getString(R.string.length_minutes)}"
        }

        fun calculateStars(rating : Double) : Int{
            return (rating / 2 + 1).toInt()
        }

        fun getHeartPhoto(isFavorite : Boolean) : Int{
            if(isFavorite){
                return R.drawable.ic_full_heart
            }
            return R.drawable.ic_empty_heart
        }

        fun parseReleaseDate(releaseDate : String) : String{
            return releaseDate.substring(0,4)
        }

        fun showNoConnectionToast(context: Context){
            Toast.makeText(context, context.getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
        }
    }
}