package pablo.motivo.Controller;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pablo.motivo.Repository.PdfRepository;
import pablo.motivo.model.Pdf;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Controller

public class pdfController {


    @Autowired
    PdfRepository pdfRepository;


    @Transactional
    @GetMapping("/pdfs")
    public String showAll(@RequestParam(value = "message", required = false) String message, Model model) {


        var pdfs = (List<Pdf>) pdfRepository.findAll();
        var pdf = new Pdf();

        model.addAttribute("pdfs", pdfs);
        model.addAttribute("pdf", pdf);
        model.addAttribute("message", message);

        return "pdfs";
    }

    @Transactional
    @PostMapping("/savepdf")
    public String addPdf(@ModelAttribute Pdf pdf, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {




        if (!file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).equals("pdf")) {

            redirectAttributes.addAttribute("message", "plik nie jest w formacie pdf.");
            return "redirect:pdfs";
        }

        else if (pdfRepository.findPdfByFileName(pdf.getFileName()) != null) {

            redirectAttributes.addAttribute("message", "nazwa musi być unikalna");
            return "redirect:pdfs";
        }

        try {


            byte[] pdfInBytes = file.getBytes();


            if (file.getSize() == 0 || pdf.getFileName().equals("")) {
                redirectAttributes.addAttribute("message", "nazwa ani plik nie  mogą  być puste");
                return "redirect:pdfs";
            }

            if (pdfRepository.findAllByFileSize(file.getSize() + "KB") != null) {

                for (Pdf x : pdfRepository.findAllByFileSize(file.getSize() + "KB")) {

                    if (Arrays.equals(x.getContent(), pdfInBytes)) {
                        redirectAttributes.addAttribute("message", "plik już istnieje w bazie danych");
                        return "redirect:pdfs";
                    }
                }
            }


            pdfRepository.save(new Pdf(pdf.getFileName(), pdf.getDescription(), new Date(), file.getSize() + "KB", pdfInBytes));


        } catch (Exception e) {
            System.out.println("cant load file: " + e.getMessage());
        }
        return "redirect:pdfs";
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public String removePdf(@PathVariable long id) {

        pdfRepository.delete(pdfRepository.findByPdfId(id));

        return "redirect:/pdfs";
    }



    @Transactional
    @GetMapping(value = "pdfs/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable("id") long id) {


        byte[] data = pdfRepository.findByPdfId(id).getContent();


        try {

            File tempfile = new File("src/main/resources/temppdf.tmp");

            OutputStream os = new FileOutputStream(tempfile);
            os.write(data);
            os.close();

            PDDocument document = PDDocument.load(tempfile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            PDPage pdpage = document.getPage(0);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            document.close();


            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            ImageIO.write(bim, "jpg", bao);

            tempfile.delete();
            return bao.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

