package cn.com.mustache.plugins.mybatis.link.element

import com.intellij.util.xml.*
import org.jetbrains.annotations.NotNull

/**
 * @author Steven
 */
interface IdDomElement : DomElement {

    @get:Required
    @get:NameValue
    @get:Attribute("id")
    val id: GenericAttributeValue<String>
}

interface Update : IdDomElement

interface Insert : IdDomElement

interface Delete : IdDomElement

interface Select : IdDomElement

interface Mapper : DomElement {

    @NotNull
    @SubTagsList(value = ["insert", "update", "delete", "select"])
    fun getDaoElements(): List<IdDomElement>

    @Required
    @NameValue
    @NotNull
    @Attribute("namespace")
    fun getNamespace(): GenericAttributeValue<String>

    @NotNull
    @SubTagList("insert")
    fun getInserts(): List<Insert>

    @NotNull
    @SubTagList("update")
    fun getUpdates(): List<Update>

    @NotNull
    @SubTagList("delete")
    fun getDeletes(): List<Delete>

    @NotNull
    @SubTagList("select")
    fun getSelects(): List<Select>

    @SubTagList("select")
    fun addSelect(): Select

    @SubTagList("update")
    fun addUpdate(): Update

    @SubTagList("insert")
    fun addInsert(): Insert

    @SubTagList("delete")
    fun addDelete(): Delete
}
