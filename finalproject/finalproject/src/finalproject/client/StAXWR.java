package finalproject.client;

import finalproject.shared.Item;

import javax.xml.stream.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ArrayList<Item> loadItems(InputStream input, String owner) throws XMLStreamException {
        reader = xmlInputFactory.createXMLStreamReader(input);
        ArrayList<Item> myItems = new ArrayList<>();
        String buffer = "";
        Item bufferItem = null;
        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    buffer = reader.getLocalName();
                    if (buffer.equals("item")) {
                        bufferItem = new Item();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (bufferItem != null && !reader.isWhiteSpace()) {
                        String text = reader.getText().trim();

                        switch (buffer) {
                            case "id":
                                bufferItem.setId(Integer.parseInt(text));
                                break;
                            case "name":
                                bufferItem.setName(text);
                                break;
                            case "price":
                                bufferItem.setPrice(Integer.parseInt(text));
                                break;
                            case "description":
                                bufferItem.setDescription(text);
                                break;
                            case "image":
                                bufferItem.setPic(encodeImage(text));
                                break;
                        }
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("item") && bufferItem != null) {
                        bufferItem.setOwner(owner);
                        myItems.add(bufferItem);
                        bufferItem = null;
                    }
                    break;
            }
        }

            reader.close();

        return myItems;
    }

    private byte[] encodeImage(String url) {
        Path path = Paths.get(url);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
