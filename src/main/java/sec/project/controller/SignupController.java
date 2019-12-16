package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @PersistenceContext
    EntityManager entityManager;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Model model, @RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        String query = "SELECT * FROM Signup WHERE Name = '" + name + "'";
        List<Signup> signups = entityManager.createNativeQuery(query, Signup.class).getResultList();
        signups.forEach(System.out::println);
        System.out.println(query);
        model.addAttribute("name", name);
        model.addAttribute("signups", signups);
        return "done";
    }

}
