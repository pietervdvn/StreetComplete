package de.westnordost.streetcomplete.quests.accepts_cash

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.quest.NoCountriesExcept
import de.westnordost.streetcomplete.data.osm.osmquest.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.quests.YesNoQuestAnswerFragment

class AddFixmeWithOption(o: OverpassMapDataAndGeometryApi) : SimpleOverpassQuestType<Boolean>(o) {


    override val tagFilters = """
        nodes, ways with
        fixme and fixme:option:1
    """
    override val commitMessage = "Add whether this place accepts cash as payment"
    override val wikiLink = "Key:payment"
    override val icon = R.drawable.ic_quest_cash



    override fun getTitle(tags: Map<String, String>) = R.string.quest_accepts_cash_title

    override fun createForm() = YesNoQuestAnswerFragment()

    override fun applyAnswerTo(answer: Boolean, changes: StringMapChangesBuilder) {
        changes.delete("fixme");
        changes.delete("fixme:option:1");
        changes.deleteIfExists("fixme:option:2");

        changes.add("payment:cash", if(answer) "yes" else "no")
    }
}
