package br.tec.gtech.pdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.tec.gtech.pdf.model.Template;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByName(String name);
}