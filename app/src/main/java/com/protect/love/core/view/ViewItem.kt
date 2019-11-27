package com.protect.love.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.protect.love.extension.dp2i
import org.jetbrains.anko.*
import org.jetbrains.anko.internals.AnkoInternals.addView

const val TITLE_TYPE = 0
const val SWITCHITEM_TYPE = 1
const val CLICK_TYPE = 2


abstract class ViewItem(val title: String, val subTitle: String, val type: Int) {
    abstract fun createView(context: Context): View
}


const val ITEM_PADDING = 10f

/**
 * 标题条目
 */
class TitleItem(title: String) : ViewItem(title, "", TITLE_TYPE) {
    override fun createView(context: Context): View {
        return context.frameLayout {

            setPadding(
                context.dp2i(ITEM_PADDING),
                context.dp2i(5f),
                context.dp2i(5f),
                context.dp2i(5f)
            )
            textView {
                text = title
                textSize = 14f
                textColor = Color.parseColor("#A3A3A3")
            }.lparams {
                backgroundColor = Color.parseColor("#EDEDED")
                width = LinearLayout.LayoutParams.MATCH_PARENT
                height = LinearLayout.LayoutParams.WRAP_CONTENT
            }
        }
    }
}

/**
 * 条目点击view
 */
class ClickItem(title: String, subTitle: String, val click: (view: View) -> Unit) :
    ViewItem(title, subTitle, CLICK_TYPE) {
    companion object {
        val leftIcon by lazy {
            val data =
                "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAD30lEQVR4Xu2bS2sUQRDHq2CvfhU9CB48iKgnUQQJdLUa3/EVje8oImoIIUaNwbf4jA/s6iWCAcFLbjl48aKfww+QQ9iShon42ISZ6t7dzE7mXP+Zf/2mp7p7pgah4gdWPH9YAbAyAhIRYOZtALAdADYBQA0AZgFghojeJrpES06T5BFg5vcAsLuZQxF5ZK092RL3CU4aDcA514uIS95lEalba00Cv8lPEQVgampq9fz8/PecrpiIbM7YtoVFAfDe7xGRdwXcfiCipo9KgXMkDY0CwMyXAWC0iCMReW+t7S2iaWVsFADn3DFEfKIw+JaI9il0ySVRAJh5BwB8UrqaJKIDSm0yWRSA6enpVXNzczMisk7jCBFfGWMOabSpNFEAggnn3GZEnIkw9IKI+iL0UdJoABkEg4gc4eQZER2N0KulSQCEq3vvrYh80DpBxKfGmONavVaXDEAwwMxhjg/LYu3xmIj6tWKNLimAYKBer/c2Go2YDdBDIjqlSUajSQ4gqwn7EfG1xlDQIOJ9Y8xprb6IriUAsppwUEReFjHzT+w9IjoToc8lbRmArCYcBoDnuZw0CRKRCWvtOa0+j66lADIIYXp7msfMIjHjRHQhQr+ktOUAMghhensckcRtIhqM0C8qbQuArCb0i8hDbRKIOGaMCbvPpEfbAGSzwwAi3tNmICKj1torWn0zXVsBZI9DqOwTEUmMENHVCP1f0rYDyCCEyj6uTQIRh40x17T6P3UdAZBBuAgAt7RJIOKQMeaGVr+g6xiArDBeEpGb2iRE5Ii1Vr3OCNftKICsMF5BxBElhJ8AsIWI8r6Z/u8yHQeQjYSrIjKsgYCIu4wxTqNdFiNgwTgzh6I2VDQRRLxgjNEX1KIXbFW8FoCIHLDWTmp9lf4RqNVqa3p6en6UFoBzLqYIzhLRBm3yHa8B3vuoaRAAthLRl1ICYOaohZCIDFlry7kQ8t6fF5E72jtX6qWwc+4sIt7VJg8A5d0Mee8HRKSa22FmDm0yD7R3XkTGrLXlfCHCzCcA4JE2eQAo7ysxZq7uS1HnXB8iPtPe+VK/FvfeV/fDiHMu6tNYKJZENKAdOUV0yTdDzLwXAN4UMfFnbLsbK5MCiP08LiJPrLVhxmjbkQxApRsknHPVbZGpdJNU5dvkmHknAHxUVqzyN0pGbHC6o1XWez8YdmlFRkC3NUsXrf7d1S5fr9fXNhqNbzlHQPf9MBESZ+b7ALBkX1/X/jKzcOedczcQ8XqzkdDutX3O0fg7LNlS2Dm3HhFD1/dGEakh4lcA+FyJ3+aKUl9O8clGwHJKqoiXFQBFaHVj7C+KAIpQvkoP2wAAAABJRU5ErkJggg=="
            val decode = Base64.decode(data, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decode, 0, decode.size)
        }
    }


    @SuppressLint("ResourceType")
    override fun createView(context: Context): View {
        return context.linearLayout {
            orientation = LinearLayout.VERTICAL
            val dp8 = context.dp2i(8f)
            relativeLayout {
                padding = context.dp2i(ITEM_PADDING)

                textView {
                    text = title
                    textSize = 18f
                    textColor = Color.parseColor("#000000")
                    id = 10001
                }


                textView {
                    text = subTitle
                    textSize = 14f
                    textColor = Color.parseColor("#969696")

                }.lparams {
                    setMargins(0, dp8, 0, 0)
                    below(10001)
                }

                imageView {
                    imageBitmap = leftIcon
                }.lparams {
                    width = context.dp2i(15f)
                    height = context.dp2i(15f)
                    alignParentRight()
                    centerVertically()
                }

            }.lparams {
                width = LinearLayout.LayoutParams.MATCH_PARENT
                height = LinearLayout.LayoutParams.WRAP_CONTENT
            }

            imageView {
                backgroundColor = Color.parseColor("#F2F2F2")
            }.lparams {
                setMargins(context.dp2i(ITEM_PADDING), 0, 0, 0)
                width = LinearLayout.LayoutParams.MATCH_PARENT
                height = context.dp2i(1f)
            }
        }.apply {
            setOnClickListener {
                click(it)
            }
        }
    }
}


