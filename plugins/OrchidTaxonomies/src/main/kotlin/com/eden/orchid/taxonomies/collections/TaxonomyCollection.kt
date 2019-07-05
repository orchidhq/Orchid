package com.eden.orchid.taxonomies.collections

//@Description("A Taxonomy Collection represents all the pages that have a given term value. The 'itemId' matches a " +
//        "specific term, while the 'collectionId' represents the taxonomy the term is from."
//)
//open class TaxonomyCollection(
//        val taxonomy: Taxonomy)
//: OrchidCollection<OrchidPage>("taxonomy", taxonomy.key, ArrayList()) {
//
//    override fun find(id: String): Stream<OrchidPage> {
//        return taxonomy
//                .terms
//                .values
//                .stream()
//                .filter { term: Term -> term.key == id }
//                .map { term: Term -> term.landingPage }
//    }
//
//}
