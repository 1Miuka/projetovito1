package catinder.website.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import catinder.website.model.Cat;

@Controller
public class WebsiteHome {

    private final RestTemplate restTemplate;

    // Constructor injection of RestTemplate
    public WebsiteHome(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/addcat")
    public String addCatForm(Model model) {
        try {
            ResponseEntity<Cat[]> response = restTemplate.getForEntity("http://localhost:8081/api/cats", Cat[].class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("systemDown", true);
            }
        } catch (Exception e) {
            model.addAttribute("systemDown", true);
        }
        return "add_cat";
    }

    @GetMapping("/miau")
    public String miauPage(Model model) {
        try {
            ResponseEntity<Cat[]> response = restTemplate.getForEntity("http://localhost:8081/api/cats", Cat[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Cat> cats = Arrays.asList(response.getBody());
                model.addAttribute("cats", cats);
                return "miau";
            }
        } catch (Exception e) {
            model.addAttribute("systemDown", true);
        }
        return "miau";
    }

    @PostMapping("/delete/{id}")
    public String deleteCat(@PathVariable Long id, Model model) {
        // Enviar solicitação para o Sistema 2 para excluir o gato
        restTemplate.delete("http://localhost:8081/api/cats/" + id);

        // Redirecionar de volta para a página 'miau'
        return "redirect:/miau";
    }

    @PostMapping("/add")
    public String addCat(@RequestParam("nome") String nome,
            @RequestParam("idade") int idade,
            @RequestParam("apelido") String apelido,
            @RequestParam("endereco") String endereco,
            @RequestParam("numeroContato") String numeroContato,
            @RequestParam("sexo") String sexo,
            Model model) {

        Cat cat = new Cat();
        cat.setNome(nome);
        cat.setIdade(idade);
        cat.setApelido(apelido);
        cat.setEndereco(endereco);
        cat.setNumeroContato(numeroContato);
        cat.setSexo(sexo);

        // Sending request to System 2
        ResponseEntity<Cat> response = restTemplate.postForEntity("http://localhost:8081/api/cats", cat, Cat.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("cat", response.getBody());
            return "redirect:/miau";
        } else {
            // Handle failure to add the cat
            return "error-page";
        }
    }
}
