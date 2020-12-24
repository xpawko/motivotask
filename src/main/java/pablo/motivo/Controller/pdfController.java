package pablo.motivo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pablo.motivo.Repository.PdfRepository;
import pablo.motivo.model.Pdf;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Controller

public class pdfController {


    @Autowired
    PdfRepository pdfRepository;


    @GetMapping("/pdfs")
    public String showAll(@RequestParam(value = "message", required = false) String message, Model model) {


        //  pdfRepository.save(new Pdf("nazwa", "jakis opis", new Date(), "2KB", null));

        var pdfs = (List<Pdf>) pdfRepository.findAll();
        var pdf = new Pdf();
        var somepdf = model.addAttribute("pdfs", pdfs);


        model.addAttribute("pdf", pdf);
        model.addAttribute("somepdf", somepdf);
        model.addAttribute("message", message);

        return "pdfs";
    }


    @Transactional
    @PostMapping("/savepdf")
    public String addPdf(@ModelAttribute Pdf pdf, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws SQLException {


        if (pdfRepository.findPdfByFileName(pdf.getFileName()) != null) {

            redirectAttributes.addAttribute("message", "nazwa musi być unikalna");
            return "redirect:pdfs";
        }


        Blob res = null;

        try {
            InputStream inputStream = file.getInputStream();
            res = new SerialBlob(StreamUtils.copyToByteArray(inputStream));


            if (file.getSize() == 0 || pdf.getFileName().equals("")) {
                redirectAttributes.addAttribute("message", "nazwa ani plik nie  mogą  być puste");
                return "redirect:pdfs";
            }


            if (pdfRepository.findAllByFileSize(file.getSize() + "KB") != null) {


                for (Pdf x : pdfRepository.findAllByFileSize(file.getSize() + "KB")) {

                    if (getMD5(x.getContent()).equals(getMD5(res))) {

                        redirectAttributes.addAttribute("message", "plik już istnieje w bazie danych");
                        return "redirect:pdfs";
                    }
                }
            }


            pdfRepository.save(new Pdf(pdf.getFileName(), pdf.getDescription(), new Date(), file.getSize() + "KB", res));


        } catch (Exception e) {
            System.out.println("cant load file: " + e.getMessage());
        }
        return "redirect:pdfs";
    }

    @GetMapping("/remove/{id}")
    public String removePdf(@PathVariable long id) {

        pdfRepository.delete(pdfRepository.findByPdfId(id));

        return "redirect:/pdfs";
    }


    public String getMD5(Blob res) {

        MessageDigest md = null;

        try {
            DigestInputStream dis = new DigestInputStream(res.getBinaryStream(), MessageDigest.getInstance("MD5"));
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();


        } catch (NoSuchAlgorithmException | IOException | SQLException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {

            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
