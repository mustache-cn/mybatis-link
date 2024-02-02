package cn.com.mustache.plugins.mybatis.link.util

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.EMPTY_STRING
import cn.com.mustache.plugins.mybatis.link.element.IdDomElement
import cn.com.mustache.plugins.mybatis.link.element.Mapper
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlElement
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomUtil
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/**
 * @author Steven
 */
object MapperUtil {

    fun isElementWithinMybatisFile(@NotNull element: PsiElement): Boolean {
        val psiFile = element.containingFile
        return element is XmlElement && PsiUtil.isMybatisFile(psiFile)
    }

    @Nullable
    @NonNls
    fun <T : IdDomElement> getId(@NotNull domElement: T): String? {
        return domElement.id.rawText
    }

    @NotNull
    @NonNls
    fun getNamespace(@NotNull mapper: Mapper): String {
        val ns = mapper.getNamespace().stringValue
        return ns ?: EMPTY_STRING
    }

    @NotNull
    @NonNls
    fun getNamespace(@NotNull element: DomElement): String {
        return getNamespace(getMapper(element))
    }

    @NotNull
    @NonNls
    fun getMapper(@NotNull element: DomElement): Mapper {
        val optional = DomUtil.getParentOfType(element, Mapper::class.java, true)
        return optional ?: throw IllegalArgumentException("Unknown element")
    }

    @NotNull
    @NonNls
    fun findMappers(@NotNull project: Project): Collection<Mapper> {
        return PsiUtil.findDomElements(project)
    }

    @NotNull
    @NonNls
    fun <T : IdDomElement> getIdSignature(@NotNull domElement: T): String {
        return "${getNamespace(domElement)}.${getId(domElement) ?: EMPTY_STRING}"
    }

}
