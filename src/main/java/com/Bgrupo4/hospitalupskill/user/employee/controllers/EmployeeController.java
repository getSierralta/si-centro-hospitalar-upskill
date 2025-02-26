package com.Bgrupo4.hospitalupskill.user.employee.controllers;

import com.Bgrupo4.hospitalupskill.calendario.CalendarioService;
import com.Bgrupo4.hospitalupskill.invoices.InvoiceController;
import com.Bgrupo4.hospitalupskill.senha.FakeSenha;
import com.Bgrupo4.hospitalupskill.invoices.InvoiceRequest;
import com.Bgrupo4.hospitalupskill.invoices.InvoiceService;
import com.Bgrupo4.hospitalupskill.senha.Senha;
import com.Bgrupo4.hospitalupskill.senha.SenhaRequest;
import com.Bgrupo4.hospitalupskill.senha.SenhaService;
import com.Bgrupo4.hospitalupskill.user.employee.Employee;
import com.Bgrupo4.hospitalupskill.user.employee.EmployeeService;
import com.Bgrupo4.hospitalupskill.user.utente.Utente;
import com.Bgrupo4.hospitalupskill.user.utente.UtenteService;
import com.Bgrupo4.hospitalupskill.user.utente.controllers.UtenteManagementController;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import static com.Bgrupo4.hospitalupskill.HospitalUpskillApplication.ECRA;

@Controller
@RequestMapping(path = "employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SenhaService senhaService;
    private final CalendarioService calendarioService;
    private final InvoiceService invoiceService;
    private final UtenteManagementController utenteManagementController;
    private final UtenteService utenteService;

    @GetMapping(value = "/show-all-utentes")
    public String showUtentes(ModelMap map) {
        map.put("utenteList", utenteManagementController.getAllUtentes());
        return "/employee/show-all-utentes";
    }
    @GetMapping(value = "/show-all-utentes/espera")
    public String showUtentesEspera(ModelMap map) {
        map.put("utenteList", utenteService.getAllUtentesAEspera());
        return "/employee/show-all-utentes";
    }
    @GetMapping(value = "/show-all-utentes/atrasados")
    public String showUtentesAtrasados(ModelMap map) {
        map.put("utenteList", utenteService.getAllUtentesAtrasados());
        return "/employee/show-all-utentes";
    }
    @GetMapping(value = "/show-all-utentes/atendidos")
    public String showUtentesAtendidos(ModelMap map) {
        map.put("utenteList", utenteService.getAllUtentesAtendidos());
        return "/employee/show-all-utentes";
    }
    @GetMapping(value = "/show-all-utentes/username/{username}")
    public String showUtente(ModelMap map, @PathVariable String username) {
        try {
            List<Utente> utentes = new ArrayList<>();
            utentes.add(utenteService.getUserByUsername(username).get());
            map.put("utenteList", utentes);
            return "/employee/show-all-utentes";
        }catch (Exception e){
            map.put("utenteList", utenteManagementController.getAllUtentes());
            return "/employee/show-all-utentes";
        }
    }
    @GetMapping(value = "/show-all-utentes/nif/{nif}")
    public String showUtenteNif(ModelMap map, @PathVariable String nif) {
        try {
            List<Utente> utentes = new ArrayList<>();
            utentes.add(utenteService.getUserByNif(nif));
            map.put("utenteList", utentes);
            return "/employee/show-all-utentes";
        }catch (Exception e){
            map.put("utenteList", utenteManagementController.getAllUtentes());
            return "/employee/show-all-utentes";
        }
    }
    @GetMapping(value = "/profile")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showProfile(ModelMap map) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeService.getLogged(auth);
        map.put("employee", employee);
        map.put("senhas", senhaService.getSenhas());
        map.put("salaDeEspera", ECRA);
        return "/employee/profile";
    }

    @GetMapping(path = "/chamar-proximo")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public RedirectView callNext() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeService.getLogged(auth);
        employeeService.callNextClient(employee);
        return new RedirectView("/employee/profile");
    }

    //GET do formulário
    @GetMapping(value = "/check-in")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showCheckInFormulario(ModelMap map){
        map.put("categorias", senhaService.getCategorias());
        return "/employee/check-in";
    }

    //POST do formulário
    @PostMapping(path = "/check-in", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public RedirectView getSenha(SenhaRequest request){
        try {
            Senha senha = senhaService.createSenha(request);
            return new RedirectView("/employee/check-in/"+senha.getId());
        }catch (Exception e){
            return new RedirectView("/employee/check-in/fake");
        }
    }

    @GetMapping(value = "/check-in/{id}")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showSenha(@PathVariable("id") Long id, ModelMap map){
        map.put("senha", senhaService.getSenhaById(id).get());
        return "/employee/senha";
    }

    @GetMapping(value = "/check-in/fake")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showSenhaFake(ModelMap map){
        map.put("senha", new FakeSenha());
        return "/employee/senha";
    }

    @GetMapping(value = "/formularioCalendario")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showFormularioCalendario(ModelMap map){
        map.put("especialidades", calendarioService.getEspecialidades());
        return "employee/formularioCalendario";
    }

    @GetMapping(value = "/calendarioemployee/{especialidade}")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showCalendarioGeral(@PathVariable String especialidade){
        return "employee/calendarioemployee";
    }

    @GetMapping(value = "/settings")
    @PreAuthorize("hasRole('ROLE_COLABORADOR')")
    public String showSetting(ModelMap map) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeService.getLogged(auth);
        map.put("employee", employee);
        return "employee/settings";
    }

    //GET do invoice
    @GetMapping(value = "/payments")
    @PreAuthorize("hasAnyRole('ROLE_COLABORADOR', 'ROLE_ADMIN', 'ROLE_RESPONSAVEL')")
    public String showBills(ModelMap map, @RequestParam (required = false) String search, @RequestParam (required = false) String status){
        map.put("invoiceList", invoiceService.getList(search, status));
        return "/employee/payments";
    }

    @GetMapping(value = "/new-bill")
    @PreAuthorize("hasAnyRole('ROLE_COLABORADOR', 'ROLE_ADMIN', 'ROLE_RESPONSAVEL')")
    public String createBill(){
        return "/employee/new-bill";
    }

    @GetMapping(value = "/success")
    @PreAuthorize("hasAnyRole('ROLE_COLABORADOR', 'ROLE_ADMIN', 'ROLE_RESPONSAVEL')")
    public String showSuccess() {
        return "/employee/success";
    }

    @GetMapping(value = "/error")
    @PreAuthorize("hasAnyRole('ROLE_COLABORADOR', 'ROLE_ADMIN', 'ROLE_RESPONSAVEL')")
    public String showError() {
        return "/employee/error";
    }

}
