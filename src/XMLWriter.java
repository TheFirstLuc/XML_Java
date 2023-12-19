import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLWriter {
    public static void writeToXML(Feedback feedback, String url){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("feedbackdatenbank");
            doc.appendChild(rootElement);

            for (var fb : feedback.content) {
                Element feedbackElement = doc.createElement("feedback");

                var besucherElement = doc.createElement("besucher");
                besucherElement.setAttribute("anrede", fb.get("anrede"));
                besucherElement.setAttribute("vorname", fb.get("vorname"));
                besucherElement.setAttribute("nachname", fb.get("nachname"));

                Element alterElement = doc.createElement("alter");
                alterElement.setTextContent(fb.get("alter"));
                besucherElement.appendChild(alterElement);

                Element kontaktElement = doc.createElement("kontakt");
                kontaktElement.setAttribute("rueckfrage_erlaubt", fb.get("kontakt"));

                Element emailElement = doc.createElement("emailadresse");
                emailElement.setTextContent(fb.get("email"));
                kontaktElement.appendChild(emailElement);

                Element websiteElement = doc.createElement("website");
                websiteElement.setTextContent(fb.get("website"));
                kontaktElement.appendChild(websiteElement);

                Element telefonnummerElement = doc.createElement("telefonnummer");
                telefonnummerElement.setTextContent(fb.get("telefonnummer"));
                kontaktElement.appendChild(telefonnummerElement);

                besucherElement.appendChild(kontaktElement);
                feedbackElement.appendChild(besucherElement);

                Element bewertungElement = doc.createElement("bewertung");
                bewertungElement.setAttribute("erneuter_besuch", fb.get("wiederbesuchen"));
                bewertungElement.setAttribute("note_inhalt", fb.get("note_inhalt"));
                bewertungElement.setAttribute("note_aussehen", fb.get("note_aussehen"));

                Element vorschlagElement = doc.createElement("vorschlag");
                vorschlagElement.setTextContent(fb.get("verbesserung"));
                bewertungElement.appendChild(vorschlagElement);

                feedbackElement.appendChild(bewertungElement);

                Element infoElement = doc.createElement("info");

                Element emailGesendetElement = doc.createElement("email-gesendet");
                emailGesendetElement.setTextContent(fb.get("kopie"));
                infoElement.appendChild(emailGesendetElement);

//                Element datumElement = doc.createElement("datum");
//                datumElement.setTextContent(fb.get());
//                infoElement.appendChild(datumElement);

//                Element uhrzeitElement = doc.createElement("uhrzeit");
//                uhrzeitElement.setTextContent(feedback.getInfoUhrzeit());
//                infoElement.appendChild(uhrzeitElement);

                feedbackElement.appendChild(infoElement);

                rootElement.appendChild(feedbackElement);
            }

            // Create the entwickler_parser element
//            Element entwicklerParserElement = doc.createElement("entwickler_parser");
//            entwicklerParserElement.setTextContent("your_developer_parser_data_here");
//            rootElement.appendChild(entwicklerParserElement);

            // Save the XML document to a file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(url));
            transformer.transform(source, result);

            System.out.println("XML file written successfully!");


        }catch (Exception e){
            System.out.println(e);
        }


    }
}
