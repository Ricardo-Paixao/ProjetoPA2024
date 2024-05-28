package com.rdlp.xml.api

import com.rdlp.xml.api.annotations.*
import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

//private const val red = "\u001b[31m"
private const val blue = "\u001b[34m"
private const val brightred = "\u001b[91m"
private const val reset = "\u001b[0m"

/**
 * A XML [Document]
 *
 * @constructor Creates an empty XML [Document]
 */
class Document {
    var rootElement: Element? = null
        private set

    /**
     * Receives a [visitor] and calls the [accept] function on the [rootElement]
     *
     * @param visitor a function that receives an [Element] and returns a [Boolean]
     * @receiver
     */
    fun accept(visitor: (Element) -> Boolean) {
        rootElement?.accept(visitor)
    }

    /**
     * Represents the XML [Document] as a [String]
     *
     * @param color [Boolean] that defines if the [String] contains ANSI color codes
     * @return the [String] representation of the XML [Document]
     */
    fun toText(color: Boolean = true): String {
        var str = ""

        if (color)
            str += blue

        str += "<"

        if (color)
            str += brightred

        str += "?xml version"

        if (color)
            str += blue

        str += "=\"1.0\""

        if (color)
            str += brightred

        str += " encoding"

        if (color)
            str += blue

        str += "=\"UTF-8\""

        if (color)
            str += brightred

        str += "?>"
        str += "\n"

        if (rootElement != null)
            str += rootElement?.toText(color, identation = true, newline = true)

        return str
    }

    /**
     * Sets the [rootElement]
     *
     * @param element the new [rootElement]
     */
    fun setRootElement(element: Element) {
        rootElement = element
    }

    /**
     * Removes the [rootElement]
     *
     */
    fun removeRootElement() {
        rootElement = null
    }

    /**
     * Renames the all [Element] to [newname] if the [Element.name] matches [oldname]
     *
     * @param oldname the [Element.name] of the [Element] to be renamed
     * @param newname the new [Element.name] of the [Element] to be renamed
     */
    fun renameElementsByName(oldname: String, newname: String) {
        require(oldname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$oldname\" was found." }
        require(newname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$newname\" was found." }
        rootElement?.accept { if (it.name == oldname) it.name = newname; true }
    }

    /**
     * Adds an [Attribute] to all [Element] if the [Element.name] matches the [elementname]
     *
     * @param elementname the [Element.name] of the [Element]
     * @param attributename the [Attribute.name] of the new [Attribute]
     * @param attributevalue the [Attribute.value] of the new [Attribute]
     */
    fun addAttributeToElement(elementname: String, attributename: String, attributevalue: String) {
        require(elementname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$elementname\" was found." }
        require(attributename.contains("^[a-z]+$".toRegex())) { "Attribute's name must be lowercase. \"$attributename\" was found." }
        require(attributevalue.isNotBlank()) { "Attribute's value can't be empty." }
        rootElement?.accept { if (it.name == elementname) it.addAttribute(attributename, attributevalue); true }
    }

    /**
     * Renames all [Attribute] to [newname] if the [Element.name] matches [elementname] and [Attribute.name] matches [oldname]
     *
     * @param elementname the [Element.name] of the [Element]
     * @param oldname the [Attribute.name] of the [Attribute] to be renamed
     * @param newname the new [Attribute.name] of the [Attribute] to be renamed
     */
    fun renameAttributesByName(elementname: String, oldname: String, newname: String) {
        require(elementname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$elementname\" was found." }
        require(oldname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$oldname\" was found." }
        require(newname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$newname\" was found." }
        rootElement?.accept {if (it.name == elementname) it.attributes.forEach { insideit -> if (insideit.name == oldname) insideit.name = newname}; true}
    }

    /**
     * Removes all [Element] if the [Element.name] matches [name]
     *
     * @param name the [Element.name] of the [Element]
     */
    fun removeElementByName(name: String) {
        require(name.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$name\" was found." }
        if (rootElement?.name == name)
            removeRootElement()

        rootElement?.accept {
            if (it.children.any { it2 -> it2.name == name })
                it.removeElement(name)
            true
        }
    }

