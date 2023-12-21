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
                try {
                    Element feedbackElement = doc.createElement("feedback");

                    var besucherElement = doc.createElement("besucher");
                    besucherElement.setAttribute("anrede", fb.get("anrede"));
                    besucherElement.setAttribute("vorname", fb.get("vorname"));
                    besucherElement.setAttribute("nachname", fb.get("nachname"));

                    Element alterElement = doc.createElement("alter");
                    alterElement.setTextContent(fb.get("alter"));
                    besucherElement.appendChild(alterElement);

                    Element kontaktElement = doc.createElement("kontakt");
                    kontaktElement.setAttribute("rueckfrage_erlaubt", fb.get("kopie").isEmpty() ? "nein" : "ja");

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
                    bewertungElement.setAttribute("erneuter_besuch", fb.get("wiederbesuchen") == null || fb.get("wiederbesuchen").isEmpty() ? "nein" : "ja");
                    bewertungElement.setAttribute("note_inhalt", noteInhaltUmrechnen(fb.get("note_inhalt")));
                    bewertungElement.setAttribute("note_aussehen", fb.get("note_aussehen"));

                    Element vorschlagElement = doc.createElement("vorschlag");
                    vorschlagElement.setTextContent(fb.get("verbesserung"));
                    bewertungElement.appendChild(vorschlagElement);

                    feedbackElement.appendChild(bewertungElement);

                    Element infoElement = doc.createElement("info");

                    Element emailGesendetElement = doc.createElement("email-gesendet");
                    emailGesendetElement.setTextContent(fb.get("kopie"));
                    infoElement.appendChild(emailGesendetElement);

                    Element datumElement = doc.createElement("datum");
                    datumElement.setTextContent(fb.get("datum").split("_")[0]);
                    infoElement.appendChild(datumElement);

                    Element uhrzeitElement = doc.createElement("uhrzeit");
                    uhrzeitElement.setTextContent(fb.get("datum").split("_")[1]);
                    infoElement.appendChild(uhrzeitElement);

                    //title added
                    Element title = doc.createElement("title");
                    title.setTextContent("Feedback" + fb.get("datum").replace(".", "_").replace(":", "_"));
                    infoElement.appendChild(title);

                    feedbackElement.appendChild(infoElement);

                    rootElement.appendChild(feedbackElement);
                }catch (Exception e) {
                    System.out.println(e);
                }
            }

            // Create the entwickler_parser element
            Element entwicklerParserElement = doc.createElement("entwickler_parser");
            entwicklerParserElement.setTextContent("developer_parser_data");
            rootElement.appendChild(entwicklerParserElement);

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

    public static String noteInhaltUmrechnen(String note){
        switch (Integer.parseInt(note)){
            case 1:
                return "sehr_gut";
            case 2:
                return "gut";
            case 3:
                return "befriedigend";
            case 4:
                return "ausreichend";
            case 5:
                return "mangelhaft";
            case 6:
                return "ungenuegend";
        }
        return "";
    }
}
