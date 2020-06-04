package de.westnordost.streetcomplete.quests.parking_lane

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.data.osm.osmquest.SimpleOverpassQuestType

class AddParkingLane(private val overpassApi: OverpassMapDataAndGeometryApi)  : SimpleOverpassQuestType<ParkingLaneAnswer>(overpassApi) {


    override val tagFilters =
            "ways with highway ~ tertiary|residential|unclassified and !parking:lane:both and !parking:lane:left and !parking:lane:right"
    override val commitMessage = "Add whether there is a parking lane"
    override val wikiLink = "Key:parking:lane"
    override val icon = R.drawable.ic_quest_parking

    override fun getTitle(tags: Map<String, String>) = R.string.quest_parking_lane_title

    override fun createForm() = AddParkingLaneForm()

    override fun applyAnswerTo(answer: ParkingLaneAnswer, changes: StringMapChangesBuilder) {
        if (answer.left && answer.right) {
            changes.add("parking:lane:both", "parallel")
        } else if (answer.left && !answer.right) {
            changes.add("parking:lane:left", "parallel");
            changes.add("parking:lane:right", "no_parking");
        } else if (!answer.left && answer.right) {
            changes.add("parking:lane:left", "no_parking");
            changes.add("parking:lane:right", "parallel");
        } else {
            changes.add("parking:lane:both", "no_parking")
        }
    }


}