    /**
     * Removes the [Attribute] if the [Element.name] matches [elementname] and [Attribute.name] matches [attributename]
     *
     * @param elementname the [Element.name] of the [Element]
     * @param attributename the [Attribute.name] of the [Attribute] to be removed
     */
    fun removeAttributeByName(elementname: String, attributename: String) {
        require(elementname.contains("^[a-z]+$".toRegex())) { "Element's name must be lowercase. \"$elementname\" was found." }
        require(attributename.contains("^[a-z]+$".toRegex())) { "Attribute's name must be lowercase. \"$attributename\" was found." }
        rootElement?.accept { if (it.name == elementname) it.removeAttribute(attributename); true }
    }

    /**
     * Gets a [List] of [Element] if the [Element.path] end with [path]
     *
     * @param path the [Element.path] or part of the [Element.path] of the [Element]
     * @return a [List] of [Element]
     */
    fun getElementsByPath(path: String): List<Element> {
        val list = mutableListOf<Element>()
        rootElement?.accept { if (it.path.endsWith(path)) list.add(it); true }
        return list
    }

    /**
     * Converts the [Document] to an XML file at [directorypath] or root directory if not given [directorypath]
     *
     * @param directorypath the path of the directory to write the file to
     */
    fun toFile(directorypath: String = "./") {
        val directory = File(directorypath)

        if (directory.exists() && directory.isDirectory) {

            val filename = if (rootElement != null) rootElement?.name + "_xml.xml" else "generated_xml.xml"

            val file = File(directory, filename)

            file.writeText(toText(false))
        }
        else
        {
            throw FileNotFoundException("The path \"$directorypath\" doesn't exist.")
        }
    }
}

/**
 * An XML [Element]
 *
 * @constructor Creates an XML [Element] with [name] and [text] if given
 *
 * @param name the [name] of the XML [Element]
 * @param text the [text] of the XML [Element]
 * @param parentelement the [Element.parentelement] of the [Element]
 */
class Element(parentelement: Element? = null, name: String, text: String = "", attributes: List<Attribute> = mutableListOf()) {

    private var _children = mutableListOf<Element>()

    /**
     * The [List] of [Element] children
     */
    val children: List<Element>
        get() = _children

    /**
     * The [name] of the XML [Element]
     */
    var name: String = name
        set(newname) {
            require(name.contains("^[a-z]+$".toRegex())) { "Element's name must contain only lowercase letters. \"$name\" was found." }
            field = newname
        }

    /**
     * The [text] of the XML [Element]
     */
    var text: String = text
        set(newtext) {
            require(_children.isEmpty()) { "Elements with text can't have children elements." }
            field = newtext
        }

    private var _attributes = mutableListOf<Attribute>()

    /**
     * The [List] of [Attribute] of the [Element]
     */
    val attributes: List<Attribute>
        get() = _attributes

    /**
     * The parent [Element] of the XML [Element]
     */
    var parentelement: Element? = parentelement
        private set

    init {
        require(name.contains("^[a-z]+$".toRegex())) { "Element's name must contain only lowercase letters. \"$name\" was found." }
        attributes.forEach {
            addAttribute(it)
        }
        parentelement?._children?.add(this)
    }

