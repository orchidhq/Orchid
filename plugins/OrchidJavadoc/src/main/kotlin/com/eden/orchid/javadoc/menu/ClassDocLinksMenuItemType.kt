package com.eden.orchid.javadoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.models.JavadocModel
import com.eden.orchid.javadoc.pages.JavadocClassPage
import java.util.ArrayList

@Description(
    "Links to the different sections within a Javadoc Class page, optionally with their items nested " +
            "underneath them.",
    name = "Javadoc Class Sections"
)
class ClassDocLinksMenuItemType : OrchidMenuFactory("javadocClassLinks") {

    @Option
    @BooleanDefault(false)
    @Description(
        "Whether to include the items for each category. For example, including a menu item for each " +
                "individual constructor as children of 'Constructors' or just a link to the Constructors section."
    )
    var includeItems: Boolean = false

    override fun canBeUsedOnPage(
        containingPage: OrchidPage,
        menu: OrchidMenu,
        possibleMenuItems: List<Map<String, Any>>,
        currentMenuItems: List<OrchidMenuFactory>
    ): Boolean {
        return containingPage is JavadocClassPage
    }

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val model = context.resolve(JavadocModel::class.java)
        val containingPage = page as JavadocClassPage
        val classDoc = containingPage.classDoc

        val menuItems = ArrayList<MenuItem>()

        val linkData = arrayOf(
            LinkData(
                { true },
                { emptyList() },
                "Summary",
                "summary"
            ),
            LinkData(
                { true },
                { emptyList() },
                "Description",
                "description"
            ),
            LinkData(
                { classDoc.fields.isNotEmpty() },
                { getFieldLinks(context, model, page) },
                "Fields",
                "fields"
            ),
            LinkData(
                { classDoc.constructors.isNotEmpty() },
                { getConstructorLinks(context, model, page) },
                "Constructors",
                "constructors"
            ),
            LinkData(
                { classDoc.methods.isNotEmpty() },
                { getMethodLinks(context, model, page) },
                "Methods",
                "methods"
            )
        )

        for (item in linkData) {
            if (item.matches()) {
                val menuItem = MenuItem.Builder(context)
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
        val items: () -> List<MenuItem>,
        val title: String,
        val id: String
    )

    private fun getFieldLinks(context: OrchidContext, model: JavadocModel, page: OrchidPage): List<MenuItem> {
        val containingPage = page as JavadocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.fields.map {
            MenuItem.Builder(context)
                .title(it.simpleSignature)
                .anchor(model.idFor(it))
                .build()
        }
    }

    private fun getConstructorLinks(context: OrchidContext, model: JavadocModel, page: OrchidPage): List<MenuItem> {
        val containingPage = page as JavadocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.constructors.map {
            MenuItem.Builder(context)
                .title(it.simpleSignature)
                .anchor(model.idFor(it))
                .build()
        }
    }

    private fun getMethodLinks(context: OrchidContext, model: JavadocModel, page: OrchidPage): List<MenuItem> {
        val containingPage = page as JavadocClassPage
        val classDoc = containingPage.classDoc

        return classDoc.methods.map {
            MenuItem.Builder(context)
                .title(it.simpleSignature)
                .anchor(model.idFor(it))
                .build()
        }
    }

}
