package cn.com.mustache.plugins.mybatis.link.util

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.NAMESPACE
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.POUND_SEPARATOR
import cn.com.mustache.plugins.mybatis.link.element.IdDomElement
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomService
import com.intellij.util.xml.DomUtil
import org.apache.commons.lang3.StringUtils
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * @author Steven
 */
object PsiUtil {
    private fun findMethod(project: Project, clazzName: String?, methodName: String?): Optional<PsiMethod> {
        if (StringUtils.isBlank(clazzName) || StringUtils.isBlank(methodName)) {
            return Optional.empty()
        }
        val clazz = findClazz(project, clazzName!!)
        return clazz.flatMap { c: PsiClass ->
            Optional.of(c.findMethodsByName(methodName, true))
                .filter { methods: Array<PsiMethod> -> methods.isNotEmpty() }
                .map { methods: Array<PsiMethod> -> methods[0] }
        }
    }

    private fun findMethod(project: Project, element: IdDomElement): Optional<PsiMethod> {
        return findMethod(project, MapperUtil.getNamespace(element), MapperUtil.getId(element))
    }

    private fun findClazz(project: Project, clazzName: String): Optional<PsiClass> {
        return Optional.ofNullable(
            JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project))
        )
    }

    fun isElementWithinInterface(element: PsiElement?): Boolean {
        if (element is PsiClass && element.isInterface) {
            return true
        }
        val type = PsiTreeUtil.getParentOfType(element, PsiClass::class.java)
        return Optional.ofNullable(type).map { obj: PsiClass -> obj.isInterface }.orElse(false)
    }

    fun isTheElement(element: PsiElement): Boolean {
        return (element is XmlToken
                && MapperUtil.isElementWithinMybatisFile(element)
                && isTargetType(element))
    }

    private fun isTargetType(token: XmlToken): Boolean {
        val tokenText = token.text
        if (MAPPER == tokenText && token.nextSibling is PsiWhiteSpace) {
            return true
        }
        if (LinkConstant.TARGET_TYPES.contains(tokenText) && token.parent is XmlTag) {
            val nextSibling = token.nextSibling
            return nextSibling is PsiWhiteSpace
        }
        return false
    }

    fun findTarget(from: XmlToken): Optional<out Any>? {
        val domElement = DomUtil.getDomElement(from)
            ?: return Optional.empty()
        if (domElement is IdDomElement) {
            return findMethod(from.project, domElement)
        }
        val xmlTag = domElement.xmlTag ?: return Optional.empty()
        val clazzName = xmlTag.getAttributeValue(NAMESPACE)
        if (StringUtils.isEmpty(clazzName)) {
            return Optional.empty()
        }
        return clazzName?.let { findClazz(from.project, it) }
    }

    fun getTooltip(target: PsiElement): String {
        var text: String? = null
        if (target is PsiMethod) {
            val psiClass = target.containingClass
            if (psiClass != null) {
                text = psiClass.name + POUND_SEPARATOR + target.name
            }
        }
        if (text == null && target is PsiClass) {
            text = target.qualifiedName
        }
        if (text == null) {
            text = target.containingFile.text
        }
        return "${LinkConstant.STATEMENT_TOOLTIP_TITLE}: $text"
    }

    fun isMybatisFile(file: PsiFile?): Boolean {
        if (file == null || (file is XmlFile && file.rootTag == null)) {
            return false
        }
        return isXmlFile(file) && (file as XmlFile).rootTag?.name == MAPPER
    }

    @NotNull
    @NonNls
    inline fun <reified T : DomElement> findDomElements(@NotNull project: Project): Collection<T> {
        val scope = GlobalSearchScope.allScope(project)
        val elements = DomService.getInstance().getFileElements(T::class.java, project, scope)
        return elements.map { it.rootElement }.toList()
    }

    private fun isXmlFile(@NotNull file: PsiFile): Boolean {
        return file is XmlFile
    }
}