    /**
     * Receives a [visitor] and calls the [visitor] on this [Element].
     * Then it calls the [accept] function on every [Element] on [Element.children]
     *
     * @param visitor a function that receives an [Element] and returns a [Boolean]
     * @receiver
     */
    fun accept(visitor: (Element) -> Boolean) {
        if (visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }

    private val depth: Int
        get() {
            return if (parentelement == null)
                0
            else
                1 + parentelement!!.depth
        }

    /**
     * The [path] of the [Element] relative to the [Document.rootElement]
     */
    val path: String
        get() {
            return if (parentelement == null)
                name
            else
                parentelement!!.path + "/" + name
        }

    private fun getIndentation(): String {
        var str = ""

        for (i in 0 until depth)
            str += "\t"

        return str
    }

    /**
     * Represent the XML [Element] as a [String]
     *
     * @param color [Boolean] that defines if the [String] contains ANSI color codes
     * @param identation [Boolean] that defines if the [String] is indented
     * @param newline [Boolean] that defines if the [String] has new lines "\n"
     * @return the [String] representation of the XML [Element]
     */
    fun toText(color: Boolean = true, identation: Boolean = true, newline: Boolean = true): String {
        var str = ""

        if (identation)
            str += getIndentation()

        if (color)
            str += blue

        str += "<"

        if (color)
            str += brightred

        str += name

        if (color)
            str += reset

        attributes.forEach {
            str += " "
            str += it.toText(color)
        }

        if (text.isBlank() && children.isEmpty()) {
            if (color)
                str += blue

            str += "/>"

            if (color)
                str += reset

            if (newline)
                str += "\n"

            return str
        }

        if (color)
            str += blue

        str += ">"

        if (color)
            str += reset

        str += text

        if (children.isNotEmpty()) {
            str += "\n"
            children.forEach {
                str += it.toText(color, identation, newline)
            }
            str += getIndentation()
        }

        if (color)
            str += blue

        str += "<"

        if (color)
            str += brightred

        str += "/"

        str += name

        if (color)
            str += blue

        str += ">"

        if (color)
            str += reset

        if (newline)
            str += "\n"

        return str
    }

    /**
     * Adds an [Attribute] to the [Element]
     *
     * @param attribute the [Attribute] to be added
     */
    fun addAttribute(attribute: Attribute) {
        for (att in attributes) {
            if (att.name == attribute.name)
                throw IllegalArgumentException("Attribute with name \"$attribute.name\" already exists in this Element.")
        }

        _attributes.add(attribute)
    }

    /**
     * Creates an [Attribute] with [name] and [value] and adds it to the [Element]
     *
     * @param name the [Attribute.name] of the [Attribute]
     * @param value the [Attribute.value] of the [Attribute]
     */
    fun addAttribute(name: String, value: String) {
        require(name.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$name\" was found." }
        require(value.isNotBlank()) { "Attribute's value can't be empty." }

        for (att in attributes) {
            if (att.name == name)
                throw IllegalArgumentException("Attribute with name \"$name\" already exists in this Element.")
        }

        _attributes.add(Attribute(name, value))
    }

    /**
     * Removes an [Attribute] from the [Element] if the [Attribute.name] matches [attributename]
     *
     * @param attributename the [Attribute.name] of the [Attribute] to be removed
     */
    fun removeAttribute(attributename: String) {
        require(attributename.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$attributename\" was found." }
        val iterator = _attributes.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child.name == attributename) {
                child.setElement(null)
                iterator.remove()
            }
        }
    }

    /**
     * Removes an [Attribute] from the [Element] if it matches [attribute]
     *
     * @param attribute the [Attribute] to be removed
     */
    fun removeAttribute(attribute: Attribute) {
        val iterator = _attributes.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child == attribute) {
                child.setElement(null)
                iterator.remove()
            }
        }
    }

    /**
     * Adds an [Element] to [Element.children]
     *
     * @param element the [Element] to be added
     */
    fun addElement(element: Element) {
        require(this.text.isBlank()) { "Elements with text can't have children elements." }
        element.parentelement?.removeElement(element)
        element.parentelement = this
        _children.add(element)
    }

    /**
     * Creates an [Element] with [name], [text] and [attributelist].
     * Then adds it to [Element.children]
     *
     * @param name the [Element.name] of the [Element]
     * @param text the [Element.text] of the [Element]
     * @param attributelist the [Element.attributes] of the [Element]
     */
    fun addElement(name: String, text: String = "", attributelist: List<Attribute>? = null) {
        require(this.text.isBlank()) { "Elements with text can't have children elements." }
        require(name.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$name\" was found." }
        val element = Element(name = name, text = text)
        element.parentelement = this
        attributelist?.forEach {
            element.addAttribute(it)
        }
        _children.add(element)
    }

    /**
     * Removes an [Element] from [Element.children]
     *
     * @param element the [Element] to be removed
     */
    fun removeElement(element: Element) {
        val iterator = _children.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child == element) {
                child.parentelement = null
                iterator.remove()
            }
        }
    }

    /**
     * Removes an [Element] from [Element.children] if the [Element.name] matches [name] and [Element.attributes] contains [list]
     *
     * @param name the [Element.name] of the [Element] to be removed
     * @param list the [Element.attributes] of the [Element] to be removed
     */
    fun removeElement(name: String, list: List<Attribute> = emptyList()) {
        require(name.contains("^[a-z]+$".toRegex())) { "Element's name must contain only lowercase letters. \"$name\" was found." }
        val iterator = _children.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child.name == name && attributes.containsAll(list)) {
                child.parentelement = null
                iterator.remove()
            }
        }
    }

    /**
     * Changes the [Attribute.value] of the [Attribute]
     *
     * @param name the [Attribute.name] of the [Attribute]
     * @param value the new [Attribute.value] of the [Attribute]
     */
    fun changeAttribute(name: String, value: String) {
        require(name.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$name\" was found." }
        require(value.isNotBlank()) { "Attribute's value can't be empty." }
        _attributes.forEach {
            if (it.name == name)
                it.value = value
        }
    }

    companion object {
        /**
         * Creates an [Element] from an annotated class' instance
         *
         * @param instance the instance of the class used to create the [Element]
         * @return the [Element] created from the given [instance]
         */
        fun fromAnnotation(instance: Any): Element {
            var annotation = instance::class.annotations.find { it is XmlElement }
            if (annotation == null) throw IllegalArgumentException("${instance::class.qualifiedName} must have XMLElement annotation")

            annotation = annotation as XmlElement

            val element = Element(name = annotation.name.ifBlank { instance::class.simpleName.toString() })

            instance::class.primaryConstructor?.parameters?.forEach {

                when {
                    it.hasAnnotation<XmlExclude>() -> {}
                    it.hasAnnotation<XmlAttribute>() -> {
                        var property: KProperty1<out Any, *>? = null
                        instance::class.declaredMemberProperties.forEach { it2 ->
                            if (it2.name == it.name) {
                                property = it2
                            }
                        }

                        if (property == null) return element

                        if (property!!.call(instance) is Map<*, *>) {

                            val map = property!!.call(instance) as Map<*, *>

                            map.forEach { it2 ->
                                val value = if (it.hasAnnotation<XmlString>()) {
                                    (it.findAnnotation<XmlString>()!!.formatter.createInstance()).textFormat(
                                        it2.value.toString()
                                    )
                                } else {
                                    it2.value.toString()
                                }
                                element.addAttribute(name = it2.key.toString(), value = value)
                            }
                        } else {
                            val text = if (it.hasAnnotation<XmlString>()) {
                                (it.findAnnotation<XmlString>()!!.formatter.createInstance()).textFormat(
                                    property!!.call(instance).toString()
                                )
                            } else {
                                property!!.call(instance).toString()
                            }

                            element.addAttribute(
                                it.findAnnotation<XmlAttribute>()!!.name.ifBlank { property!!.name },
                                text
                            )
                        }
                    }

                    it.hasAnnotation<XmlElement>() -> {

                        var property: KProperty1<out Any, *>? = null
                        instance::class.declaredMemberProperties.forEach { it2 ->
                            if (it2.name == it.name) {
                                property = it2
                            }
                        }

                        if (property == null) return element

                        if (property!!.call(instance) is Collection<*>) {
                            val element1 =
                                Element(name = it.findAnnotation<XmlElement>()!!.name.ifBlank { property!!.name })

                            element.addElement(element1)

                            val collection = property!!.call(instance) as Collection<*>

                            collection.forEach { it2 ->
                                if (it2 != null) {
                                    element1.addElement(fromAnnotation(it2))
                                }
                            }
                        } else if (property!!.call(instance) is Map<*, *>) {
                            val element1 =
                                Element(name = it.findAnnotation<XmlElement>()!!.name.ifBlank { property!!.name })

                            element.addElement(element1)

                            val collection = property!!.call(instance) as Map<*, *>

                            collection.forEach { it2 ->
                                val text = if (it.hasAnnotation<XmlString>()) {
                                    (it.findAnnotation<XmlString>()!!.formatter.createInstance()).textFormat(
                                        it2.value.toString()
                                    )
                                } else {
                                    it2.value.toString()
                                }

                                element1.addElement(name = it2.key.toString(), text = text)
                            }
                        } else {

                            val text = if (it.hasAnnotation<XmlString>()) {
                                (it.findAnnotation<XmlString>()!!.formatter.createInstance()).textFormat(
                                    property!!.call(instance).toString()
                                )
                            } else {
                                property!!.call(instance).toString()
                            }

                            element.addElement(
                                it.findAnnotation<XmlElement>()!!.name.ifBlank { property!!.name },
                                text
                            )
                        }
                    }
                }
            }

            val adapterannotation = instance::class.findAnnotation<XmlAdapter>()

            if (adapterannotation != null) {
                return adapterannotation.adapter.createInstance().adapt(element)
            }

            return element
        }
    }

}

