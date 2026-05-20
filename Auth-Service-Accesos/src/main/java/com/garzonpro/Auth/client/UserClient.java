package com.garzonpro.Auth.client;

import com.garzonpro.Auth.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// name: nombre del servicio destino
// url: donde esta corriendo el User-Service (puerto 8081)
@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @PostMapping("/users/create")
    void crearPerfilUsuario(@RequestBody RegisterRequestDTO dto);
}