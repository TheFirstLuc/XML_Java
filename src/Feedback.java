import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

class Feedback {

    public ArrayList<HashMap<String, String>> content;

    public Feedback(ArrayList<Document> documents) {

        content = extractChildren(documents);

        content.forEach(System.out::println);
    }

    private ArrayList<HashMap<String, String>> extractChildren(ArrayList<Document> xmlDocuments) {
        ArrayList<HashMap<String, String>> resultList = new ArrayList<>();

        for (Document document : xmlDocuments) {
            HashMap<String, String> childrenMap = new HashMap<>();
            NodeList childNodes = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    childrenMap.put(element.getTagName(), element.getTextContent());
                }
            }

            resultList.add(childrenMap);
        }

        return resultList;
    }

    public double averageNoteInhalt() {

        double a = 0,b = 0; // a/b == average

        for (var c : content){
            for (var d: c.keySet()){
                if(d.equals("note_inhalt") && c.containsKey(d)) {
                    a += Integer.parseInt(c.get(d));
                    b++;
                }
            }
        }

        return a/b;
    }

    public double calculateReturningVisitorsPercentage(){
        return 0;
    }



}