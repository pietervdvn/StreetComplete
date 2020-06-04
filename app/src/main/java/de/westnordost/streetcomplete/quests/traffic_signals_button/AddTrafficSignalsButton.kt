package de.westnordost.streetcomplete.quests.traffic_signals_button

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.osmquest.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.quests.traffic_signal_red_duration.AddTrafficSignalsWaitDurationForm

class AddTrafficSignalsButton(o: OverpassMapDataAndGeometryApi) : SimpleOverpassQuestType<Int>(o) {

    override val tagFilters =
        "nodes with highway = traffic_signals and !traffic_signals:duration:wait"
    override val commitMessage = "add the wait time of traffic signals "
    override val wikiLink = "Tag:highway=traffic_signals"
    override val icon = R.drawable.ic_quest_traffic_lights

    override fun getTitle(tags: Map<String, String>) = R.string.quest_traffic_signals_wait_time_title

    override fun createForm() = AddTrafficSignalsWaitDurationForm()

    override fun applyAnswerTo(answer: Int, changes: StringMapChangesBuilder) {
        changes.add("traffic_signals:duration:wait", answer.toString())
    }
}
