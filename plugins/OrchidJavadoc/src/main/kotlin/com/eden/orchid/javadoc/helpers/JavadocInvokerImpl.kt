package com.eden.orchid.javadoc.helpers

import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.name.Named
import com.sun.javadoc.RootDoc
import com.sun.tools.javac.util.Context
import com.sun.tools.javadoc.JavadocTool
import com.sun.tools.javadoc.Messager
import com.sun.tools.javadoc.ModifierFilter
import java.io.File
import java.util.Locale
import javax.inject.Inject
import javax.tools.JavaFileObject
import com.sun.tools.javac.util.List as JavacList

class JavadocInvokerImpl
@Inject
constructor(
        @Named("src") val resourcesDir: String
): JavadocInvoker {

    override fun getRootDoc(sourceDirs: List<String>): RootDoc? {
        try {
            return getJavadocTool().getRootDocImpl(
                    getLocale(),                        // var1, this.docenv.setLocale(var1);
                    getCharset(),                       // var2, this.docenv.setEncoding(var2);
                    getModifierFilter(),                // var3, this.docenv.showAccess = var3;
                    getJavaSourceFileNames(sourceDirs), // var4
                    getOptions(),                       // var5
                    getJavaSourceFileObjects(),         // var6
                    isBreakiterator(),                  // var7, this.docenv.breakiterator = var7;
                    getStuffList(),                     // var8
                    getThingList(),                     // var9
                    isDocClasses(),                     // var10, this.javadocReader.sourceCompleter = var10 ? null : this.thisCompleter;
                    useLegacyDoclet(),                  // var11, this.docenv.legacyDoclet = var11;
                    isQuiet()                           // var12, this.docenv.quiet = var12;
            )
        }
        catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }

    private fun getJavadocContext(): Context {
        val context = Context()

        Messager.preRegister(context, "orchid")

        return context
    }

    private fun getJavadocTool(): JavadocTool {
        return JavadocTool.make0(getJavadocContext())
    }

    private fun getLocale(): String {
        return Locale.getDefault().toString()
    }

    private fun getCharset(): String {
        return "UTF-8"
    }

    private fun getModifierFilter(): ModifierFilter {
        return ModifierFilter(5)
    }

    private fun getJavaSourceFileNames(sourceDirs: List<String>): JavacList<String> {
        var sourceFiles = JavacList.nil<String>()

        for(sourceDir in sourceDirs) {
            File("$resourcesDir/${OrchidUtils.normalizePath(sourceDir)}").absoluteFile
                    .walk()
                    .forEach {
                        if(it.isFile) {
                            sourceFiles = sourceFiles.append(it.absolutePath)
                        }
                    }
        }

        return sourceFiles
    }

    private fun getOptions(): JavacList<Array<String>> {
        return JavacList.nil()
    }

    private fun getJavaSourceFileObjects(): JavacList<JavaFileObject> {
        return JavacList.nil()
    }

    private fun isBreakiterator(): Boolean {
        return false
    }

    private fun getStuffList(): JavacList<String> {
        return JavacList.nil()
    }

    private fun getThingList(): JavacList<String> {
        return JavacList.nil()
    }

    private fun isDocClasses(): Boolean {
        return false
    }

    private fun useLegacyDoclet(): Boolean {
        return false
    }

    private fun isQuiet(): Boolean {
        return true
    }

}