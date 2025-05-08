package com.spring.udemy.controlagua.controller;

import com.spring.udemy.controlagua.model.Usuario;
import com.spring.udemy.controlagua.service.PdfGeneratorService;
import com.spring.udemy.controlagua.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PdfGeneratorService pdfGeneratorService;

    public UsuarioController(UsuarioService usuarioService, PdfGeneratorService pdfGeneratorService) {
        this.usuarioService = usuarioService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping
    public String listarUsuarios(Model model, @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size){

        Pageable paginable = PageRequest.of(page, size, Sort.by("apellido").ascending());
        Page<Usuario> usuariosPage = usuarioService.listarUsuarios(paginable);
        model.addAttribute("usuarios", usuariosPage);
        model.addAttribute("paginaActual", page);
        return "usuario/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(Model model, @PathVariable Long id){
        model.addAttribute("usuario", usuarioService.buscarUsuarioPorId(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        return "usuario/formulario";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model){
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Cuando hay errores, el objeto "usuario" se mantiene en el modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarios", usuarioService.listaUsuario());
            return "usuario/formulario";  // Regresar al formulario con los errores
        }

        usuario.setEstado("no");
        usuarioService.guardarUsuario(usuario);  // Si la validación pasa, guardar el usuario
        return "redirect:/usuario";  // Redirigir a la lista de usuarios
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id).orElseThrow();

        Map<String, Object> data = new HashMap<>();
        data.put("usuario", usuario);

        model.addAttribute("usuario", usuario);

        byte[] pdfBytes = pdfGeneratorService.generatePdf("usuario-pdf", data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "usuario_" + usuario.getNombre() + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
