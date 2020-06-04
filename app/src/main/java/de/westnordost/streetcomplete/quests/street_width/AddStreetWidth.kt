package de.westnordost.streetcomplete.quests.street_width

import de.westnordost.osmapi.map.data.BoundingBox
import de.westnordost.osmapi.map.data.Element
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.elementgeometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.osmquest.OsmElementQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.data.tagfilters.FiltersParser
import de.westnordost.streetcomplete.data.tagfilters.getQuestPrintStatement
import de.westnordost.streetcomplete.data.tagfilters.toGlobalOverpassBBox

class AddStreetWidth(private val overpassApi: OverpassMapDataAndGeometryApi) : OsmElementQuestType<StreetWidthAnswer> {

    private val wayFilter by lazy { FiltersParser().parse("""
        ways with
          highway ~ tertiary|tertiary_link|unclassified|residential|living_street|road
        and !width:carriageway
    """)}

    override val commitMessage = "Add street width heights"
    override val wikiLink = "Key:width"
    override val icon = R.drawable.ic_quest_street_width

    override fun getTitle(tags: Map<String, String>): Int {
        return  R.string.quest_roadwidth_title
    }

    override fun isApplicableTo(element: Element) = wayFilter.matches(element)

    override fun download(bbox: BoundingBox, handler: (element: Element, geometry: ElementGeometry?) -> Unit): Boolean {
        return overpassApi.query(getWayOverpassQuery(bbox), handler)
    }

    private fun getWayOverpassQuery(bbox: BoundingBox) =
        bbox.toGlobalOverpassBBox() + "\n" + wayFilter.toOverpassQLString() + "\n" + getQuestPrintStatement()

    override fun createForm() = AddStreetWidthForm()

    override fun applyAnswerTo(answer: StreetWidthAnswer, changes: StringMapChangesBuilder) {
        changes.add("width:carriageway", answer.value.toString())
    }
}
