package com.financontrol.carteira.controller;
import com.financontrol.carteira.model.entity.Lancamento;
import com.financontrol.carteira.model.entity.Usuario;
import com.financontrol.carteira.service.LancamentoService;
import com.financontrol.carteira.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Controller
public class LancamentoController {
    @Autowired
    LancamentoService lancamentoService;

    @Autowired
    UserService userService;

    @GetMapping("/lancamentos")
    public String todos(HttpSession session, Model model) {
        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("pt-BR"));

            List<Lancamento> lancamentos = lancamentoService.getLancamentosByUsuario(usuario.orElseThrow());

            Map<String, Map<String, List<Lancamento>>> lancamentosPorMes = lancamentos.stream()
                    .sorted(Comparator.comparing(l -> LocalDate.parse(l.getData(), formatter)))
                    .collect(Collectors.groupingBy(l -> {
                        LocalDate date = LocalDate.parse(l.getData(), formatter);
                        YearMonth yearMonth = YearMonth.from(date);
                        return yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR")));
                    }, LinkedHashMap::new, Collectors.groupingBy(l -> l.getTipo().getNome())));

            model.addAttribute("lancamentosPorMes", lancamentosPorMes);
        }
        return "lancamentos";
    }

    @PostMapping("/lancamentos/create")
    public String createLancamento(@ModelAttribute("lancamento") Lancamento lancamento,  HttpSession session, BindingResult result, Model model) {
        String username = session.getAttribute("username").toString();

        if (result.hasErrors() ) {
            return "novo-lancamento";
        }

        if (lancamento.getTipo() == null) {
            result.rejectValue("tipo", "NotNull", "Tipo is required");
            return "novo-lancamento";
        }

        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()){
            lancamento.setUsuario(usuario.get());
            lancamentoService.saveLancamento(lancamento);
            return "redirect:/lancamentos";
        }

        return "novo-lancamento";
    }

    @GetMapping("/lancamentos/update/{codigo}")
    public String formularioUpdate(@PathVariable("codigo") UUID codigo, HttpSession session, Model model) {
        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            Lancamento lancamento = lancamentoService.getLancamento(codigo);
            model.addAttribute("lancamento", lancamento);
        }
        return "update-lancamento";
    }

    @PostMapping("lancamentos/update/{codigo}")
    public String updateLancamento(@PathVariable("codigo") UUID codigo, HttpSession session, Lancamento lancamento, BindingResult result, Model model) {

        if (result.hasErrors()) {
            lancamento.setCodigo(codigo);
            return "update-lancamento";
        }

        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            lancamento.setUsuario(usuario.get());
            lancamentoService.saveLancamento(lancamento);
            if (lancamento.getValor() != lancamentoService.getLancamento(codigo).getValor()) {
                userService.atualizarSaldo(lancamento.getTipo().getNome(), lancamento.getValor(), usuario.get());
            }
        }
        return "redirect:/lancamentos";
    }

    @PostMapping("lancamentos/delete/{codigo}")
    public String deleteLancamento(@PathVariable("codigo") UUID codigo, HttpSession session) {
        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            lancamentoService.deleteLancamento(codigo);
        }
        return "redirect:/lancamentos";
    }


    @GetMapping("/graficos")
    public String graficos(HttpSession session, Model model) {
        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            float gastos = lancamentoService.getLancamentoTotalGastos(usuario.get());
            float receita = lancamentoService.getLancamentoTotalReceita(usuario.get());

            model.addAttribute("gastos", gastos);
            model.addAttribute("receitas", receita);
            return "graficos";
        }
        return "lancamentos";
    }

}
