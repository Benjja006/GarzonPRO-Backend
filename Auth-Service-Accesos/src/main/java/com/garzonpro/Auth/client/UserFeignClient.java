package com.garzonpro.Auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Con esto le decimos a Spring que este archivo es un cliente Feign.
// "url" debe apuntar al puerto donde corre tu User-Service (que es el 8081).
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserFeignClient {

    // Este método debe ser idéntico al endpoint de User-Service para buscar por ID.
    @GetMapping("/usuarios/{id}")
    Object obtenerUsuarioPorId(@PathVariable("id") Long id);
}