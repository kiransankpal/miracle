package view.bean;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class QueueToPdf {
    public QueueToPdf() {
        super();
    }
    

    public static void main(String[] args) {
    try {
        File fileIn =
            new File("C://JDeveloper//mywork//tiff//temp-file-name3854394719278213820.tif");
    new QueueToPdf().generatePdfFromTifPbox(fileIn);
    } catch (Exception e) {
    e.printStackTrace();
    }
    }

    public void generatePdfFromTifPbox(File file) throws Exception {
    PDDocument doc = new PDDocument();
    List<BufferedImage> bimages = Imaging.getAllBufferedImages(file);
    for (BufferedImage bi : bimages) {
    PDPage page = new PDPage();
    doc.addPage(page);
    PDPageContentStream contentStream = new PDPageContentStream(doc, page);
    try {
    // the .08F can be tweaked. Go up for better quality,
    // but the size of the PDF will increase
    PDImageXObject image = JPEGFactory.createFromImage(doc, bi, 0.08f);
    Dimension scaledDim = getScaledDimension(new Dimension(image.getWidth(), image.getHeight()),
    new Dimension((int) page.getMediaBox().getWidth(), (int) page.getMediaBox().getHeight()));
    contentStream.drawImage(image, 1, 1, scaledDim.width, scaledDim.height);
    } finally {
    contentStream.close();
    }
    }
    System.out.println(file.getAbsolutePath());
    doc.save(file.getAbsolutePath() + ".pdf");
    }

    // taken from a stack overflow post
    // http://stackoverflow.com/questions/23223716/scaled-image-blurry-in-pdfbox
    // Thanks Gyo!
    private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
    // scale width to fit
    new_width = bound_width;
    // scale height to maintain aspect ratio
    new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
    // scale height to fit instead
    new_height = bound_height;
    // scale width to maintain aspect ratio
    new_width = (new_height * original_width) / original_height;
    }

    return new Dimension(new_width, new_height);
    }
}
