package master.ipld.ligueylu.repository.reservation;

import master.ipld.ligueylu.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
