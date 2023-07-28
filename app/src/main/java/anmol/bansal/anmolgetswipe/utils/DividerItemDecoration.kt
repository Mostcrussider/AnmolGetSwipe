package anmol.bansal.anmolgetswipe.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import anmol.bansal.anmolgetswipe.R

class DividerItemDecoration(context: Context, @ColorRes private val dividerColorResId: Int) :
    RecyclerView.ItemDecoration() {

    private val dividerPaint: Paint = Paint()
    private val marginStart: Float
    private val marginEnd: Float

    init {
        val resources = context.resources
        marginStart = resources.getDimension(R.dimen.dimens_8dp)
        marginEnd = resources.getDimension(R.dimen.dimens_8dp)
        val dividerColor = ContextCompat.getColor(context, dividerColorResId)
        dividerPaint.color = dividerColor
        dividerPaint.strokeWidth = 1f
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position < (parent.adapter?.itemCount ?: (0 - 1))) {
            outRect.set(0, 0, 0, dividerPaint.strokeWidth.toInt())
        } else {
            outRect.setEmpty()
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + marginStart
        val right = parent.width - parent.paddingRight - marginEnd

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin.toFloat()
            val bottom = top + dividerPaint.strokeWidth

            c.drawRect(left, top, right, bottom, dividerPaint)
        }
    }
}
