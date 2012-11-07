package de.mdsdacp.eclipse.plugin.menu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.mdsdacp.eclipse.plugin.util.ContentValues;
import de.mdsdacp.launch.ACPGenLauncher;

/**
 * GerneateHandler
 * Used to generate the content provider from the ecore model 
 * 
 * @author Frederik Goetz
 */
public class GenerateHandler extends AbstractHandler {
    private static final String TAG = "de.mdsdacp.eclipse.plugin.menu.GenerateHandler";
    private static final boolean DEBUG = false;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (DEBUG) {
            System.out.println("DEBUG: " + TAG);
        }

        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveSite(event).getSelectionProvider()
                .getSelection();
        Object obj = selection.getFirstElement();

        if (obj instanceof IFile) {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
            IProgressMonitor monitor = dialog.getProgressMonitor();

            dialog.open();
            monitor.beginTask("Generate content provider...", 0);

            IFile file = (IFile) obj;
            try {
                monitor.subTask("Create source folder...");
                monitor.worked(1);

                IContainer container = file.getProject();

                final IFolder srcGenFolder = container.getFolder(new Path(ContentValues.SRC_GEN_PATH));

                if (!srcGenFolder.exists()) {
                    try {
                        createSrcGen(container);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String ecorePath = file.getLocation().toOSString();
                String projectSrcPath = getSourceFolderPathFromClasspath(file.getProject().getFile(".classpath")
                        .getContents());
                String srcGenPath = file.getProject().getFolder(projectSrcPath).getLocation().toOSString();

                monitor.subTask("Generate content provider ...");
                monitor.worked(2);

                ACPGenLauncher.run(null, ecorePath, srcGenPath);

                file.getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CoreException e) {
                e.printStackTrace();
            } finally {
                monitor.done();
                dialog.close();
            }
        }

        return null;
    }

    private void createSrcGen(final IContainer container) throws Exception {
        final IFolder folder = container.getFolder(new Path(ContentValues.SRC_GEN_PATH));
        if (!folder.exists()) {
            folder.create(true, true, null);
        }
        addSrcGenFolderToClasspath(container);
    }

    private void addSrcGenFolderToClasspath(IContainer container) throws Exception {
        IFile classpath = container.getFile(new Path(".classpath"));

        if (getSourceFolderPathFromClasspath(classpath.getContents()) == null) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(classpath.getContents());

            Element element = doc.createElement("classpathentry");
            element.setAttribute("kind", "src");
            element.setAttribute("path", ContentValues.SRC_GEN_PATH);

            doc.getElementsByTagName("classpath").item(0).appendChild(element);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);

            classpath.setContents(new ByteArrayInputStream(outputStream.toByteArray()), true, true, null);
        }
    }

    private String getSourceFolderPathFromClasspath(InputStream is) throws ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(is);
        NodeList classpathEntries = doc.getElementsByTagName("classpathentry");
        for (int i = 0; i < classpathEntries.getLength(); i++) {
            NamedNodeMap map = classpathEntries.item(i).getAttributes();
            if (map.getNamedItem("kind").getNodeName().equals("kind")
                    && map.getNamedItem("kind").getNodeValue().equals("src")) {
                if (map.getNamedItem("path").getNodeValue().equals(ContentValues.SRC_GEN_PATH)) {
                    return map.getNamedItem("path").getNodeValue();
                }
            }
        }
        return null;
    }
}
