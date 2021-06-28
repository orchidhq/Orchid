package com.eden.orchid.swiftdoc.swift

import clog.Clog
import com.eden.orchid.swiftdoc.swift.members.SwiftClassMethod
import com.eden.orchid.swiftdoc.swift.members.SwiftClassVar
import com.eden.orchid.swiftdoc.swift.members.SwiftCommentMark
import com.eden.orchid.swiftdoc.swift.members.SwiftEnum
import com.eden.orchid.swiftdoc.swift.members.SwiftEnumCase
import com.eden.orchid.swiftdoc.swift.members.SwiftInstanceMethod
import com.eden.orchid.swiftdoc.swift.members.SwiftInstanceVar
import com.eden.orchid.swiftdoc.swift.members.SwiftLocalVar
import com.eden.orchid.swiftdoc.swift.members.SwiftStaticMethod
import com.eden.orchid.swiftdoc.swift.members.SwiftStaticVar
import com.eden.orchid.swiftdoc.swift.members.SwiftStruct
import com.eden.orchid.swiftdoc.swift.members.SwiftSubscriptMethod
import org.json.JSONArray
import org.json.JSONObject

interface SwiftAttributes {

    var attributes: List<String>

    fun initAttributes(data: JSONObject) {
        attributes = data.optJSONArray("key.attributes")
                ?.map { it as JSONObject }
                ?.map { it.optString("key.attribute")?.replace("source.decl.attribute.", "") }
                ?.map { it?.replace("source.decl.attribute", "") }
                ?.filterNotNull() ?: emptyList()
    }
}

interface SwiftMembers {
    var members: List<SwiftMember>

    fun SwiftStatement.initMembers(data: JSONObject) {
        val arr = data.optJSONArray("key.substructure") ?: JSONArray()

        val statementMembers = ArrayList<SwiftMember>()

        arr.forEachIndexed { i, _ ->
            val o = arr.getJSONObject(i)

            var isHidden = false
            isHidden = isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.private")
//            isHidden = isHidden or (o.optString("key.accessibility") == "source.lang.swift.accessibility.internal")

            if(!isHidden) { // skip hidden/internal members
                val member: SwiftMember? = when (o.getString("key.kind")) {
                    "source.lang.swift.syntaxtype.comment.mark"       -> null
                    "source.lang.swift.decl.enum"                     -> SwiftEnum(context, o, this)
                    "source.lang.swift.decl.enumcase"                 -> SwiftEnumCase(context, o, this)
                    "source.lang.swift.decl.struct"                   -> SwiftStruct(context, o, this)
                    "source.lang.swift.decl.var.static"               -> SwiftStaticVar(context, o, this)
                    "source.lang.swift.decl.var.class"                -> SwiftClassVar(context, o, this)
                    "source.lang.swift.decl.var.instance"             -> SwiftInstanceVar(context, o, this)
                    "source.lang.swift.decl.var.local"                -> SwiftLocalVar(context, o, this)
                    "source.lang.swift.decl.function.method.static"   -> SwiftStaticMethod(context, o, this)
                    "source.lang.swift.decl.function.method.class"    -> SwiftClassMethod(context, o, this)
                    "source.lang.swift.decl.function.method.instance" -> SwiftInstanceMethod(context, o, this)
                    "source.lang.swift.decl.function.subscript"       -> SwiftSubscriptMethod(context, o, this)
                    else                                              -> {
                        Clog.e("found other member kind {}", o.getString("key.kind"))
                        null
                    }
                }

                if (member != null) {
                    member.init()
                    statementMembers.add(member)
                }
            }
        }

        members = statementMembers.sortedWith(compareBy({
            when(it) {
                is SwiftCommentMark -> 0
                is SwiftEnum -> 1
                is SwiftStruct -> 2

                is SwiftStaticVar -> 3
                is SwiftStaticMethod -> 4

                is SwiftClassVar -> 5
                is SwiftInstanceVar -> 6
                is SwiftLocalVar -> 7

                is SwiftClassMethod -> 8
                is SwiftInstanceMethod -> 9
                is SwiftSubscriptMethod -> 10

                else -> 0
            }
        }))
    }
}
