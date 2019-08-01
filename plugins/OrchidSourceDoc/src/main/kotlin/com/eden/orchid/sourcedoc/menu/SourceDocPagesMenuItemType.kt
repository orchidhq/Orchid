package com.eden.orchid.sourcedoc.menu

//@Description("All Javadoc package pages.", name = "Javadoc Packages")
//class SourceDocPagesMenuItemType : OrchidMenuFactory("sourcedocPages") {
//
//    @Option
//    @StringDefault("All Packages")
//    lateinit var title: String
//
//    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
//        val model = context.resolve(JavadocModel::class.java)
//
//        val items = ArrayList<MenuItem>()
//        val pages = ArrayList<OrchidPage>(model.allPackages)
//        pages.sortBy { it.title }
//        items.add(
//            MenuItem.Builder(context)
//                .title(title)
//                .pages(pages)
//                .build()
//        )
//        return items
//    }
//
//}
