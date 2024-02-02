package cn.com.mustache.plugins.mybatis.link.description

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER
import cn.com.mustache.plugins.mybatis.link.element.Mapper
import cn.com.mustache.plugins.mybatis.link.util.PsiUtil
import com.intellij.openapi.module.Module
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomFileDescription

/**
 * @author Steven
 */
class MapperDescription : DomFileDescription<Mapper>(Mapper::class.java, MAPPER) {

    override fun isMyFile(file: XmlFile, module: Module?): Boolean {
        return PsiUtil.isMybatisFile(file)
    }
}