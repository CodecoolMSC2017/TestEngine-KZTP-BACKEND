package com.kztp.testengine.service;

import com.kztp.testengine.exception.InvalidUploadTypeException;
import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.Choice;
import com.kztp.testengine.model.Question;
import com.kztp.testengine.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public final class XMLService {

    @Value("${upload.path}")
    private String path;

    @Autowired
    private TestService testService;

    public XMLService() {
    }

    public String createXml(int maxPoints,List<Question> questions) {
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
                //QuestionText element
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
            String filename = UUID.randomUUID().toString().replace("-", "");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename+".xml"));

            transformer.transform(source, result);
            return filename;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Question> readXml(String path) {
        File xmlFile = new File(path + ".xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        List<Question> questionList= new ArrayList<>();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();


            NodeList questions = doc.getElementsByTagName("question");
            for (int i = 0;i < questions.getLength();i++) {
                questionList.add(getQuestion(questions.item(i)));
            }


        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }


        return questionList;
    }

    private Question getQuestion(Node node) {
        if(node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            String text = element.getElementsByTagName("text").item(0).getFirstChild().getTextContent();
            int id =Integer.parseInt(element.getElementsByTagName("id").item(0).getFirstChild().getTextContent());
            List<Choice> choiceList = new ArrayList<>();
            Choice answer = new Choice(element.getElementsByTagName("answer").item(0).getFirstChild().getTextContent());

            NodeList choices = element.getElementsByTagName("choices").item(0).getChildNodes();
            for (int j = 0;j< choices.getLength();j++) {
                choiceList.add(getChoice(choices.item(j)));
            }


            return new Question(id,text,choiceList,answer);
        }
        return null;

    }

    private Choice getChoice(Node node) {
        if(node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            return new Choice(element.getFirstChild().getTextContent());
        }
        return null;
    }

    public Map<String,Boolean> uploadXml(String title, String description, int price, int maxpoints,String type,MultipartFile file) throws IOException, InvalidUploadTypeException, UnauthorizedRequestException {
        Path directory = Paths.get(path);
        String fullPath = path;

        if(file == null){
            throw new InvalidUploadTypeException("Empty file error.");
        }
        if (!file.getContentType().equals("xml")) {
            throw new InvalidUploadTypeException("Only xml files are allowed!");
        }

        if (!Files.exists(directory)) {
            new File(fullPath).mkdirs();
        }
        String filename = UUID.randomUUID().toString().replace("-", "");


        File uploadedFile = new File(fullPath, filename);
        uploadedFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(uploadedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        Map<String,Boolean> status = new HashMap<>();
        if(xmlValidator(filename)) {
            status.put("status",true);
            testService.createTestFromUploadedXml(filename,title,description,price,maxpoints,type);
        }
        status.put("status",false);


        return status;
    }

    private boolean xmlValidator(String path) {
        List<Question> questions = readXml(path);
        if(questions.size() == 0 ){
            return false;
        }
        for(Question question : questions) {
            if (question.getText() == null || question.getAnswer() == null || question.getChoices().size() == 0 ) {
                return false;
            }
            for(Choice choice : question.getChoices()) {
                if(choice.getText() == null || choice.getText().length() < 1) {
                    return false;
                }
            }
        }
        return true;
    }

}
