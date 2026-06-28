package com.garzonpro.Auth.client;

import com.garzonpro.Auth.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserClient {

    @PostMapping("/usuarios")
    void crearPerfilUsuario(@RequestBody RegisterRequestDTO dto);

    @org.springframework.web.bind.annotation.PutMapping("/usuarios/{id}")
    void actualizarPerfilUsuario(@org.springframework.web.bind.annotation.PathVariable("id") Long id,
                                 @org.springframework.web.bind.annotation.RequestBody RegisterRequestDTO dto);

    @org.springframework.web.bind.annotation.DeleteMapping("/usuarios/{id}")
    void eliminarPerfilUsuario(@org.springframework.web.bind.annotation.PathVariable("id") Long id);
}