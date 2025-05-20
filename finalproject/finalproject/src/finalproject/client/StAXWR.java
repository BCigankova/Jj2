package finalproject.client;

import finalproject.shared.Item;

import javax.xml.stream.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class StAXWR {
    private XMLInputFactory xmlInputFactory;
    private XMLStreamReader reader;
    private XMLOutputFactory xmlOutputFactory;
    private XMLStreamWriter xmlWriter;

    public StAXWR() {
        xmlInputFactory = XMLInputFactory.newInstance();
        xmlOutputFactory = XMLOutputFactory.newInstance();
    }

    public void storeItems(OutputStream outputStream, ArrayList<Item> myItems, ArrayList<Item> items) throws XMLStreamException {
        xmlWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);
        //separatne pro myItems a buyItems
        xmlWriter.writeStartElement("myItems");
        for (Item item : myItems) {
            xmlWriter.writeStartElement("item");

                xmlWriter.writeStartElement("id");
                    xmlWriter.writeCharacters(String.valueOf(item.getId()));
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("name");
                    xmlWriter.writeCharacters(item.getName());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("price");
                    xmlWriter.writeCharacters(String.valueOf(item.getPrice()));
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("description");
                    xmlWriter.writeCharacters(item.getDescription());
                xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("items");
        for (Item item : items) {
            xmlWriter.writeStartElement("item");

                xmlWriter.writeStartElement("id");
                    xmlWriter.writeCharacters(String.valueOf(item.getId()));
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("owner");
                    xmlWriter.writeCharacters(item.getOwner());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("name");
                    xmlWriter.writeCharacters(item.getName());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("price");
                    xmlWriter.writeCharacters(String.valueOf(item.getPrice()));
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("description");
                    xmlWriter.writeCharacters(item.getDescription());
                xmlWriter.writeEndElement();

            xmlWriter.writeEndElement(); // item
        }
        xmlWriter.writeEndElement(); // items
        xmlWriter.writeEndDocument();
    }

    public ArrayList<Item> loadItems(InputStream inputStream) throws XMLStreamException {
        ArrayList<Item> items = new ArrayList<>();
        return items;
    }

}
