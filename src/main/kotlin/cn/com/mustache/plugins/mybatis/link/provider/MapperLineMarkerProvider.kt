package cn.com.mustache.plugins.mybatis.link.provider

import cn.com.mustache.plugins.mybatis.link.constant.Icons
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER_TOOLTIP_TITLE
import cn.com.mustache.plugins.mybatis.link.services.LinkService
import cn.com.mustache.plugins.mybatis.link.util.PsiUtil
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.CommonProcessors
import com.intellij.util.xml.DomElement
import org.jetbrains.uast.getUParentForIdentifier
import java.util.stream.Collectors

/**
 * @author Steven
 */
class MapperLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        val uElement = getUParentForIdentifier(element) ?: return

        val identifier = uElement.javaPsi ?: return
        if (identifier is PsiNameIdentifierOwner && PsiUtil.isElementWithinInterface(identifier)) {
            val processor = CommonProcessors.CollectProcessor<DomElement>()
            LinkService.getInstance(identifier.getProject()).process(identifier, processor)
            val results = processor.results
            if (results.isEmpty()) {
                return
            }
            val targets = results.stream().map { obj: DomElement -> obj.xmlTag }.collect(Collectors.toList())
            val builder =
                NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                    .setAlignment(GutterIconRenderer.Alignment.CENTER)
                    .setTargets(targets)
                    .setTooltipTitle(MAPPER_TOOLTIP_TITLE)
            result.add(builder.createLineMarkerInfo(identifier.nameIdentifier!!))
        }
    }
}