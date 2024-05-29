# XML API

## Introduction

The XML API is a Kotlin library designed to facilitate the creation, manipulation in memory, and representation of XML documents. 
It provides classes and functions to create XML elements, attributes, and documents, along with utility functions to manipulate these XML structures.

## Table of Contents

- [Introduction](#introduction)
- [Usage](#usage)
- [Features](#features)
- [Documentation](#documentation)
- [Examples](#examples)
- [Troubleshooting](#troubleshooting)
- [Contributors](#contributors)

## Usage

### Creating a Document

```kotlin
val document = Document()
```

### Setting the Root Element

```kotlin
val root = Element(name = "root")
document.setRootElement(root)
```

### Addinng Elements

```kotlin
val child = Element(name = "child")
root.addElement(child)
```

### Adding Attributes

```kotlin
val attribute = Attribute(name = "id", value = "123")
child.addAttribute(attribute)
```

### Converting Document to text

```kotlin
val xmlString = document.toText()
println(xmlString)
```

### Writing Document to file

```kotlin
document.toFile(directorypath = "./output")
```

## Features

- Create and manipulate XML documents.
- Add, remove, and rename elements and attributes.
- Support for visitor pattern to traverse XML structures.
- Convert XML documents to text with optional ANSI color coding.
- Write XML documents to files.
- Support for annotations to generate XML from annotated classes.

## Documentation
### Classes

- Document
  - Represents an XML document.
  - Methods: `accept`, `toText`, `setRootElement`, `removeRootElement`, `renameElementsByName`, `addAttributeToElement`, `renameAttributesByName`, `removeElementByName`, `removeAttributeByName`, `getElementsByPath`, `toFile`.

- Element
  - Represents an XML element.
  - Methods: `accept`, `toText`, `addAttribute`, `removeAttribute`, `addElement`, `removeElement`, `changeAttribute`.
  - Companion Object: `fromAnnotation`.

- Attribute
  - Represents an XML attribute.
  - Methods: `toText`.

- TextFormatter
  - Interface for formatting text with the annotation `XmlString`.

- ClassAdapter
  - Interface for making changes to an element after mapping inside `Element.fromAnnotation`.

### Annotations

- XmlElement
  - Marks a class as an XML element.

- XmlAttribute
  - Marks a property as an XML attribute.

- XmlExclude
  - Excludes a property from being included in the XML representation.

- XmlString
  - Specifies a formatter for a property.

- XmlAdapter
  - Specifies an adapter for a class to adapt it after mapping.

## Examples

### Example 1: Creating a Simple XML Document

```kotlin
val document = Document()
val root = Element(name = "library")
document.setRootElement(root)

val book = Element(name = "book", text = "Kotlin Programming")
root.addElement(book)

val author = Attribute(name = "author", value = "John Doe")
book.addAttribute(author)

println(document.toText())
```

### Example 2: Generating XML from Annotated Class

```kotlin
@XmlElement(name = "person")
data class Person(
    @XmlAttribute(name = "id") val id: String,
    @XmlElement(name = "name") val name: String,
    @XmlElement(name = "age") val age: Int
)

val person = Person(id = "1", name = "Alice", age = 30)
val element = Element.fromAnnotation(person)

val document = Document()
document.setRootElement(element)

println(document.toText())
```

## Troubleshooting
### Common Issues

- Invalid Element Name
  - Ensure element names contain only lowercase letters.

- Invalid Attribute Name
  - Ensure attribute names contain only lowercase letters.

- Empty Attribute Value
  - Ensure attribute values are not empty or blank.

- File Not Found
  - Ensure the directory path provided to `toFile` exists.

Debugging Tips

- Use `println(document.toText())` to inspect the XML document at different stages of construction.
- Verify annotations and their usage in the annotated classes.

## Contributors
- [Ricardo-Paixao](https://github.com/Ricardo-Paixao)