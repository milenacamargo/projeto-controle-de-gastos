package com.financontrol.carteira.service;

import com.financontrol.carteira.model.entity.Usuario;
import com.financontrol.carteira.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    @Autowired
    private UsuarioRepository userRepository;

    public Usuario atualizarSaldo(String tipo, float valor, Usuario usuario) {
        if (tipo == "DESPESA")
        {
            usuario.setSaldo(usuario.getSaldo() - valor);
            usuario = save(usuario);
        }
        else if (tipo == "RECEITA")
        {
            usuario.setSaldo(usuario.getSaldo() + valor);
            usuario = save(usuario);
        }
        return usuario;
    }

    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public Optional<Usuario> listByEmail(String email) {
        return userRepository.findByEmail(email);
}
}