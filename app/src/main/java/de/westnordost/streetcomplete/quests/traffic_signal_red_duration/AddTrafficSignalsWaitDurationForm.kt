package de.westnordost.streetcomplete.quests.traffic_signal_red_duration

import android.os.Bundle
import android.view.View
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.util.TextChangedWatcher
import kotlinx.android.synthetic.main.quest_bike_parking_capacity.*

class AddTrafficSignalsWaitDurationForm : AbstractQuestFormAnswerFragment<Int>() {

    override val contentLayoutResId = R.layout.quest_traffic_signal_wait_duration

    private val capacity get() = capacityInput?.text?.toString().orEmpty().trim()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        capacityInput.addTextChangedListener(TextChangedWatcher { checkIsFormComplete() })
    }

    override fun isFormComplete() = capacity.isNotEmpty()

    override fun onClickOk() {
        applyAnswer(capacity.toInt())
    }
}
