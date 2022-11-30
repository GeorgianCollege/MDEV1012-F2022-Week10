package ca.georgiancollege.mdev1012_f2022_week10

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import java.lang.Math.abs

open class OnCustomTouchListener(context: Context?): OnTouchListener
{
    private val gestureDetector: GestureDetector

    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean
    {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    inner class GestureListener: SimpleOnGestureListener() {
        private val SWIPE_LENGTH_HORIZONTAL_THRESHOLD: Int = 50
        private val SWIPE_VELOCITY_THRESHOLD: Int = 50
        private val DEBUG_TAG = "Gestures"

        override fun onDown(e: MotionEvent): Boolean
        {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean
        {
            onClick()
            Log.d(DEBUG_TAG, "onSingleTapConfirmed")
            return super.onSingleTapUp(e)
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean
        {
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY))
                {
                    if (abs(diffX) > SWIPE_LENGTH_HORIZONTAL_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            Log.d(DEBUG_TAG, "onSwipeRight")
                            onSwipeRight()
                        }
                        else {
                            Log.d(DEBUG_TAG, "onSwipeLeft")
                            onSwipeLeft()
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }

    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    open fun onClick() {}
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

}