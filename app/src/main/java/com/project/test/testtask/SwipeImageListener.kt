package com.project.test.testtask

import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.jvm.internal.Ref

class SwipeImageListener(val firstImageView: ImageView, val secondImageView: ImageView, imagesArray: Array<Bitmap>?, imageIndex: Ref.IntRef) {
    private val layoutParams = firstImageView.layoutParams as ViewGroup.MarginLayoutParams
    private val initX = firstImageView.x + layoutParams.leftMargin
    private val initY = firstImageView.y + layoutParams.topMargin
    private var startEventX = 0F
    private var startEventY = 0F
    private var dx = 0F
    private var dy = 0F

    var index = imageIndex
    var directionY: Boolean? = null
    var imagesArray: Array<Bitmap>? = imagesArray
        set(value) {
            field = value

            firstImageView.setImageBitmap(value?.get(index.element))
            secondImageView.setImageBitmap(value?.get(index.element.inc()))

        }


    fun onTouch(view: View?, event: MotionEvent?): Boolean =
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (view != null) {
                        dx = view.x - event.rawX
                        dy = view.y - event.rawY
                        startEventX = event.rawX
                        startEventY = event.rawY
                    }
                    Log.wtf("kikong", "down")
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val eventX = event.rawX
                    val eventY = event.rawY


                    if (directionY == null) {
                        Log.wtf("kikong", "x before if${eventX - startEventX}")
                        Log.wtf("kikong", "y before if${eventY - startEventY}")
                        if (eventX > startEventX || eventY > startEventY) {
                            Log.wtf("kikong", " in if${eventX - startEventX}")
                            Log.wtf("kikong", "in if${eventY - startEventY}")
                            directionY = (eventY - startEventY) > (eventX - startEventX)
                        }

                    } else {
                        if (directionY!!) {
                            val newY = if (eventY > startEventY) {
                                eventY + dy
                            } else {
                                initY
                            }
                            view?.animate()?.y(newY)?.alpha((view.height - newY) / (view.height).toLong())?.setDuration(0)?.start()
                            Log.wtf("kikong", "move")

                        } else {
                            val newX = if (eventX > startEventX) {
                                eventX + dx
                            } else {
                                initX
                            }
                            view?.animate()?.x(newX)?.alpha((view.width - newX) / (view.width).toLong())?.setDuration(0)?.start()
                            Log.wtf("kikong", "startX:$startEventX eventX:$eventX")
                            Log.wtf("kikong", "move")

                        }
                    }

                    true
                }
                MotionEvent.ACTION_UP -> {
                    val eventX = event.rawX
                    val eventY = event.rawY
                    if (directionY != null) {
                        if (directionY!!) {
                            if ((eventY - startEventY).toDouble() >= view!!.height * 0.5) {
                                showNextImage()
                            }
                        } else {
                            if ((eventX - startEventX) >= view!!.width * 0.5) {
                                showNextImage()
                            }
                        }
                        view.animate()?.x(initX)?.y(initY)?.alpha(1F)?.setDuration(0)?.start()
                        directionY = null
                        Log.wtf("kikong", "up")
                    }
                    true
                }
                else -> false
            }

    private fun showNextImage() {
        nextIndex()
        firstImageView.setImageBitmap(imagesArray?.get(index.element))
        secondImageView.setImageBitmap(imagesArray?.get(index.element.inc()))
    }

    private fun nextIndex() {
        index.element = index.element.inc()
        if (index.element == imagesArray?.size ?: 0) {
            index.element = 0
        }

    }


}
