import com.rdlp.xml.api.*
import com.rdlp.xml.api.annotations.*

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

@XmlElement("plano")
class Plano(
    @XmlElement
    val curso: String,
    @XmlElement
    val fucs: List<FUC>
)

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

fun main() {
    //plano()
    planoAnnotations()
}

fun planoAnnotations() {

    val f1 = FUC("M4310", "Programação Avançada", 6.0, "la la...",
        listOf(
            ComponenteAvaliacao("Quizzes", 20),
            ComponenteAvaliacao("Projeto", 80)
        )
    )
    val f2 = FUC("03782", "Dissertação", 42.0, "la la...",
        listOf(
            ComponenteAvaliacao("Dissertação", 60),
            ComponenteAvaliacao("Apresentação", 20),
            ComponenteAvaliacao("Discussão", 20)
        )
    )
    val f3 = FUC("A03782", "Mundos virtuais", 50.0, "la la...",
        listOf(
            ComponenteAvaliacao("Dissertação", 60),
            ComponenteAvaliacao("Apresentação", 20),
            ComponenteAvaliacao("Discussão", 20)
        )
    )
    val plano = Plano("Mestrado em Engenharia Informática", listOf(f1,f2,f3))
    val doc = Document()
    doc.setRootElement(Element.fromAnnotation(plano))
    print(doc.toText())
}

fun plano() {
    val doc = Document()
    val plano = Element(name = "plano")
    doc.setRootElement(plano)

    plano / listOf(
        Element(name = "curso", text = "Mestrado em Engenharia Informática"),
        Element(name = "fuc") (Attribute("codigo", "M4310")) /
                listOf(
                    Element(name = "nome", text = "Programação Avançada"),
                    Element(name = "ects", text = "6.0"),
                    Element(name = "avaliacao") /
                            listOf(
                                Element(name = "componente") (listOf(Attribute("nome", "Quizzes"), Attribute("peso", "20%"))),
                                Element(name = "componente") (listOf(Attribute("nome", "Projeto"), Attribute("peso", "80%")))
                            )
                ),
        Element(name = "fuc") (Attribute("codigo", "03782")) /
                listOf(
                    Element(name = "nome", text = "Dissertação"),
                    Element(name = "ects", text = "42.0"),
                    Element(name = "avaliacao") /
                            listOf(
                                Element(name = "componente") (listOf(Attribute("nome", "Dissertação"), Attribute("peso", "60%"))),
                                Element(name = "componente") (listOf(Attribute("nome", "Apresentação"), Attribute("peso", "20%"))),
                                Element(name = "componente") (listOf(Attribute("nome", "Discussão"), Attribute("peso", "20%")))
                            )
                )
    )

    print(doc.toText(color = false))
}

fun biblioteca() {
    val doc = Document()
    val bookstoreelement = Element(name = "bookstore")
    doc.setRootElement(bookstoreelement)

    val book1 = Element(parentelement = bookstoreelement, name = "book")
    book1.addAttribute("category", "cooking")
    book1.addElement(name = "title", text = "Everyday Italian", attributelist = listOf(Attribute("lang", "en")))
    book1.addElement(name = "author", text = "Giada De Laurentiis")
    book1.addElement(name = "year", text = "2005")
    book1.addElement(name = "price", text = "30.00")

    val book2 = Element(parentelement = bookstoreelement, name = "book")
    book2.addAttribute("category", "children")
    book2.addElement(name = "title", text = "Harry Potter", attributelist = listOf(Attribute("lang", "en")))
    book2.addElement(name = "author", text = "J K. Rowling")
    book2.addElement(name = "year", text = "2005")
    book2.addElement(name = "price", text = "29.99")

    val book3 = Element(parentelement = bookstoreelement, name = "book")
    book3.addAttribute("category", "web")
    book3.addElement(name = "title", text = "Learning XML", attributelist = listOf(Attribute("lang", "en")))
    book3.addElement(name = "author", text = "Erik T. Ray")
    book3.addElement(name = "year", text = "2003")
    book3.addElement(name = "price", text = "39.95")

    //print(doc.toText)
}