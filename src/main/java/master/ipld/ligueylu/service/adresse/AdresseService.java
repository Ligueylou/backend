package master.ipld.ligueylu.service.adresse;

import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.exception.ResourceAlreadyExistException;
import master.ipld.ligueylu.exception.ResourceNotFoundException;
import master.ipld.ligueylu.model.Adresse;
import master.ipld.ligueylu.repository.adresse.AdresseRepository;
import master.ipld.ligueylu.request.AddAdresseRequest;
import master.ipld.ligueylu.request.AdresseUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdresseService implements  IAdresseService{
    private final AdresseRepository adresseRepository;
    @Override
    public Adresse addAdresse(AddAdresseRequest request) {
        boolean existed = adresseRepository.findByNumeroAndRueAndVilleAndCodePostalAndPays(
                request.getNumero(),
                request.getRue(),
                request.getVille(),
                request.getCodePostal(),
                request.getPays()
        ).isPresent();
        if(existed){
            throw new ResourceAlreadyExistException("Adresse already exists");
        }
        return adresseRepository.save(createAdresse(request));
    }
    public Adresse createAdresse(AddAdresseRequest request) {
        return new Adresse(
                request.getNumero(),
                request.getCodePostal(),
                request.getVille(),
                request.getPays(),
                request.getRue()
        );
    }

    @Override
    public Adresse getAdresseById(Long adresseId) {
        return adresseRepository.findById(adresseId)
                .orElseThrow(()-> new ResourceNotFoundException("Adresse Not Found !"));
    }

    @Override
    public List<Adresse> getAllAdresses() {
        return adresseRepository.findAll();
    }

    @Override
    public List<Adresse> getAdressesByVille(String ville) {
        return adresseRepository.findByVille(ville);
    }

    @Override
    public List<Adresse> getAdressesByVilleAndRue(String ville, String rue) {
        return adresseRepository.findByVilleAndRue(ville, rue);
    }

    @Override
    public Adresse updateAdresse(AdresseUpdateRequest request, Long id) {
        return adresseRepository.findById(id)
                .map(existingAdresse -> updateExistingAdresse(existingAdresse,request))
                .map(adresseRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse Not Found"));
    }
    public Adresse updateExistingAdresse(Adresse existingAdresse, AdresseUpdateRequest request) {
        existingAdresse.setNumero(request.getNumero());
        existingAdresse.setCodePostal(request.getCodePostal());
        existingAdresse.setVille(request.getVille());
        existingAdresse.setPays(request.getPays());
        existingAdresse.setRue(request.getRue());
        return existingAdresse;
    }

    @Override
    public void deleteAdresse(Long id) {
         adresseRepository.findById(id)
                 .ifPresentOrElse(adresseRepository::delete,
                         () -> {throw new ResourceNotFoundException("Adresse Not Found !");});
    }
}
