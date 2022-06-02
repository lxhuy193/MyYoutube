package com.example.myyoutube.additionClass

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myyoutube.R


/**
 *
 * Created by Arman Chatikyan on 05 Sep 2018
 *
 */

class SingleViewTouchableMotionLayout(context: Context, attributeSet: AttributeSet? = null) : MotionLayout(context, attributeSet) {

    private val viewToDetectTouch by lazy {
        findViewById<View>(R.id.linearLayout) //TODO move to Attributes
    }
    private val viewRect = Rect()
    private var touchStarted = false
    private val transitionListenerList = mutableListOf<TransitionListener?>()

    init {
        addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                touchStarted = false
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        })

        super.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                transitionListenerList.filterNotNull()
                    .forEach { it.onTransitionChange(p0, p1, p2, p3) }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                transitionListenerList.filterNotNull()
                    .forEach { it.onTransitionCompleted(p0, p1) }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        })
    }

//    constructor(context: Context) : super(context)
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (!isInteractionEnabled) {
            return
        }

        if (target !is SwipeRefreshLayout) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val recyclerView = target.getChildAt(0)
        if (recyclerView !is RecyclerView) {
            return super.onNestedPreScroll(target, dx, dy, consumed, type)
        }

        val canScrollVertically = recyclerView.canScrollVertically(-1)
        if (dy < 0 && canScrollVertically) {
            // don't start motionLayout transition
            return;
        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun setTransitionListener(listener: TransitionListener?) {
        addTransitionListener(listener)
    }

    override fun addTransitionListener(listener: TransitionListener?) {
        transitionListenerList += listener
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            transitionToEnd()
            return false
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //gestureDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                touchStarted = false
                return super.onTouchEvent(event)
            }
        }
        if (!touchStarted) {
            viewToDetectTouch.getHitRect(viewRect)
            touchStarted = viewRect.contains(event.x.toInt(), event.y.toInt())
        }
        return touchStarted && super.onTouchEvent(event)
    }
}