package codes.side.andcolorpicker.app.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import codes.side.andcolorpicker.app.R
import codes.side.andcolorpicker.converter.toColorInt
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.ColorSeekBar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.fragment_hsl_seekbar_recyclerview.*
import kotlinx.android.synthetic.main.layout_item_hsl_seekbar.view.*

class HslSeekBarRecyclerViewFragment : Fragment(R.layout.fragment_hsl_seekbar_recyclerview) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(
      view,
      savedInstanceState
    )

    recyclerView.layoutManager = LinearLayoutManager(
      requireContext()
    )

    val adapter = ItemAdapter.items<HSLItem>()
    val items = mutableListOf<HSLItem>()
    repeat(100) {
      items.add(
        HSLItem(
          IntegerHSLColor.createRandomColor(true).toColorInt()
        )
      )
    }
    adapter.add(items)

    recyclerView.adapter = FastAdapter.with(adapter)
  }

  class HSLItem(@ColorInt initialColor: Int) : AbstractItem<HSLItem.ViewHolder>() {
    private val hslColor = IntegerHSLColor().also {
      it.setFromColor(initialColor)
    }

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<HSLItem>(itemView) {
      private var lastBoundItem: HSLItem? = null

      init {
        itemView.hslSeekBar.addListener(
          object : HSLColorPickerSeekBar.DefaultOnColorPickListener() {
            override fun onColorChanged(
              picker: ColorSeekBar<IntegerHSLColor>,
              color: IntegerHSLColor,
              value: Int
            ) {
              lastBoundItem?.hslColor?.setFromHSLColor(color)
              colorize()
            }
          }
        )
      }

      override fun bindView(item: HSLItem, payloads: List<Any>) {
        lastBoundItem = item
        itemView.hslSeekBar.pickedColor = item.hslColor
        colorize()
      }

      override fun unbindView(item: HSLItem) {
        lastBoundItem = null
      }

      private fun colorize() {
        val hslColor = requireNotNull(lastBoundItem).hslColor.clone().also {
          it.floatL += 0.45f
        }
        itemView.cardView.setCardBackgroundColor(
          hslColor.toColorInt()
        )
      }
    }

    override val layoutRes: Int
      get() = R.layout.layout_item_hsl_seekbar
    override val type: Int
      get() = 0

    override fun getViewHolder(v: View): ViewHolder {
      return ViewHolder(v)
    }
  }
}
