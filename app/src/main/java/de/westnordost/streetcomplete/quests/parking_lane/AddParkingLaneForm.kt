package de.westnordost.streetcomplete.quests.parking_lane

import android.os.Bundle
import android.view.View
import androidx.annotation.AnyThread
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.elementgeometry.ElementPolylinesGeometry
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.quests.StreetSideRotater
import de.westnordost.streetcomplete.view.Item
import de.westnordost.streetcomplete.view.dialogs.ImageListPickerDialog
import kotlinx.android.synthetic.main.quest_street_side_puzzle.*

class AddParkingLaneForm : AbstractQuestFormAnswerFragment<ParkingLaneAnswer>() {

    override val contentLayoutResId = R.layout.quest_parking_lane_puzzle
    override val contentPadding = false

    private var streetSideRotater: StreetSideRotater? = null
    private var leftSide: ParallelParking? = null
    private var rightSide: ParallelParking? = null

    // just a shortcut
    private val isLeftHandTraffic get() = countryInfo.isLeftHandTraffic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getString(PARKING_RIGHT)?.let { rightSide = ParallelParking.valueOf(it) }
        savedInstanceState?.getString(PARKING_LEFT)?.let { leftSide = ParallelParking.valueOf(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        puzzleView.listener = { isRight -> showSidewalkSelectionDialog(isRight) }

        streetSideRotater = StreetSideRotater(puzzleView, compassNeedleView, elementGeometry as ElementPolylinesGeometry)

        val defaultResId =
            if (isLeftHandTraffic) R.drawable.ic_sidewalk_unknown_l
            else                   R.drawable.ic_sidewalk_unknown

        puzzleView.setLeftSideImageResource(leftSide?.puzzleResId ?: defaultResId)
        puzzleView.setRightSideImageResource(rightSide?.puzzleResId ?: defaultResId)

        checkIsFormComplete()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rightSide?.let { outState.putString(PARKING_RIGHT, it.name) }
        leftSide?.let { outState.putString(PARKING_LEFT, it.name) }
    }

    @AnyThread override fun onMapOrientation(rotation: Float, tilt: Float) {
        streetSideRotater?.onMapOrientation(rotation, tilt)
    }

    override fun onClickOk() {
        applyAnswer(ParkingLaneAnswer(
            left = leftSide == ParallelParking.YES,
            right = rightSide == ParallelParking.YES
        ))
    }

    override fun isFormComplete() = leftSide != null && rightSide != null

    override fun isRejectingClose() = leftSide != null || rightSide != null

    private fun showSidewalkSelectionDialog(isRight: Boolean) {
        val ctx = context ?: return

        val items = ParallelParking.values().map { it.asItem() }
        ImageListPickerDialog(ctx, items, R.layout.labeled_icon_button_cell, 2) { selected ->
            val sidewalk = selected.value!!
            if (isRight) {
                puzzleView.replaceRightSideImageResource(sidewalk.puzzleResId)
                rightSide = sidewalk
            } else {
                puzzleView.replaceLeftSideImageResource(sidewalk.puzzleResId)
                leftSide = sidewalk
            }
            checkIsFormComplete()
        }.show()
    }

    private enum class ParallelParking(val iconResId: Int, val puzzleResId: Int, val nameResId: Int) {
        NO(R.drawable.ic_sidewalk_no, R.drawable.ic_sidewalk_puzzle_no, R.string.quest_parallell_parking_no),
        YES(R.drawable.ic_parking_lane_yes, R.drawable.ic_parking_lane_puzzle_yes, R.string.quest_parallell_parking_yes);

        fun asItem() = Item(this, iconResId, nameResId)
    }

    companion object {
        private const val PARKING_LEFT = "parking_left"
        private const val PARKING_RIGHT = "parking_right"
    }
}
