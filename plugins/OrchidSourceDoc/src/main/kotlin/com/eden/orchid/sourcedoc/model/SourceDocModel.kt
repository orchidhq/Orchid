package com.eden.orchid.sourcedoc.model

import clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator

class SourceDocModel(
    val modules: List<SourceDocModuleModel>,
    override val collections: List<OrchidCollection<*>>
) : OrchidGenerator.Model {

    override val allPages by lazy {
        modules.flatMap { it.allPages }
    }

    companion object {
        fun getModel(context: OrchidContext, moduleType: String): SourceDocModel? {
            return context.resolve(SourceDocModel::class.java, moduleType) ?: context.resolve(SourceDocModel::class.java)
        }
        fun getModule(context: OrchidContext, moduleType: String, module: String): SourceDocModuleModel? {
            val model = getModel(context, moduleType)

            // no model, return early
            if (model == null) return null

            return if (model.modules.size > 1 && module.isNotBlank()) {
                model.modules.firstOrNull { it.name == module }
            } else if (model.modules.size == 1) {
                model.modules.single()
            } else {
                Clog.e("Cannot find module of type '{}' named '{}'", moduleType, module)
                null
            }
        }
    }
}
