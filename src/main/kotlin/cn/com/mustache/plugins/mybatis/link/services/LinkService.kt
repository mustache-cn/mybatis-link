package cn.com.mustache.plugins.mybatis.link.services

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.DOT_SEPARATOR
import cn.com.mustache.plugins.mybatis.link.element.IdDomElement
import cn.com.mustache.plugins.mybatis.link.element.Mapper
import cn.com.mustache.plugins.mybatis.link.util.MapperUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.Processor

/**
 * @author Steven
 */
@Service(Service.Level.PROJECT)
class LinkService(private val project: Project) {

    companion object {
        fun getInstance(project: Project): LinkService {
            return project.getService(LinkService::class.java)
        }
    }

    private fun process(psiMethod: PsiMethod, processor: Processor<IdDomElement?>) {
        val psiClass = psiMethod.containingClass ?: return
        val id = psiClass.qualifiedName + DOT_SEPARATOR + psiMethod.name
        MapperUtil.findMappers(project)
            .stream()
            .flatMap { mapper -> mapper.getDaoElements().stream() }
            .filter { idDomElement -> MapperUtil.getIdSignature(idDomElement) == id }
            .forEach { t: IdDomElement? -> processor.process(t) }
    }

    private fun process(clazz: PsiClass, processor: Processor<Mapper?>) {
        MapperUtil.findMappers(clazz.project)
            .stream()
            .filter { mapper -> MapperUtil.getNamespace(mapper) == clazz.qualifiedName }
            .forEach { t: Mapper? -> processor.process(t) }
    }

    @Suppress("UNCHECKED_CAST")
    fun process(target: PsiElement, processor: Processor<*>) {
        when (target) {
            is PsiMethod -> process(target, processor as Processor<IdDomElement?>)
            is PsiClass -> process(target, processor as Processor<Mapper?>)
        }
    }
}

