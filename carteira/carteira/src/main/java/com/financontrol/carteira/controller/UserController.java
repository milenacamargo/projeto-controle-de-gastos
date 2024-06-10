package com.financontrol.carteira.controller;

import com.financontrol.carteira.model.entity.Usuario;
import com.financontrol.carteira.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public String updateUser(HttpSession session, Usuario usuarioAtualizar, Model model) {
        String username = session.getAttribute("username").toString();
        Optional<Usuario> usuario = userService.listByEmail(username);
        if (usuario.isPresent()) {
            var usuarioOptional = userService.listByEmail(usuarioAtualizar.getEmail());
            if (usuarioOptional.isEmpty()) {
                return "redirect:/user";
            }
            usuarioAtualizar.setId(usuarioOptional.get().getId());
            Usuario usuarioNovo = userService.save(usuarioAtualizar);
            model.addAttribute("usuarioAtualizar", usuarioNovo);
        }
        return "redirect:/user";
}

}