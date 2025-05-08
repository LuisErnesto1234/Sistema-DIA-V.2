package com.spring.udemy.controlagua.service;

import com.spring.udemy.controlagua.model.Usuario;
import com.spring.udemy.controlagua.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void guardarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> listaUsuario(){
        return Collections.unmodifiableList(usuarioRepository.findAll());
    }

    public void MinutosUtilizadosOperacion(Integer minutosUtilizados, Usuario usuario){
        if (usuario != null && minutosUtilizados != null){
            if (minutosUtilizados <= usuario.getMinutosSemana()){
                usuario.setMinutosSemana(usuario.getMinutosSemana() - minutosUtilizados);
            }else {
                if (usuario.getMinutosSemana() > 0){
                    Integer minutosRestantes = minutosUtilizados - usuario.getMinutosSemana();
                    usuario.setMinutosSemana(0);
                    usuario.setMinutosAcumulados(usuario.getMinutosAcumulados() - minutosRestantes);
                }else {
                    Integer minutosRestantes = usuario.getMinutosAcumulados() - minutosUtilizados;
                    usuario.setMinutosAcumulados(minutosRestantes);
                }
            }
        }
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
}
