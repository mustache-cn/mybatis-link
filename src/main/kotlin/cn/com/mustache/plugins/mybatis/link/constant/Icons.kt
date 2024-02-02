package cn.com.mustache.plugins.mybatis.link.constant

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon


/**
 * @author Steven
 */
interface Icons {
    companion object {
        val MAPPER_LINE_MARKER_ICON: Icon = IconLoader.getIcon("/images/mapper_method.svg", Icons::class.java)

        val STATEMENT_LINE_MARKER_ICON: Icon = IconLoader.getIcon("/images/statement.svg", Icons::class.java)
    }
}