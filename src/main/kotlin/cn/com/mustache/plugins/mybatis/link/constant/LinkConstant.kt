package cn.com.mustache.plugins.mybatis.link.constant

import com.google.common.collect.ImmutableSet
import com.intellij.psi.util.ReferenceSetBase

/**
 * @author Steven
 *
 * Constants
 */
class LinkConstant {
    companion object {
        /**
         * The file type for MyBatis Mapper XML files.
         */
        const val MAPPER_FILE_TYPE: String = "mapper"

        /**
         * The XML attribute representing the namespace in MyBatis Mapper files.
         */
        const val MAPPER_FILE_NAMESPACE: String = "namespace"

        /**
         * Set of target types in MyBatis (e.g., "select", "update", "insert", "delete").
         */
        val TARGET_TYPES: ImmutableSet<String> = ImmutableSet.of(
            "select",
            "update",
            "insert",
            "delete"
        )

        /**
         * Tooltip title for navigation to targets in Mapper XML.
         */
        const val MAPPER_TOOLTIP_TITLE: String = "Navigation to Target in Mapper Xml"

        /**
         * Tooltip title for navigation to general targets.
         */
        const val STATEMENT_TOOLTIP_TITLE: String = "Navigation to Target"

        /**
         * Separator for constructing concatenated strings (e.g., namespace + id).
         */
        const val DOT_SEPARATOR: String = ReferenceSetBase.DOT_SEPARATOR.toString()

        /**
         *  Separator used in tooltip text (e.g., class name + method name).
         */
        const val POUND_SEPARATOR: String = "#"

        /**
         * An empty string constant.
         */
        const val EMPTY_STRING: String = ""

    }
}