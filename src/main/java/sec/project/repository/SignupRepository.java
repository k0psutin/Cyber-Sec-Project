package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sec.project.domain.Signup;

import java.util.*;

public interface SignupRepository extends JpaRepository<Signup, Long> {
    
}
