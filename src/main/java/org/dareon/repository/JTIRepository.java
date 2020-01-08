package org.dareon.repository;

import org.dareon.domain.JTI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JTIRepository extends JpaRepository<JTI, Long>
{

    JTI findByJti(String jti);

}
