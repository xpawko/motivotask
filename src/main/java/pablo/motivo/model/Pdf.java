package pablo.motivo.model;


import javax.persistence.*;
import java.awt.*;
//import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name= "pdf")
public class Pdf {





@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private Long pdfId;


private String fileName;
private String description;
private Date loadDate;
private String fileSize;
@Lob
private byte[] content;



    public Pdf(String fileName, String description, Date loadDate, String fileSize, byte[] content) {
        this.fileName=fileName;
        this.description = description;
        this.loadDate = loadDate;
        this.fileSize = fileSize;
        this.content=content;
    }




    public Pdf(String fileName, String description, Date loadDate, String fileSize) {
        this.fileName = fileName;
        this.description = description;
        this.loadDate = loadDate;
        this.fileSize = fileSize;
    }

    public Pdf(){}

    public Long getPdfId() {
        return pdfId;
    }

    public void setPdfId(Long pdfId) {
        this.pdfId = pdfId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }


}
