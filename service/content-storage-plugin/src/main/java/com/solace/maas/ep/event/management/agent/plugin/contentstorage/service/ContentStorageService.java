package com.solace.maas.ep.event.management.agent.plugin.contentstorage.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.value.BinaryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.IntStream;

@Service
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class ContentStorageService {
    private final JcrSessionService jcrSessionService;

    @Autowired
    public ContentStorageService(JcrSessionService jcrSessionService) {
        this.jcrSessionService = jcrSessionService;
    }

    public String uploadFile(InputStream is, Path path, String mimeType) throws RepositoryException, IOException {
        Session session = jcrSessionService.createSession();

        createFolderNode(path.getParent().toString(), NodeType.NT_FOLDER, session);

        Node node = session.getNode(FilenameUtils.separatorsToUnix(path.getParent().toString()));
        Node fileNode = node.addNode(path.getFileName().toString(), NodeType.NT_FILE);

        Node dataNode = fileNode.addNode(JcrConstants.JCR_CONTENT, NodeType.NT_RESOURCE);
        dataNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
        dataNode.setProperty(JcrConstants.JCR_ENCODING, "");
        dataNode.setProperty(JcrConstants.JCR_DATA, new BinaryImpl(is));
        dataNode.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());

        session.save();

        String jcrPath = FilenameUtils.separatorsToUnix(path.toString());

        Node savedNode = session.getNode(jcrPath);
        Node blobNode = savedNode.getNode(JcrConstants.JCR_CONTENT);
        Property uuidProp = blobNode.getProperty((JcrConstants.JCR_UUID));

        return uuidProp.getValue().toString();
    }

    private Node createFolderNode(String folderPath, String type, Session session) throws RepositoryException {
        Path path = Paths.get(folderPath);
        int numFolders = path.getNameCount();

        Node currNode = session.getRootNode();

        for (int i = 0; i < numFolders; i++) {
            Path currPath = path.subpath(i, i + 1);
            String pathName = currPath.getFileName().toString();

            currNode = !currNode.hasNode(pathName) ? currNode.addNode(pathName, type) :
                    currNode.getNode(pathName);
        }

        return currNode;

//        return IntStream.range(0, numFolders)
//                .mapToObj(i -> {
//                    Path currPath = path.subpath(i, i + 1);
//                    String pathName = currPath.getFileName().toString();
//
//                    try {
//                        return !rootNode.hasNode(pathName) ? rootNode.addNode(pathName, type) :
//                                rootNode.getNode(pathName);
//                    } catch (RepositoryException e) {
//                        e.printStackTrace();
//                    }
//
//                    return null;
//                }).findFirst()
//                .orElseThrow(() -> new RuntimeException("Error creating file!"));
    }
}
