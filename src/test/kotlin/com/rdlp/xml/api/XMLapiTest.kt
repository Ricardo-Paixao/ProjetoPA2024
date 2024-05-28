package com.rdlp.xml.api

import com.rdlp.xml.api.annotations.*
import org.junit.jupiter.api.Assertions.*
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
        assertEquals(
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
        assertEquals(
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
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", doc.toText(false))
    }

    @Test
    fun documentRenameElementsByNameTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        doc.renameElementsByName("curso", "cur")
        print(doc.toText(false))
        assertEquals(
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
        assertEquals(
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
        assertEquals(
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
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<plano/>\n", doc.toText(false))
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
        assertEquals(
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
        assertEquals(listOf(fuc2), doc.getElementsByPath("fuc"))
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
            assertEquals(
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
        assertEquals("plano/fuc", fuc2.path)
    }

    @Test
    fun elementToTextTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        Element(plano, "curso", "Mestrado em Engenharia Informática")
        assertEquals(
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
        assertEquals(
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
        assertEquals(
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
        assertEquals(
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
        assertEquals("<plano/>", plano.toText(false, identation = false, newline = false))
    }

    @Test
    fun elementChangeAttributeTest() {
        val doc = Document()
        val plano = Element(name = "plano")
        doc.setRootElement(plano)
        val fuc = Element(plano, "fuc")
        fuc.addAttribute("codigo", "M4310")
        fuc.changeAttribute("codigo", "A4545")
        assertEquals(
            "<plano>\n\t<fuc codigo=\"A4545\"/>\n</plano>\n",
            plano.toText(color = false, identation = true, newline = true)
        )
    }

    @Test
    fun attributeToTextTest() {
        val attr = Attribute("codigo", "M4310")
        assertEquals("codigo=\"M4310\"", attr.toText(false))
    }

    @Test
    fun attributeSetElementTest() {
        val attr = Attribute("codigo", "M4310")
        val fuc = Element(name="fuc")
        attr.setElement(fuc)
        assertEquals(
            "<fuc codigo=\"M4310\"/>\n",
            fuc.toText(color = false, identation = true, newline = true)
        )
    }

    class AddPercentage : TextFormatter {
        override fun textFormat(text: String): String {
            return "$text%"
        }
    }

    class FucAdapter : ClassAdapter {
        override fun adapt(element: Element): Element {
            val avalicao = element.children[2]
            val componentes = avalicao.children.toList()
            element.removeElement(avalicao)
            componentes.forEach { element.addElement(it) }
            return element
        }
    }

    @XmlElement("componente")
    class ComponenteAvaliacao(
        @XmlAttribute
        val nome: String,
        @XmlAttribute
        @XmlString(AddPercentage::class)
        val peso: Int
    )

    @XmlElement("fuc")
    @XmlAdapter(FucAdapter::class)
    class FUC(
        @XmlAttribute
        val codigo: String,
        @XmlElement
        val nome: String,
        @XmlElement
        val ects: Double,
        @XmlExclude
        val observacoes: String,
        @XmlElement
        val avaliacao: List<ComponenteAvaliacao>
    )

    @Test
    fun annotationTest() {
        val f1 = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val element = Element.fromAnnotation(f1)

        assertEquals("<fuc codigo=\"M4310\">\n" +
                "\t<nome>Programação Avançada</nome>\n" +
                "\t<ects>6.0</ects>\n" +
                "\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "</fuc>\n",
            element.toText(false, identation = true, newline = true))
    }
}