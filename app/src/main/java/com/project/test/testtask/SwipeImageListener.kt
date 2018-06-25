package com.project.test.testtask

import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.jvm.internal.Ref

/*
Обработчик свайпов
firstImageView - ImageView, на который анимируется свайп
secondImageView - ImageView под firstImageView
imagesArray - массив показываемых изображений
imageIndex - ссылка на индес массива изображений
 */
class SwipeImageListener(val firstImageView: ImageView, val secondImageView: ImageView, imagesArray: Array<Bitmap>?, imageIndex: Ref.IntRef) {
    //Начальные координаты view на экране
    private val layoutParams = firstImageView.layoutParams as ViewGroup.MarginLayoutParams
    private val initX = firstImageView.x + layoutParams.leftMargin
    private val initY = firstImageView.y + layoutParams.topMargin
    //Начальные координаты касания
    private var startEventX = 0F
    private var startEventY = 0F
    //Различие event.rawX/Y и view.x/y
    private var dx = 0F
    private var dy = 0F
    //Индекс массива картинок
    private var index = imageIndex
    //Индикатор направления. True - вниз , false - вправо, null - нет направления
    private var directionY: Boolean? = null
    //Массив картинок
    var imagesArray: Array<Bitmap>? = imagesArray
    //При установке нового массива установить картинки на ImageView
        set(value) {
            field = value
            firstImageView.setImageBitmap(value?.get(index.element))
            secondImageView.setImageBitmap(value?.get(getNextIndex()))

        }


    fun onTouch(view: View?, event: MotionEvent?): Boolean =
            when (event?.action) {
            //Момент нажатия
                MotionEvent.ACTION_DOWN -> {
                    if (view != null) {
                        dx = view.x - event.rawX
                        dy = view.y - event.rawY
                        startEventX = event.rawX
                        startEventY = event.rawY
                    }
                    true
                }
            //Проведение пальца по экрану
                MotionEvent.ACTION_MOVE -> {
                    //Координаты нажатия
                    val eventX = event.rawX
                    val eventY = event.rawY

                    //Если направление не задано, задаётся
                    if (directionY == null) {
                        //Условие свайпа вниз или вправо
                        if (eventX > startEventX || eventY > startEventY) {
                            directionY = (eventY - startEventY) > (eventX - startEventX)
                        }

                    }
                    //Если направление задано
                    else {
                        //Если направление вниз
                        if (directionY!!) {
                            //Новое значение y для firstImageView если двигаем пальцем ниже startEventY, то задаётся новое значение иначе начальное
                            val newY = if (eventY > startEventY) {
                                eventY + dy
                            } else {
                                initY
                            }
                            //Анимация движения
                            view?.animate()?.y(newY)?.alpha((view.height - newY) / (view.height).toLong())?.setDuration(0)?.start()

                        } else {
                            //Новое значение x для firstImageView если двигаем пальцем правее startEventX, то задаётся новое значение иначе начальное
                            val newX = if (eventX > startEventX) {
                                eventX + dx
                            } else {
                                initX
                            }
                            //Анимация движения
                            view?.animate()?.x(newX)?.alpha((view.width - newX) / (view.width).toLong())?.setDuration(0)?.start()

                        }
                    }

                    true
                }
            //Момент поднятия пальца
                MotionEvent.ACTION_UP -> {
                    val eventX = event.rawX
                    val eventY = event.rawY
                    //Если задано направление
                    if (directionY != null) {
                        //Если направление вниз
                        if (directionY!!) {
                            //Проверка на граничное условие для смены картинки
                            if ((eventY - startEventY).toDouble() >= view!!.height * 0.5) {
                                showNextImage()
                            }
                        }
                        //Если направнеие вправо
                        else {
                            //Проверка на граничное условие для смены картинки
                            if ((eventX - startEventX) >= view!!.width * 0.5) {
                                showNextImage()
                            }
                        }
                        //Анимация возврата картинки в начальное положение
                        view.animate()?.x(initX)?.y(initY)?.alpha(1F)?.setDuration(0)?.start()
                        //Обнуление направления
                        directionY = null
                    }
                    true
                }
                else -> false
            }

    /*
    Метод переставляющий картинку с заднего ImageView на передний
     */
    private fun showNextImage() {
        setNextIndex()
        firstImageView.setImageBitmap(imagesArray?.get(index.element))
        secondImageView.setImageBitmap(imagesArray?.get(getNextIndex()))
    }

    /*
    Метод учеличения индеса массива картинок
     */
    private fun setNextIndex() {
        index.element = getNextIndex()
    }

    private fun getNextIndex() = if (index.element.inc() == imagesArray?.size) 0 else index.element.inc()


}
