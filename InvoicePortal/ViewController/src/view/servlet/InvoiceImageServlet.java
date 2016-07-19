package view.servlet;


import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.BlobDomain;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class InvoiceImageServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/pdf";
    @SuppressWarnings("compatibility:-7117235625143767421")
    private static final long serialVersionUID = 231067087011842484L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        System.out.println("doGet invoked with request invoiceId" +
                           request.getParameter("invoiceId"));
        response.reset();
        response.setContentType(CONTENT_TYPE);
        String invoiceId = request.getParameter("invoiceId");

        String amDef = "model.am.CommonAppModule";
        String config = "CommonAppModuleLocal";
        ApplicationModule am =
            Configuration.createRootApplicationModule(amDef, config);
        ViewObjectImpl vo =
            (ViewObjectImpl)am.findViewObject("InvoiceCopyPrintVO"); // get view object (the same as used in the table)
        oracle.jbo.domain.Number pInvoiceId = null;
        try {
            pInvoiceId = new oracle.jbo.domain.Number(invoiceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vo.setNamedWhereClauseParam("bind_invoice_id", pInvoiceId);
        vo.executeQuery();
        boolean isImagePresent = false;
        
        if (vo.hasNext()) {

            Row encodedDocumentRow = vo.first();
            BlobDomain image =
                (BlobDomain)encodedDocumentRow.getAttribute("Encodeddocument");
            if(image != null){
                isImagePresent = true;
            }
            
        }
        
        if (isImagePresent) {

            Row encodedDocumentRow = vo.first();
            BlobDomain image =
                (BlobDomain)encodedDocumentRow.getAttribute("Encodeddocument");
            
                
                OutputStream output = response.getOutputStream();
                byte[] buf = new byte[1024];

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = image.getBinaryStream();
                int n = 0;
                while ((n = in.read(buf)) >= 0) {
                    baos.write(buf, 0, n);
                }

                PDDocument doc = new PDDocument();
                List<BufferedImage> bimages = null;
                try {
                    bimages = Imaging.getAllBufferedImages(baos.toByteArray());
                } catch (ImageReadException e) {
                    e.printStackTrace();
                }
                for (BufferedImage bi : bimages) {
                    PDPage page = new PDPage();
                    doc.addPage(page);
                    PDPageContentStream contentStream =
                        new PDPageContentStream(doc, page);
                    try {
                        PDImageXObject pdfImage =
                            JPEGFactory.createFromImage(doc, bi, 0.08f);
                        Dimension scaledDim =
                            getScaledDimension(new Dimension(pdfImage.getWidth(),
                                                             pdfImage.getHeight()),
                                               new Dimension((int)page.getMediaBox().getWidth(),
                                                             (int)page.getMediaBox().getHeight()));
                        contentStream.drawImage(pdfImage, 1, 1,
                                                scaledDim.width,
                                                scaledDim.height);
                    } finally {
                        contentStream.close();
                    }
                    doc.save(output);
                }
                in.close();
                doc.close();
                baos.close();
                baos.flush();
                output.close();
                output.flush();
        } else {
            printBlankResponse(response);
        }


        Configuration.releaseRootApplicationModule(am, false);
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Dimension getScaledDimension(Dimension imgSize,
                                         Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    private void printBlankResponse(HttpServletResponse response) {
        response.reset();
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("<html>");
        out.println("<head><title>Invoice</title></head>");
        out.println("<body>");
        out.println("<p>No Invoice Attached.</p>");
        out.println("</body></html>");
        out.close();
        out.flush();
    }
}
