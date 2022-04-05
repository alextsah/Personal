import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.*;
import javafx.scene.chart.*;
import java.awt.image.*;
import javafx.embed.swing.*;
import javax.imageio.ImageIO;
import java.util.logging.*;
import java.io.*; 
import javafx.scene.image.*;
import javafx.scene.*;

public class Pdf
{
    private PDDocument doc;
    
    Pdf() //constructor 
    {
        doc = new PDDocument();
    }
    
    public PDDocument getDoc() //accessor method 
    {
        return doc;
    }
    
    public void setDoc(PDDocument doc) //mutator method 
    {
        this.doc=doc;
    }
    
    public void addChart(Chart ch) 
    {
        PDPage page = new PDPage(); //new page 
        PDImageXObject pdimage;
        PDPageContentStream content;

        WritableImage nodeshot = ch.snapshot(new SnapshotParameters(), null);
        File file = new File("chart.png"); //extrapolation from png generated via JavaFX
        try
        {ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);}
        catch(IOException e){} 
        try {
            pdimage = PDImageXObject.createFromFile("chart.png",doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 50, 200, 500, 400); //center image 
            content.close(); //close editor 
            doc.addPage(page); //add page 
            file.delete(); //delete previous file 
        }catch(IOException ex){Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);}
    }
}