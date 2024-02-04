package cn.com.mustache.plugins.mybatis.link.util

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER_FILE_TYPE
import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER_FILE_NAMESPACE
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
 *
 * Utility methods for working with PsiElements and MyBatis related functionality.
 */
object PsiUtil {

    /**
     * Finds a method in the specified class and with the given method name.
     *
     * @param project The IntelliJ project.
     * @param clazzName The fully qualified class name.
     * @param methodName The name of the method to find.
     * @return An Optional containing the found PsiMethod, or empty if not found.
     */
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

    /**
     * Finds a method in the specified class and with the same name as the one in the provided IdDomElement.
     *
     * @param project The IntelliJ project.
     * @param element The IdDomElement containing information about the method.
     * @return An Optional containing the found PsiMethod, or empty if not found.
     */
    fun findMethod(project: Project, element: IdDomElement): Optional<PsiMethod> {
        return findMethod(project, element.namespace(), MapperUtil.getIdRawText(element))
    }

    /**
     * Finds a PsiClass by its fully qualified name.
     *
     * @param project The IntelliJ project.
     * @param clazzName The fully qualified class name.
     * @return An Optional containing the found PsiClass, or empty if not found.
     */
    fun findClazz(project: Project, clazzName: String): Optional<PsiClass> {
        return Optional.ofNullable(
            JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project))
        )
    }

    /**
     * Finds all DomElements of a specific type in the project.
     *
     * @param project The IntelliJ project.
     * @return A collection of found DomElements.
     */
    @NotNull
    @NonNls
    inline fun <reified T : DomElement> findDomElements(@NotNull project: Project): Collection<T> {
        val scope = GlobalSearchScope.allScope(project)
        val elements = DomService.getInstance().getFileElements(T::class.java, project, scope)
        return elements.map { it.rootElement }.toList()
    }
}

/**
 * Retrieves the target method or class associated with the current [XmlToken].
 *
 * @return An [Optional] containing the target method or class, or empty if not found.
 */
fun XmlToken.targetMethodOrClass(): Optional<out Any>? {
    val domElement = DomUtil.getDomElement(this)
        ?: return Optional.empty()
    if (domElement is IdDomElement) {
        return PsiUtil.findMethod(this.project, domElement)
    }
    val xmlTag = domElement.xmlTag ?: return Optional.empty()
    val clazzName = xmlTag.getAttributeValue(MAPPER_FILE_NAMESPACE)
    if (StringUtils.isEmpty(clazzName)) {
        return Optional.empty()
    }
    return clazzName?.let { PsiUtil.findClazz(this.project, it) }
}

/**
 * Checks if the current [PsiFile] is a MyBatis file.
 *
 * @return `true` if it is a MyBatis file, `false` otherwise.
 */
fun PsiFile?.isMybatisFile(): Boolean {
    if (this == null || (this is XmlFile && this.rootTag == null)) {
        return false
    }
    return this.isXmlFile() && (this as XmlFile).rootTag?.name == MAPPER_FILE_TYPE
}

/**
 * Checks if the current [PsiFile] is an XML file.
 *
 * @return `true` if it is an XML file, `false` otherwise.
 */
private fun PsiFile.isXmlFile(): Boolean {
    return this is XmlFile
}

/**
 * Generates tooltip text for the current [PsiElement].
 *
 * @return The tooltip text.
 */
fun PsiElement.tooltipText(): String {
    var tooltipText: String? = null
    if (this is PsiMethod) {
        val psiClass = this.containingClass
        if (psiClass != null) {
            tooltipText = psiClass.name + POUND_SEPARATOR + this.name
        }
    }
    if (tooltipText == null && this is PsiClass) {
        tooltipText = this.qualifiedName
    }
    if (tooltipText == null) {
        tooltipText = this.containingFile.text
    }
    return "${LinkConstant.STATEMENT_TOOLTIP_TITLE}: $tooltipText"
}

/**
 * Checks if the current [PsiElement] is within an interface.
 *
 * @return `true` if the element is within an interface, `false` otherwise.
 */
fun PsiElement?.isElementWithinInterface(): Boolean {
    if (this is PsiClass && this.isInterface) {
        return true
    }
    val type = PsiTreeUtil.getParentOfType(this, PsiClass::class.java)
    return Optional.ofNullable(type).map { psiClass: PsiClass -> psiClass.isInterface }.orElse(false)
}

/**
 * Checks if the current [PsiElement] is a MyBatis XML token.
 *
 * @return `true` if it is a MyBatis XML token, `false` otherwise.
 */
fun PsiElement.isMybatisXmlToken(): Boolean {
    return (this is XmlToken
            && this.elementInMybatisFile()
            && this.isMybatisTokenType())
}

/**
 * Checks if the current [XmlToken] is of a specific MyBatis token type.
 *
 * @return `true` if it is a MyBatis token, `false` otherwise.
 */
private fun XmlToken.isMybatisTokenType(): Boolean {
    val tokenText = this.text
    if (MAPPER_FILE_TYPE == tokenText && this.nextSibling is PsiWhiteSpace) {
        return true
    }
    if (LinkConstant.TARGET_TYPES.contains(tokenText) && this.parent is XmlTag) {
        val nextSibling = this.nextSibling
        return nextSibling is PsiWhiteSpace
    }
    return false
}
