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
    val avaliacao: List<ComponenteAvaliacao>,
    @XmlAttribute
    val rand: Map<String, String>
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
    val map = mutableMapOf<String,String>()
    map["nomes"] = "bacanos"
    map["ectss"] = "bacanos2"

    val f1 = FUC("M4310", "Programação Avançada", 6.0, "la la...",
        listOf(
            ComponenteAvaliacao("Quizzes", 20),
            ComponenteAvaliacao("Projeto", 80)
        ),
        map
    )
    val f2 = FUC("03782", "Dissertação", 42.0, "la la...",
        listOf(
            ComponenteAvaliacao("Dissertação", 60),
            ComponenteAvaliacao("Apresentação", 20),
            ComponenteAvaliacao("Discussão", 20)
        ),
        map
    )
    val f3 = FUC("A03782", "Mundos virtuais", 50.0, "la la...",
        listOf(
            ComponenteAvaliacao("Dissertação", 60),
            ComponenteAvaliacao("Apresentação", 20),
            ComponenteAvaliacao("Discussão", 20)
        ),
        map
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

    Element(parentelement = plano, name = "curso", text = "Mestrado em Engenharia Informática")

    val fuc1 = Element(parentelement = plano, name = "fuc")
    fuc1.addAttribute("codigo", "M4310")
    fuc1.addElement(name = "nome", text = "Programação Avançada")
    fuc1.addElement(name = "ects", text = "6.0")

    val avaliacao1 = Element(parentelement = fuc1, name = "avaliacao")
    avaliacao1.addElement(name = "componente", attributelist = listOf(Attribute("nome", "Quizzes"), Attribute("peso", "20%")))
    avaliacao1.addElement(name = "componente", attributelist = listOf(Attribute("nome", "Projeto"), Attribute("peso", "80%")))

    val fuc2 = Element(parentelement = plano, name = "fuc")
    fuc2.addAttribute("codigo", "03782")
    fuc2.addElement(name = "nome", text = "Dissertação")
    fuc2.addElement(name = "ects", text = "42.0")

    val avaliacao2 = Element(parentelement = fuc2, name = "avaliacao")
    avaliacao2.addElement(name = "componente", attributelist = listOf(Attribute("nome", "Dissertação"), Attribute("peso", "60%")))
    avaliacao2.addElement(name = "componente", attributelist = listOf(Attribute("nome", "Apresentação"), Attribute("peso", "20%")))
    avaliacao2.addElement(name = "componente", attributelist = listOf(Attribute("nome", "Discussão"), Attribute("peso", "20%")))

    //val list = doc.getElementsByPath("fuc/avaliacao/componente")

    //list.forEach { println(it.toText(color = true, identation = false, newline = false)) }

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