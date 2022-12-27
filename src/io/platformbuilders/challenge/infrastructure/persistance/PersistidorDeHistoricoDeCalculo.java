package io.platformbuilders.challenge.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistidorDeHistoricoDeCalculo extends JpaRepository<HistoricoCalculoJuros, Long> {
}
