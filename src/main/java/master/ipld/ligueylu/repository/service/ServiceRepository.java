package master.ipld.ligueylu.repository.service;

import master.ipld.ligueylu.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
