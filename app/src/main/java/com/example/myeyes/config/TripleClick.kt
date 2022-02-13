package com.example.myeyes.config

import android.os.Handler
import android.view.View

open class TripleClick
/**
 * Builds a DoubleClick.
 *
 * @param tripleClickListener the click listener to notify clicks.
 */
    (/*
   * Click callback.
   */
    private val tripleClickListener: TripleClickListener
) : View.OnClickListener {

    /*
   * Handler to process click event.
   */
    private val mHandler = Handler()

    /*
   * Number of clicks in @DOUBLE_CLICK_INTERVAL interval.
   */
    private var clicks: Int = 0

    /*
   * Flag to check if click handler is busy.
   */
    private var isBusy = false

    override fun onClick(view: View) {

        if (!isBusy) {
            //  Prevent multiple click in this short time
            isBusy = true

            // Increase clicks count
            clicks++

            mHandler.postDelayed({

                if (clicks == 3) {
                    tripleClickListener.onTripleClick(view)
                }

                if (clicks == 2) {  // Double tap.
                    tripleClickListener.onDoubleClick(view)
                }

                if (clicks == 1) {  // Single tap
                    tripleClickListener.onSingleClick(view)
                }

                // we need to  restore clicks count
                clicks = 0
            }, DOUBLE_CLICK_INTERVAL)
            isBusy = false
        }

    }

    companion object {

        /*
   * Duration of click interval.
   * 200 milliseconds is a best fit to double click interval.
   */
        private const val DOUBLE_CLICK_INTERVAL = 500L  // Time to wait the second click.
    }
}

