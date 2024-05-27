package com.rdlp.xml.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class XMLapiTest {
    @Test
    fun documentToTextTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<curso>Mestrado em Engenharia Informática</curso>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentSetRootElementTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<curso>Mestrado em Engenharia Informática</curso>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentRemoveRootElementTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        doc.removeRootElement()
        print(doc.toText(false))
        Assertions.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", doc.toText(false))
    }

    @Test
    fun documentRenameElementsByNameTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        doc.renameElementsByName("curso", "cur")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<cur>Mestrado em Engenharia Informática</cur>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentAddAttributeToElementTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "fuc")
        doc.addAttributeToElement("fuc", "codigo", "M4310")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<fuc codigo=\"M4310\"/>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentRenameAttributesByNameTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc2 = Element(plano, "fuc")
        fuc2.addAttribute("codigo", "03782")
        doc.renameAttributesByName("fuc", "codigo", "codig")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<fuc codig=\"03782\"/>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentRemoveElementByNameTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "fuc")
        doc.removeElementByName("fuc")
        print(doc.toText(false))
        Assertions.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano/>\n", doc.toText(false))
    }

    @Test
    fun documentRemoveAttributeByNameTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc2 = Element(plano, "fuc")
        fuc2.addAttribute("codigo", "03782")
        doc.removeAttributeByName("fuc", "codigo")
        print(doc.toText(false))
        Assertions.assertEquals(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<fuc/>\n</plano>\n",
            doc.toText(false)
        )
    }

    @Test
    fun documentGetElementsByPathTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        val fuc2 = Element(plano, "fuc")
        doc.setRootElement(plano)
        Assertions.assertEquals(listOf(fuc2), doc.getElementsByPath("fuc"))
    }

    @Test
    fun documentToFileTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        Element(plano, "fuc")
        doc.setRootElement(plano)
        doc.toFile()
        val file = File("./plano_xml.xml")
        if (!file.exists()) {
            throw AssertionError("Assertion failed")
        } else {
            print(file.readText())
            Assertions.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano>\n\t<fuc/>\n</plano>\n",
                file.readText()
            )
        }
    }

    @Test
    fun elementPathTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        val fuc2 = Element(plano, "fuc")
        doc.setRootElement(plano)
        Assertions.assertEquals("plano/fuc", fuc2.path)
    }

    @Test
    fun elementToTextTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        Assertions.assertEquals(
            "<plano>\n\t<curso>Mestrado em Engenharia Informática</curso>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun elementAddAttributeTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc = Element(plano, "fuc")
        fuc.addAttribute("codigo", "M4310")
        fuc.addAttribute(Attribute("nome","Programação Avançada"))
        Assertions.assertEquals(
            "<plano>\n\t<fuc codigo=\"M4310\" nome=\"Programação Avançada\"/>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun elementRemoveAttributeTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc = Element(plano, "fuc")
        fuc.addAttribute("codigo", "M4310")
        fuc.addAttribute(Attribute("nome","Programação Avançada"))
        fuc.removeAttribute("codigo")
        fuc.removeAttribute(Attribute("nome","Programação Avançada"))
        Assertions.assertEquals(
            "<plano>\n\t<fuc/>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun elementAddElementTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val curso = Element(name="curso",text="Mestrado em Engenharia Informática")
        plano.addElement(curso)
        Element(plano,"fuc", attributes = listOf(Attribute("codigo", "M4310")))
        Assertions.assertEquals(
            "<plano>\n\t<curso>Mestrado em Engenharia Informática</curso>\n\t<fuc codigo=\"M4310\"/>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun elementRemoveElementTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val curso = Element(name="curso",text="Mestrado em Engenharia Informática")
        plano.addElement(curso)
        Element(plano,"fuc", attributes = listOf(Attribute("codigo", "M4310")))
        plano.removeElement(curso)
        plano.removeElement("fuc")
        Assertions.assertEquals("<plano/>", plano.toText(false, identation = false, newline = false))
    }

    @Test
    fun elementChangeAttributeTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc = Element(plano, "fuc")
        fuc.addAttribute("codigo", "M4310")
        fuc.changeAttribute("codigo", "A4545")
        Assertions.assertEquals(
            "<plano>\n\t<fuc codigo=\"A4545\"/>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun attributeToTextTest() {
        val attr = Attribute("codigo", "M4310")
        Assertions.assertEquals("codigo=\"M4310\"", attr.toText(false))
    }

    @Test
    fun attributeSetElementTest() {
        val attr = Attribute("codigo", "M4310")
        val fuc = Element(name="fuc")
        attr.setElement(fuc)
        Assertions.assertEquals(
            "<fuc codigo=\"M4310\"/>\n",
            fuc.toText(color = false, identation = true, newline = true)
        )
    }
}