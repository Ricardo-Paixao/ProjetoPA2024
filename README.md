# XML API

This API 

# Usage

## Element

```kotlin
import com.rdlp.xml.api.Element

fun main() {
    // Creates an empty xml element
    val element = Element(name = "element")
    
    // Creates a xml element with text
    val element2 = Element(name = "element", text = "example text")
    
    val parentelement = Element(name = "parent")
    
    // Creates a xml element and attaches it to another element
    val element3 = Element(parentelement = parentelement, name = "element")
}
```

## Attribute

```kotlin
import com.rdlp.xml.api.Attribute

fun main() {
    // Creates an attibute with name and value
    val attribute = Attribute(name = "attibutename", value = "attibutevalue")

    val element1 = Element(name = "element")
    
    // Creates an attibute and attaches it to an element
    val attibute2 = Attribute(name = "attibutename", value = "attibutevalue", element = element1)
}
```

## Document

```kotlin
import com.rdlp.xml.api.Document
import com.rdlp.xml.api.Element

fun main() {
    // Creates an empty xml document
    val doc = Document()
    
    
}
```