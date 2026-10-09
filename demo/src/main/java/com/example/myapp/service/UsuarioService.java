/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.myapp.service;

import com.example.myapp.model.Usuario;
import com.example.myapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarCredenciales(String correo, String clave) {
        // Asumiendo que definiste findByCorreoAndClave en tu UsuarioRepository
        Optional<Usuario> usuario = usuarioRepository.findByCorreoAndClave(correo, clave);
        return usuario.isPresent();
    }
    // Método para guardar un usuario en la BD
    public Usuario guardarUsuario(Usuario usuario) {
        // Aquí puedes agregar validaciones de negocio en el futuro
        return usuarioRepository.save(usuario);
    }

    // Método opcional para obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}