/**
 * An XML [Attribute]
 *
 * @constructor Creates an [Attribute] with [name] and [value]
 *
 * @param name the [name] of the [Attribute]
 * @param value the [value] of the [Attribute]
 * @param element the [Element] that the [Attribute] belongs to
 */
class Attribute (
    name: String,
    value: String,
    element: Element? = null
) {

    /**
     * The [Element] that the [Attribute] belongs to
     */
    var element: Element? = element
        private set

    /**
     * The [name] of the XML [Attribute]
     */
    var name: String = name
        set(newname) {
            require(newname.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$name\" was found." }
            element?.attributes?.forEach {
                if (it.name == newname && it != this)
                    throw IllegalArgumentException("Attribute with name \"$newname\" already exists in this Element.")
            }
            field = newname
        }

    /**
     * The [value] of the XML [Attribute]
     */
    var value: String = value
        set(newvalue) {
            require(newvalue.isNotBlank()) { "Attribute's value can't be empty." }
            field = newvalue
        }


    init {
        require(name.contains("^[a-z]+$".toRegex())) { "Attribute's name must contain only lowercase letters. \"$name\" was found." }
        require(value.isNotBlank()) { "Attribute's value can't be empty." }
        element?.addAttribute(this)
    }

    /**
     * Represents the [Attribute] as a [String]
     *
     * @param color [Boolean] that defines if the [String] contains ANSI color codes
     * @return the [String] representation of the XML [Attribute]
     */
    fun toText(color: Boolean = true): String {
        var str = ""

        if (color)
            str += brightred

        str += name

        if (color)
            str += blue

        str += "=\""

        str += value

        str += "\""

        if (color)
            str += reset

        return str
    }

    /**
     * Sets the [Element] that the [Attribute] belongs to
     *
     * @param element the [Element] that the [Attribute] will belong to
     */
    fun setElement(element: Element?) {
        this.element?.removeAttribute(this)
        element?.addAttribute(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Attribute

        if (name != other.name) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

/**
 * Interface used for formatting text with the annotation [XmlString] inside [Element.fromAnnotation]
 *
 * @constructor Create empty [TextFormatter]
 */
interface TextFormatter {
    /**
     * Function that formats the text
     *
     * @param text input [String]
     * @return the formatted [String]
     */
    fun textFormat(text: String): String
}

/**
 * Interface used for after mapping changes on [Element] inside [Element.fromAnnotation]
 *
 * @constructor Create empty [ClassAdapter]
 */
interface ClassAdapter {
    /**
     * Function that makes after mapping changes on [element]
     *
     * @param element input [Element]
     * @return the changed [Element]
     */
    fun adapt(element: Element) : Element
}

operator fun Element.div(list: List<Element>) : Element {
    list.forEach {
        addElement(it)
    }
    return this
}

operator fun Element.div(element: Element) : Element  {
    addElement(element)
    return this
}

operator fun Element.div(name: String) : Element  {
    addElement(name)
    return this
}

operator fun Element.invoke(list: List<Attribute>) : Element  {
    list.forEach {
        addAttribute(it)
    }
    return this
}

operator fun Element.invoke(attribute: Attribute) : Element  {
    addAttribute(attribute)
    return this
}