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