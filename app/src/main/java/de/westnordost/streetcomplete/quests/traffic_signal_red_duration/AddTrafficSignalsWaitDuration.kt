package de.westnordost.streetcomplete.quests.postbox_ref

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.quest.NoCountriesExcept
import de.westnordost.streetcomplete.data.osm.osmquest.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.ktx.containsAny
import de.westnordost.streetcomplete.quests.traffic_signal_red_duration.AddTrafficSignalsWaitDurationForm

class AddTrafficSignalsWaitDuration(o: OverpassMapDataAndGeometryApi) : SimpleOverpassQuestType<Int>(o) {

    override val tagFilters = "nodes with highway = traffic_signals and !'traffic_signals:duration:wait'"

    override val icon = R.drawable.ic_quest_traffic_lights
    override val commitMessage = "Add traffic light durations"
    override val wikiLink = "Tag:traffic_signals"


    override fun getTitleArgs(tags: Map<String, String>, featureName: Lazy<String?>): Array<String> {
        val name = tags["name"] ?: tags["brand"] ?: tags["operator"]
        return if (name != null) arrayOf(name) else arrayOf()
    }

    override fun getTitle(tags: Map<String, String>): Int {
        return R.string.quest_traffic_signals_wait_time_title
    }

    override fun createForm() = AddTrafficSignalsWaitDurationForm()

    override fun applyAnswerTo(answer: Int, changes: StringMapChangesBuilder) {
       changes.add("traffic_signals:duration:wait",answer.toString())
    }
}
