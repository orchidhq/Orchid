package com.eden.orchid.api.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream

interface ResourceSpec : OptionsHolder {
    fun getResources(): Stream<OrchidResource>
}

class ContextResourceSpec(
    val context: OrchidContext
) : ResourceSpec {

    @Option
    lateinit var baseDir: String

    @Option
    lateinit var fileExtensions: Array<String>

    @Option
    var recursive: Boolean = false

    override fun getResources(): Stream<OrchidResource> {
        return context.getResourceEntries(baseDir, fileExtensions, recursive).stream()
    }
}
//
//fun OrchidGenerator.source(
//    vararg sources: ResourceSpec,
//    creator: (OrchidResource) -> OrchidPage
//): () -> Stream<OrchidPage> {
//    return {
//        sources
//            .toList()
//            .stream()
//            .flatMap(ResourceSpec::getResources)
//            .map(creator)
//    }
//}


interface GeneratorModel {
    fun getAllPages(): List<OrchidPage>
    fun getAllCollections(): List<OrchidCollection<*>>
}


//fun OrchidGenerator.collect(
//    creator: (List<OrchidPage>) -> OrchidPage
//): () -> Stream<OrchidCollection<*>> {
//    return {
//        creator
//    }
//}



//fun OrchidGenerator.index(context: OrchidContext): Model {
//    val pages = startIndexing()
//
//    val collections = if (!EdenUtils.isEmpty(pages)) {
//        listOf(FileCollection(this, this.key, pages))
//    } else emptyList()
//
//    return Model(this, { pages }, { collections })
//}
//
//class Model(
//    private val generator: OrchidGenerator,
//    private val pages: () -> List<OrchidPage>,
//    private val collections: () -> List<OrchidCollection<*>>
//)