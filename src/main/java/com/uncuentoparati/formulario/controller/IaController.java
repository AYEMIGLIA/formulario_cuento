package com.uncuentoparati.formulario.controller;

import com.uncuentoparati.formulario.model.FormularioRequest;
import com.uncuentoparati.formulario.service.IaService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // <-- tu dominio
public class IaController {

    private final IaService iaService;

    public IaController(IaService iaService) {
        this.iaService = iaService;
    }

    @PostMapping("/recomendar")
    public Map<String, Object> recomendar(@RequestBody FormularioRequest data) throws Exception {
        String recomendacionIA = iaService.generarRecomendacion(data.getPerfil());
        Map<String, Object> result = new HashMap<>();
        result.put("perfil", data.getPerfil());
        result.put("recomendacionIA", recomendacionIA);
        return result;
    }
}
