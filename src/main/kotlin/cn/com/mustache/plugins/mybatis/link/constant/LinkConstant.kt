package cn.com.mustache.plugins.mybatis.link.constant

import com.google.common.collect.ImmutableSet
import com.intellij.psi.util.ReferenceSetBase

/**
 * @author Steven
 */
class LinkConstant {
    companion object {
        const val MAPPER: String = "mapper"

        const val NAMESPACE: String = "namespace"

        val TARGET_TYPES: ImmutableSet<String> = ImmutableSet.of(
            "select",
            "update",
            "insert",
            "delete"
        )

        const val MAPPER_TOOLTIP_TITLE: String = "Navigation to Target in Mapper Xml"

        const val STATEMENT_TOOLTIP_TITLE: String = "Navigation to Target"

        const val DOT_SEPARATOR: String = ReferenceSetBase.DOT_SEPARATOR.toString()

        const val POUND_SEPARATOR: String = "#"

        const val EMPTY_STRING: String = ""

    }
}