package com.eden.orchid.javadoc

import com.eden.orchid.Orchid
import com.eden.orchid.StandardModule
import com.eden.orchid.api.options.OrchidFlags
import com.sun.javadoc.LanguageVersion
import com.sun.javadoc.RootDoc
import com.sun.javadoc.Tag
import com.sun.tools.doclets.standard.Standard
import java.util.Arrays
import java.util.stream.Collectors


/**
 * This is the main entry point to the Orchid build process. It does little more than create a OrchidContextImpl for
 * Orchid to runTask within, and then set that context into motion. It is the single point-of-entry for starting the
 * Orchid process; both Javadoc's `start` method and the Java `main` method are in here, which create the appropriate
 * OrchidContext and then runTask a single Orchid task.
 */
class OrchidJavadoc {
    companion object {

// Doclet hackery to allow this to parse documentation as expected and not crash...
//----------------------------------------------------------------------------------------------------------------------

        /**
         * Get the number of arguments that a given option expects from the command line. This number includes the option
         * itself: for example '-d /output/javadoc' should return 2.
         *
         * @param optionFlag the option to parse
         * @return the number of arguments it expects from the command line
         */
        @JvmStatic
        fun optionLength(optionFlag: String): Int {
            for (option in OrchidFlags.getInstance().flags) {
                if (optionFlag == "-" + option.flag) {
                    return option.optionLength()
                }
            }

            return Standard.optionLength(optionFlag)
        }

        /**
         * NOTE: Without this method present and returning LanguageVersion.JAVA_1_5,
         * Javadoc will not process generics because it assumes LanguageVersion.JAVA_1_1
         *
         * @return language version (hard coded to LanguageVersion.JAVA_1_5)
         */
        @JvmStatic
        fun languageVersion(): LanguageVersion {
            return LanguageVersion.JAVA_1_5
        }


// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

        @JvmStatic
        fun start(rootDoc: RootDoc): Boolean {
            val options: Map<String, Array<String>> = Arrays
                    .stream(rootDoc.options())
                    .collect(Collectors.toMap({ s -> s[0] }, { s -> s }) { key1, _ -> key1 })

            return Orchid.getInstance().start(
                    StandardModule.builder().flags(options).build(),
                    JavadocModule(rootDoc)
            )
        }

        @JvmStatic
        fun getText(tags: Array<Tag>): String {
            var text = ""
            for (tag in tags) {
                text += tag.text()
            }
            return text
        }
    }
}
