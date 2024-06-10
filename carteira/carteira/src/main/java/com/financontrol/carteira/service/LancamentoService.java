package com.financontrol.carteira.service;

import com.financontrol.carteira.model.entity.Lancamento;
import com.financontrol.carteira.model.entity.Usuario;
import com.financontrol.carteira.model.repository.LancamentoRepository;
import com.financontrol.carteira.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class LancamentoService {
    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void deleteLancamento(UUID codigo) {
        lancamentoRepository.deleteById(codigo);
    }

    public Lancamento saveLancamento(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public float getLancamentoTotalGastos(Usuario usuario) {
        List<Lancamento> lancamento = getLancamentosByUsuario(usuario);
        float totalGastos = 0;
        for (Lancamento lancamentoItem : lancamento) {
            if (Objects.equals(lancamentoItem.getTipo().getId(), 0))
            {
                totalGastos += lancamentoItem.getValor();
            }
        }
        return totalGastos;
    }

    public float getLancamentoTotalReceita(Usuario usuario) {
        List<Lancamento> lancamento = getLancamentosByUsuario(usuario);
        float totalReceita = 0;
        for (Lancamento lancamentoItem : lancamento) {
            if (Objects.equals(lancamentoItem.getTipo().getId(), 1))
            {
                totalReceita+= lancamentoItem.getValor();
            }
        }
        return totalReceita;    }

    public List<Lancamento> getLancamentosByUsuario(Usuario usuario) {
        return lancamentoRepository.findAllByUsuario(usuario);
    }

    public Lancamento getLancamento(UUID codigo) {
        return lancamentoRepository.findByCodigo(codigo);
    }

}
