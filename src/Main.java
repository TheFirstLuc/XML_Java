import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        var xmlFiles = getXMLDatei("C:\\Users\\TheFi\\Desktop\\Programmieren\\Website\\INS\\Feedbacks");
        ArrayList<Document> documents = null;

        for (var xml : xmlFiles) {
            System.out.println(xml);
        }

        //check XML
        try {
            documents = checkXMLandGetDocument(xmlFiles);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException: " + e.getMessage());
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            System.exit(-1);
        } catch (SAXException e) {
            System.out.println("SAXException: " + e.getMessage());
            System.exit(-1);
        }

        //auswerten
        Feedback feedback = new Feedback(documents);

        System.out.println(feedback.averageNoteInhalt());

        XMLWriter.writeToXML(feedback, "C:\\Users\\TheFi\\Desktop\\Programmieren\\Website\\INS\\Auswertung\\Auswertung.xml");

    }

    private static ArrayList<Document> checkXMLandGetDocument(Set<File> xmlFiles) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new SimpleErrorHandler());
        // the "parse" method also validates XML, will throw an exception if misformatted

        ArrayList<Document> documentList = new ArrayList<>();

        for (var xmlFile : xmlFiles){
            documentList.add(builder.parse(new InputSource(xmlFile.getAbsolutePath())));
        }
        return documentList;
    }

    private static Set<File> getXMLDatei(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(name -> !name.getName().equals("Feedback.dtd"))
                .collect(Collectors.toSet());
    }
}