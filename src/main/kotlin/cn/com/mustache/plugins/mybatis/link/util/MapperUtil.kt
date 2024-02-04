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
 * Utility methods for working with MyBatis mappers.
 */
object MapperUtil {

    /**
     * Gets the ID of the specified [domElement].
     *
     * @param domElement The DOM element.
     * @return The ID as a string, or `null` if not present.
     */
    @Nullable
    @NonNls
    fun <T : IdDomElement> getIdRawText(@NotNull domElement: T): String? {
        return domElement.id.rawText
    }

    /**
     * Gets the ID signature of the specified [domElement].
     *
     * @param domElement The DOM element.
     * @return The ID signature as a string.
     */
    @NotNull
    @NonNls
    fun <T : IdDomElement> getIdSignature(@NotNull domElement: T): String {
        return "${domElement.namespace()}.${getIdRawText(domElement) ?: EMPTY_STRING}"
    }

}

/**
 * Finds all MyBatis mappers in the current [Project].
 *
 * @return A collection of MyBatis mappers.
 */
@NotNull
@NonNls
fun Project.mappers(): Collection<Mapper> {
    return PsiUtil.findDomElements(this)
}

/**
 * Gets the namespace of the current [Mapper].
 *
 * @return The namespace as a string, or an empty string if not present.
 */
@NotNull
@NonNls
fun Mapper.namespace(): String {
    val ns = this.getNamespace().stringValue
    return ns ?: EMPTY_STRING
}

/**
 * Gets the namespace of the current [DomElement] by retrieving the namespace of its associated [Mapper].
 *
 * @return The namespace as a string.
 */
@NotNull
@NonNls
fun DomElement.namespace(): String {
    return this.mapper().namespace()
}

/**
 * Gets the [Mapper] associated with the current [DomElement].
 *
 * @return The MyBatis mapper.
 * @throws IllegalArgumentException if the element is not within a MyBatis mapper.
 */
@NotNull
@NonNls
fun DomElement.mapper(): Mapper {
    val optional = DomUtil.getParentOfType(this, Mapper::class.java, true)
    return optional ?: throw IllegalArgumentException("Unknown element")
}

/**
 * Checks if the [PsiElement] is within a MyBatis file.
 *
 * @return `true` if the element is within a MyBatis file, `false` otherwise.
 */
fun PsiElement.elementInMybatisFile(): Boolean {
    val psiFile = this.containingFile
    return this is XmlElement && psiFile.isMybatisFile()
}

