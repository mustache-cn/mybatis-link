package cn.com.mustache.plugins.mybatis.link.description

import cn.com.mustache.plugins.mybatis.link.constant.LinkConstant.Companion.MAPPER_FILE_TYPE
import cn.com.mustache.plugins.mybatis.link.element.Mapper
import cn.com.mustache.plugins.mybatis.link.util.isMybatisFile
import com.intellij.openapi.module.Module
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomFileDescription

/**
 * A custom file description for MyBatis Mapper XML files, extending [DomFileDescription].
 *
 * @param mapperClass The class representing the structure of the XML file.
 * @param fileType    The file type constant, typically representing the file extension.
 */
class MybatisMapperFileDescription : DomFileDescription<Mapper>(Mapper::class.java, MAPPER_FILE_TYPE) {

    /**
     * Checks if the given XML file is a MyBatis Mapper file.
     *
     * @param file   The XML file to be checked.
     * @param module The module to which the file belongs.
     * @return True if the file is a MyBatis Mapper file, false otherwise.
     */
    override fun isMyFile(file: XmlFile, module: Module?): Boolean {
        return file.isMybatisFile()
    }
}