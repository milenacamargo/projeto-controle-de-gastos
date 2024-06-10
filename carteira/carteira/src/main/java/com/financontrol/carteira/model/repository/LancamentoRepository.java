package com.financontrol.carteira.model.repository;

import com.financontrol.carteira.model.entity.Lancamento;
import com.financontrol.carteira.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
    Lancamento findByCodigo(UUID codigo);

    List<Lancamento> findAllByUsuario(Usuario usuario);

    void deleteById(UUID codigo);
}
