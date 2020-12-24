package pablo.motivo.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pablo.motivo.model.Pdf;

import java.sql.Blob;
import java.util.List;


@Repository
public interface PdfRepository extends CrudRepository<Pdf,Long> {



    public Pdf findByPdfId(long id);
    public Pdf findPdfByFileName(String filename);
    public List<Pdf> findAllByFileSize(String size);


}
