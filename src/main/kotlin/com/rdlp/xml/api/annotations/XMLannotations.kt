package com.rdlp.xml.api.annotations

import com.rdlp.xml.api.ClassAdapter
import com.rdlp.xml.api.TextFormatter
import com.rdlp.xml.api.*
import kotlin.reflect.KClass

/**
 * Annotation used on classes/parameters/properties to represent XML [Element]
 *
 * @property name the [Element.name] of the XML [Element]
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class XmlElement(val name: String = "")

/**
 * Annotation used on parameters/properties to represent XML [Attribute]
 *
 * @property name the [Attribute.name] of the XML [Attribute]
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class XmlAttribute(val name: String = "")

/**
 * Annotation used to exclude classes/parameters/properties from the XML
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class XmlExclude

/**
 * Annotation used to format text inside XML
 *
 * @property formatter the class that implements the [TextFormatter] interface
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class XmlString(val formatter: KClass<out TextFormatter>)

/**
 * Annotation used to make after mapping change on the XML [Element]
 *
 * @property adapter the class that implements the [ClassAdapter] interface
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class XmlAdapter(val adapter: KClass<out ClassAdapter>)