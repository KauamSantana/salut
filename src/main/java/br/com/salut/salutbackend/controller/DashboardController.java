package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.DashboardDTO;
import br.com.salut.salutbackend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/representante/{id}")
    public ResponseEntity<DashboardDTO> getDashboardParaRepresentante(@PathVariable Long id) {
        DashboardDTO dashboardData = dashboardService.getDashboardInfo(id);
        return ResponseEntity.ok(dashboardData);
    }
}