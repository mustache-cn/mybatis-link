package cn.com.mustache.plugins.mybatis.link.services

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.DOT_SEPARATOR
import cn.com.mustache.plugins.mybatis.link.element.IdDomElement
import cn.com.mustache.plugins.mybatis.link.element.Mapper
import cn.com.mustache.plugins.mybatis.link.util.MapperUtil
import cn.com.mustache.plugins.mybatis.link.util.mappers
import cn.com.mustache.plugins.mybatis.link.util.namespace
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.Processor

/**
 * @author Steven
 * Service for handling links between MyBatis mappers and elements in IntelliJ IDEA.
 *
 * @constructor Creates a new [LinkService] instance for the specified [project].
 */
@Service(Service.Level.PROJECT)
class LinkService(private val project: Project) {

    companion object {
        /**
         * Gets the instance of [LinkService] for the specified [project].
         *
         * @param project The IntelliJ project.
         * @return The [LinkService] instance.
         */
        fun getInstance(project: Project): LinkService {
            return project.getService(LinkService::class.java)
        }
    }

    /**
     * Processes the given [psiMethod] and invokes the [processor] for matching [IdDomElement] instances.
     *
     * @param psiMethod The PSI method to process.
     * @param processor The processor to handle matching [IdDomElement] instances.
     */
    private fun process(psiMethod: PsiMethod, processor: Processor<IdDomElement?>) {
        val psiClass = psiMethod.containingClass ?: return
        val id = psiClass.qualifiedName + DOT_SEPARATOR + psiMethod.name
        project.mappers()
            .stream()
            .flatMap { mapper -> mapper.getDaoElements().stream() }
            .filter { idDomElement -> MapperUtil.getIdSignature(idDomElement) == id }
            .forEach { t: IdDomElement? -> processor.process(t) }
    }

    /**
     * Processes the given [clazz] and invokes the [processor] for matching [Mapper] instances.
     *
     * @param clazz The PSI class to process.
     * @param processor The processor to handle matching [Mapper] instances.
     */
    private fun process(clazz: PsiClass, processor: Processor<Mapper?>) {
        clazz.project.mappers()
            .stream()
            .filter { mapper -> mapper.namespace() == clazz.qualifiedName }
            .forEach { t: Mapper? -> processor.process(t) }
    }

    /**
     * Processes the specified [target] PSI element and invokes the [processor] accordingly.
     *
     * @param target The target PSI element to process.
     * @param processor The processor to handle matching elements.
     */
    @Suppress("UNCHECKED_CAST")
    fun process(target: PsiElement, processor: Processor<*>) {
        when (target) {
            is PsiMethod -> process(target, processor as Processor<IdDomElement?>)
            is PsiClass -> process(target, processor as Processor<Mapper?>)
        }
    }
}