open  class SwitchItem(
    title: String,
    subTitle: String,
    val isChecked: Boolean

) : ViewItem(title, subTitle, SWITCHITEM_TYPE) {

    var checkedChangeListener: ((SwitchButton, Boolean) -> Unit)? = null

    var onClickListener: View.OnClickListener? = null

    @SuppressLint("ResourceType")
    override fun createView(context: Context): View {
        return context.linearLayout {
            orientation = LinearLayout.VERTICAL

            val dp8 = context.dp2i(8f)
            relativeLayout {
                padding = context.dp2i(ITEM_PADDING)

                textView {
                    text = title
                    textSize = 18f
                    textColor = Color.parseColor("#000000")
                    id = 10001
                }


                textView {
                    text = subTitle
                    textSize = 14f
                    textColor = Color.parseColor("#969696")

                }.lparams {
                    setMargins(0, dp8, 0, 0)
                    below(10001)
                }


                val switchButton = SwitchButton(context)
                switchButton.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
                switchButton.isChecked = isChecked
                if (checkedChangeListener != null) {
                    switchButton.setOnCheckedChangeListener { switchButton: SwitchButton, isChecked: Boolean ->
                        checkedChangeListener!!.invoke(switchButton, isChecked)
                    }
                }

                if (onClickListener != null) {
                    switchButton.setOnClickListener(onClickListener)
                }

                addView(switchButton)
            }.lparams {
                width = LinearLayout.LayoutParams.MATCH_PARENT
                height = LinearLayout.LayoutParams.WRAP_CONTENT
            }



            imageView {
                backgroundColor = Color.parseColor("#F2F2F2")
            }.lparams {
                setMargins(context.dp2i(ITEM_PADDING), 0, 0, 0)
                width = LinearLayout.LayoutParams.MATCH_PARENT
                height = context.dp2i(1f)
            }
        }
    }
}