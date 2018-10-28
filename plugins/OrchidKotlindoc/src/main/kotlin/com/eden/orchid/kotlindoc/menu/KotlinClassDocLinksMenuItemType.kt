package com.eden.orchid.kotlindoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import javax.inject.Inject

@Description("Links to the different sections within a Kotlindoc Class page, optionally with their items nested " +
        "underneath them.",
        name = "Kotlindoc Class Sections"
)
class KotlinClassDocLinksMenuItemType
@Inject
constructor(
        context: OrchidContext,
        val model: KotlindocModel
) : OrchidMenuItem(context, "kotlindocClassLinks", 100) {

    @Option @BooleanDefault(false)
    @Description("Whether to include the items for each category. For example, including a menu item for each " +
            "individual constructor as children of 'Constructors' or just a link to the Constructors section."
    )
    var includeItems: Boolean = false

    override fun canBeUsedOnPage(containingPage: OrchidPage?, menu: OrchidMenu?, possibleMenuItems: List<Map<String, Any>>, currentMenuItems: MutableList<OrchidMenuItem>?): Boolean {
        return containingPage is KotlindocClassPage
    }

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val containingPage = page as KotlindocClassPage
        val classDoc = containingPage.classDoc

        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val linkData = arrayOf(
                LinkData({true},                                 { emptyList() },           "Summary",      "summary"),
                LinkData({true},                                 { emptyList() },           "Description",  "description"),
                LinkData({classDoc.fields.isNotEmpty()},         this::getFieldLinks,       "Fields",       "fields"),
                LinkData({classDoc.constructors.isNotEmpty()},   this::getConstructorLinks, "Constructors", "constructors"),
                LinkData({classDoc.methods.isNotEmpty()},        this::getMethodLinks,      "Methods",      "methods")
        )

        for(item in linkData) {
            if(item.matches()) {
                val menuItem = OrchidMenuItemImpl.Builder(context)
                        .title(item.title)
                        .anchor(item.id)

                if (includeItems) {
                    menuItem.children(item.items())
                }

                menuItems.add(menuItem.build())
            }
        }

        return menuItems
    }

    private data class LinkData(
            val matches: () -> Boolean,
            val items: () -> List<OrchidMenuItemImpl>,
            val title: String,
            val id: String
    )

    private fun getFieldLinks(): List<OrchidMenuItemImpl> {
        val containingPage = page as KotlindocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.fields.map {
            OrchidMenuItemImpl.Builder(context)
                    .title(it.simpleSignature)
                    .anchor(model.idFor(it))
                    .build()
        }
    }

    private fun getConstructorLinks(): List<OrchidMenuItemImpl> {
        val containingPage = page as KotlindocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.constructors.map {
            OrchidMenuItemImpl.Builder(context)
                    .title(it.simpleSignature)
                    .anchor(model.idFor(it))
                    .build()
        }
    }

    private fun getMethodLinks(): List<OrchidMenuItemImpl> {
        val containingPage = page as KotlindocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.methods.map {
            OrchidMenuItemImpl.Builder(context)
                    .title(it.simpleSignature)
                    .anchor(model.idFor(it))
                    .build()
        }
    }

}
