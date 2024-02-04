package cn.com.mustache.plugins.mybatis.link.element

import com.intellij.util.xml.*
import org.jetbrains.annotations.NotNull

/**
 * @author Steven
 *
 * Represents a common interface for elements with an 'id' attribute in the XML.
 */
interface IdDomElement : DomElement {

    /**
     * Gets the 'id' attribute value.
     */
    @get:Required
    @get:NameValue
    @get:Attribute("id")
    val id: GenericAttributeValue<String>
}

/**
 * Represents an 'update' element in the XML, extending the [IdDomElement] interface.
 */
interface Update : IdDomElement

/**
 * Represents an 'insert' element in the XML, extending the [IdDomElement] interface.
 */
interface Insert : IdDomElement

/**
 * Represents a 'delete' element in the XML, extending the [IdDomElement] interface.
 */
interface Delete : IdDomElement

/**
 * Represents a 'select' element in the XML, extending the [IdDomElement] interface.
 */
interface Select : IdDomElement

/**
 * Represents a 'mapper' element in the XML.
 */
interface Mapper : DomElement {

    /**
     * Gets a list of [IdDomElement] elements representing 'insert', 'update', 'delete', and 'select'.
     *
     * @return A list of [IdDomElement] elements.
     */
    @NotNull
    @SubTagsList(value = ["insert", "update", "delete", "select"])
    fun getDaoElements(): List<IdDomElement>

    /**
     * Gets the value of the 'namespace' attribute.
     *
     * @return The value of the 'namespace' attribute.
     */
    @Required
    @NameValue
    @NotNull
    @Attribute("namespace")
    fun getNamespace(): GenericAttributeValue<String>

    /**
     * Gets a list of 'insert' elements in the XML.
     *
     * @return A list of [Insert] elements.
     */
    @NotNull
    @SubTagList("insert")
    fun getInserts(): List<Insert>

    /**
     * Gets a list of 'update' elements in the XML.
     *
     * @return A list of [Update] elements.
     */
    @NotNull
    @SubTagList("update")
    fun getUpdates(): List<Update>

    /**
     * Gets a list of 'delete' elements in the XML.
     *
     * @return A list of [Delete] elements.
     */
    @NotNull
    @SubTagList("delete")
    fun getDeletes(): List<Delete>

    /**
     * Gets a list of 'select' elements in the XML.
     *
     * @return A list of [Select] elements.
     */
    @NotNull
    @SubTagList("select")
    fun getSelects(): List<Select>

    /**
     * Adds a 'select' element to the XML.
     *
     * @return The newly added [Select] element.
     */
    @SubTagList("select")
    fun addSelect(): Select

    /**
     * Adds an 'update' element to the XML.
     *
     * @return The newly added [Update] element.
     */
    @SubTagList("update")
    fun addUpdate(): Update

    /**
     * Adds an 'insert' element to the XML.
     *
     * @return The newly added [Insert] element.
     */
    @SubTagList("insert")
    fun addInsert(): Insert

    /**
     * Adds a 'delete' element to the XML.
     *
     * @return The newly added [Delete] element.
     */
    @SubTagList("delete")
    fun addDelete(): Delete
}
