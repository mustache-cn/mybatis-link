package cn.com.mustache.plugins.mybatis.link.provider

import cn.com.mustache.plugins.mybatis.link.constant.Icons
import cn.com.mustache.plugins.mybatis.link.util.PsiUtil
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.xml.XmlToken

/**
 * @author Steven
 */
class StatementLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (!PsiUtil.isTheElement(element)) return null

        val target = PsiUtil.findTarget(element as XmlToken) ?: return null

        if (target.isEmpty || target.get() !is PsiMethod) {
            return null
        }
        val psiElement = target.get() as PsiElement;
        val builder =
            NavigationGutterIconBuilder.create(Icons.STATEMENT_LINE_MARKER_ICON)
                .setTargets(psiElement)
                .setTooltipText(PsiUtil.getTooltip(psiElement))
        return builder.createLineMarkerInfo(element)
    }


    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        super.collectSlowLineMarkers(elements, result)
    }


}