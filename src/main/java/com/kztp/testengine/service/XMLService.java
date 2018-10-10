package com.kztp.testengine.service;

import com.kztp.testengine.model.Choice;
import com.kztp.testengine.model.Question;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public final class XMLService {
    private List<Question> questions;

    public XMLService(List<Question> questions) {
        this.questions = questions;
    }

    private void createXml(int maxPoints,String filename) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Test root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("test");
            doc.appendChild(rootElement);

            //Test maxPoints element
            Element maxPointsEl = doc.createElement("maxpoints");
            rootElement.appendChild(maxPointsEl);
            maxPointsEl.appendChild(doc.createTextNode(Integer.toString(maxPoints)));


            // Question elements
            for (int i = 0; i < questions.size(); i++) {
                //Question Tag
                Element questionEl = doc.createElement("question");
                rootElement.appendChild(questionEl);
                //QeuestionText element
                Element questionText = doc.createElement("text");
                questionEl.appendChild(questionText);
                questionText.appendChild(doc.createTextNode(questions.get(i).getText()));
                //Question Id element
                Element id = doc.createElement("id");
                questionEl.appendChild(id);
                id.appendChild(doc.createTextNode(Integer.toString(i)));
                //Question choice element
                Element choiceEl = doc.createElement("choices");
                questionEl.appendChild(choiceEl);
                //Choice element
                for (int j = 0; j < questions.get(i).getChoices().size(); j++) {
                    Choice currentChoice = questions.get(i).getChoices().get(j);
                    Element numberEl = doc.createElement("c");
                    numberEl.appendChild(doc.createTextNode(currentChoice.getText()));
                    choiceEl.appendChild(numberEl);
                }
                //Answer element
                Element answerEl = doc.createElement("answer");
                answerEl.appendChild(doc.createTextNode(questions.get(i).getAnswer().getText()));

                questionEl.appendChild(answerEl);


            }

            // XML to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename+".xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
