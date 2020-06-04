package de.westnordost.streetcomplete.quests.street_width

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.ktx.allowOnlyNumbers
import de.westnordost.streetcomplete.ktx.numberOrNull
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.quests.street_width.HeightMeasurementUnit.FOOT_AND_INCH
import de.westnordost.streetcomplete.quests.street_width.HeightMeasurementUnit.METER
import de.westnordost.streetcomplete.util.TextChangedWatcher

class AddStreetWidthForm : AbstractQuestFormAnswerFragment<StreetWidthAnswer>() {


    private var meterInput: EditText? = null
    private var feetInput: EditText? = null
    private var inchInput: EditText? = null
    private var heightUnitSelect: Spinner? = null
    private var meterInputSign: View? = null
    private var feetInputSign: View? = null

    private val heightUnits get() = listOf(METER)

    override fun isFormComplete() = getHeightFromInput() != null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        setMaxHeightSignLayout(R.layout.quest_maxheight, heightUnits.first())
        return view
    }

    private fun setMaxHeightSignLayout(resourceId: Int, unit: HeightMeasurementUnit) {
        val contentView = setContentView(resourceId)

        meterInput = contentView.findViewById(R.id.meterInput)
        feetInput = contentView.findViewById(R.id.feetInput)
        inchInput = contentView.findViewById(R.id.inchInput)

        val onTextChangedListener = TextChangedWatcher { checkIsFormComplete() }
        meterInput?.addTextChangedListener(onTextChangedListener)
        feetInput?.addTextChangedListener(onTextChangedListener)
        inchInput?.addTextChangedListener(onTextChangedListener)

        meterInputSign = contentView.findViewById(R.id.meterInputSign)
        feetInputSign = contentView.findViewById(R.id.feetInputSign)

        heightUnitSelect = contentView.findViewById(R.id.heightUnitSelect)
        heightUnitSelect?.visibility = if (heightUnits.size == 1) View.GONE else View.VISIBLE
        heightUnitSelect?.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_centered, heightUnits)
        heightUnitSelect?.setSelection(0)
        heightUnitSelect?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                switchLayout()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        inchInput?.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val destStr = dest.toString()
            val input = destStr.substring(0, dstart) + source.toString() + destStr.substring(dend, destStr.length)

            if(input.isEmpty() || input.toIntOrNull() != null && input.toInt() <= 12) null else ""
        })
        meterInput?.allowOnlyNumbers()
        switchLayout()
    }

    private fun switchLayout() {
        meterInputSign?.visibility =  View.VISIBLE
        feetInputSign?.visibility =  View.GONE

         meterInput?.requestFocus()
    }

    override fun onClickOk() {
        if (userSelectedUnrealisticHeight()) {
            confirmUnusualInput { applyMaxHeightFormAnswer() }
        } else {
            applyMaxHeightFormAnswer()
        }
    }

    private fun userSelectedUnrealisticHeight(): Boolean {
        val height = getHeightFromInput() ?: return false
        val m = height
        return m > 20 || m < 1.0
    }

    private fun applyMaxHeightFormAnswer() {
        applyAnswer(StreetWidthAnswer(getHeightFromInput()!!))
    }

    private fun getHeightFromInput(): Double? {
        when(heightUnitSelect?.selectedItem as HeightMeasurementUnit? ?: heightUnits.first()) {
            METER -> {
                return meterInput?.numberOrNull?.let { it }
            }
        }
        return null
    }


    private fun confirmUnusualInput(callback: () -> (Unit)) {
        activity?.let { AlertDialog.Builder(it)
            .setTitle(R.string.quest_generic_confirmation_title)
            .setMessage(R.string.quest_maxheight_unusualInput_confirmation_description)
            .setPositiveButton(R.string.quest_generic_confirmation_yes) { _, _ -> callback() }
            .setNegativeButton(R.string.quest_generic_confirmation_no, null)
            .show()
        }
    }
}
