import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFToImage;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.rendering.ImageType;


class MyPDFparser {
    
    private PDFParser parser;
    private PDFTextStripper pdfStripper;
    private PDDocument pdDoc ;
    private COSDocument cosDoc ;
   
    private String Text ;
    private String filePath;
    private File file;
    
    public MyPDFparser() {
        
    }
    
    public String toText(int firstPage, int lastPage) throws IOException {
        this.pdfStripper = null;
        this.pdDoc = null;
        this.cosDoc = null;
        
        file = new File(filePath);
        parser = new PDFParser(new RandomAccessFile(file,"r"));
        
        parser.parse();
        cosDoc = parser.getDocument();
        pdfStripper = new PDFTextStripper();
        pdDoc = new PDDocument(cosDoc);
        pdDoc.getNumberOfPages();
        pdfStripper.setStartPage(firstPage);
        pdfStripper.setEndPage(lastPage);
        
        Text = pdfStripper.getText(pdDoc);
        return Text;
    }
    
    /**
     * Set File path
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * PDF to JPG
     *
    */
    public void toPNG(int page) throws IOException {
        file = new File(filePath);
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
       // for (int page = 0; page < document.getNumberOfPages(); ++page) { 
       
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            // suffix in filename will be used as the file format
            ImageIOUtil.writeImage(bim, file + "-" + page + ".png", 300);
       // }
        document.close();
    }
    
    
    
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        File outputfile = new File("text-page-output.txt");
                
        MyPDFparser pdfManager = new MyPDFparser();
        pdfManager.setFilePath("testbook.pdf");
        
        //Parse page to png
        pdfManager.toPNG(2);
       
       
        //parse page to text
        try {
        //checking the file and creating if is not exists
        if(!outputfile.exists()){
            outputfile.createNewFile();
        }
        
        PrintWriter out = new PrintWriter(outputfile.getAbsoluteFile());
        
        try {
            out.print(pdfManager.toText(1, 5));
        } finally {
            out.close();
        }
        
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

    }
}
