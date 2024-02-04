package cn.com.mustache.plugins.mybatis.link.provider

import cn.com.mustache.plugins.mybatis.link.constant.Icons
import cn.com.mustache.plugins.mybatis.link.util.isMybatisXmlToken
import cn.com.mustache.plugins.mybatis.link.util.targetMethodOrClass
import cn.com.mustache.plugins.mybatis.link.util.tooltipText
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.xml.XmlToken

/**
 * @author Steven
 * Provides line markers for MyBatis XML tokens in the editor.
 *
 */
class StatementLineMarkerProvider : LineMarkerProvider {

    /**
     * Gets line marker information for the specified [element].
     *
     * @param element The PSI element to analyze.
     * @return The line marker information or `null` if no line marker is needed.
     */
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!element.isMybatisXmlToken()) return null

        val target = (element as XmlToken).targetMethodOrClass() ?: return null

        if (target.isEmpty || target.get() !is PsiMethod) {
            return null
        }
        val psiElement = target.get() as PsiElement;
        val builder =
            NavigationGutterIconBuilder.create(Icons.STATEMENT_LINE_MARKER_ICON)
                .setTargets(psiElement)
                .setTooltipText(psiElement.tooltipText())
        return builder.createLineMarkerInfo(element)
    }

    /**
     * Collects slow line markers for a batch of PSI elements.
     *
     * @param elements The list of PSI elements to analyze.
     * @param result The collection to which line markers should be added.
     */
    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        super.collectSlowLineMarkers(elements, result)
    }


}