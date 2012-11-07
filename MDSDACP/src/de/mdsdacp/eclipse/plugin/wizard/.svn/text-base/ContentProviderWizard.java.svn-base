package de.mdsdacp.eclipse.plugin.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import de.mdsdacp.eclipse.plugin.Activator;

/**
 * ContentProviderWizard
 * Wizard functionality: creates the model folder and the ecore + ecorediag 
 * 
 * @author Frederik Goetz
 */
public class ContentProviderWizard extends Wizard implements INewWizard {
    private ContentProviderWizardPage page;
    private ISelection selection;

    public ContentProviderWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        page = new ContentProviderWizardPage(selection);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        final String containerName = page.getContainerName();
        final String fileName = page.getFileName();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerName, fileName, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));

        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + containerName + "\" does not exist.");
        }

        IContainer container = (IContainer) resource;

        final IFolder folder = container.getFolder(new Path("model"));
        if (!folder.exists()) {
            folder.create(true, true, monitor);
        }

        final IFile ecoreFile = folder.getFile(fileName);
        try {
            InputStream stream = getEcoreFileContent();
            if (ecoreFile.exists()) {
                ecoreFile.setContents(stream, true, true, monitor);
            } else {
                ecoreFile.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final IFile ecoreDiagFile = folder.getFile(fileName.replace(".ecore", ".ecorediag"));
        try {
            InputStream stream = getEcoreDiagFileContent(fileName);
            if (ecoreDiagFile.exists()) {
                ecoreDiagFile.setContents(stream, true, true, monitor);
            } else {
                ecoreDiagFile.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        monitor.worked(1);
    }

    private InputStream getEcoreFileContent() {
        try {
            return Activator.getDefault().getBundle().getEntry("/DefaultEcoreFiles/ecore").openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getEcoreDiagFileContent(String filename) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(Activator.getDefault().getBundle().getEntry("/DefaultEcoreFiles/ecorediag")
                    .openStream());

            NamedNodeMap filenameAttributes = doc.getElementsByTagName("element").item(0).getAttributes();
            Attr fileAttr = doc.createAttribute("href");
            fileAttr.setValue(filename + "#/");
            filenameAttributes.setNamedItem(fileAttr);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "Android Content Provider", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
