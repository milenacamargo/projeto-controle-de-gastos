package com.financontrol.carteira.controller;

import com.financontrol.carteira.model.entity.Usuario;
import com.financontrol.carteira.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.Optional;


@Controller
public class AuthenticateController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public String login(HttpSession session, Usuario body, Model model) {
        Optional<Usuario> usuario = userService.listByEmail(body.getEmail());
        if (usuario.isPresent()) {
            if (Objects.equals(body.getSenha(), usuario.get().getSenha())) {
                session.setAttribute("username", usuario.get().getEmail());
                return "redirect:/lancamentos";
            }
        }
        model.addAttribute("erro", "Usuário não encontrado!");
        return "auth";
    }


    @PostMapping("/auth/register")
    public String register(HttpSession session, Usuario body, Model model) {
        var usuarioOptional = userService.listByEmail(body.getEmail());
        if (usuarioOptional.isEmpty()) {
            Usuario usuarioNovo = userService.save(body);
            session.setAttribute("username", usuarioNovo.getEmail());
            return "redirect:/lancamentos";
        }
        model.addAttribute("erro", "Usuário já existe!");
        return "auth";
    }

    @GetMapping("/auth")
    public String templatelogin(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "auth";
    }

    @GetMapping
    public String index() {
        return "redirect:/auth";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

}