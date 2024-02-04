package cn.com.mustache.plugins.mybatis.link.provider

import cn.com.mustache.plugins.mybatis.link.constant.Icons
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER_TOOLTIP_TITLE
import cn.com.mustache.plugins.mybatis.link.services.LinkService
import cn.com.mustache.plugins.mybatis.link.util.isElementWithinInterface
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.CommonProcessors
import com.intellij.util.xml.DomElement
import org.jetbrains.uast.getUParentForIdentifier

/**
 * @author Steven
 *
 * Provides line markers for MyBatis mappers in the editor.
 */
class MapperLineMarkerProvider : RelatedItemLineMarkerProvider() {
    /**
     * Collects navigation markers for the specified [element].
     *
     * @param element The PSI element to analyze.
     * @param result The collection to which line markers should be added.
     */
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        val uParentForIdentifier = getUParentForIdentifier(element) ?: return

        val psiIdentifier = uParentForIdentifier.javaPsi ?: return
        if (psiIdentifier is PsiNameIdentifierOwner && psiIdentifier.isElementWithinInterface()) {
            val domElementProcessor = CommonProcessors.CollectProcessor<DomElement>()
            LinkService.getInstance(psiIdentifier.getProject()).process(psiIdentifier, domElementProcessor)
            val results = domElementProcessor.results
            if (results.isEmpty()) {
                return
            }
            val targets = results.stream().map { obj: DomElement -> obj.xmlTag }.toList()
            val builder =
                NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                    .setAlignment(GutterIconRenderer.Alignment.CENTER)
                    .setTargets(targets)
                    .setTooltipTitle(MAPPER_TOOLTIP_TITLE)
            result.add(builder.createLineMarkerInfo(psiIdentifier.nameIdentifier ?: return))
        }
    }
}