package com.log_austral.cl.log_austral.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.log_austral.cl.log_austral.model.Camion;
import com.log_austral.cl.log_austral.service.CamionService;
import com.log_austral.cl.log_austral.service.ArchivoService;

@RestController
@RequestMapping("/api/v1/camion")
public class CamionController {

    @Autowired
    private CamionService camionService;

    @Autowired
    private ArchivoService archivoService;

    @GetMapping("/{id}")
    public ResponseEntity<Camion> getCamionById(@PathVariable Integer id) {
        try {
            Camion camionGet = this.camionService.findById(id);
            return ResponseEntity.ok(camionGet);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Camion>> getAllCamiones() {
        List<Camion> listaCamiones = this.camionService.findAll();
        if (listaCamiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaCamiones);
    }

    @PostMapping()
    public ResponseEntity<Camion> saveCamion(@RequestBody Camion camion) {
        try {
            Camion camionPost = this.camionService.saveCamion(camion);
            return ResponseEntity.status(HttpStatus.CREATED).body(camionPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // crear camion con imagen (multipart: camion + file)
    @PostMapping(value = "/with-image", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveCamionWithImage(
            @RequestPart("camion") Camion camion,
            @RequestPart("file") MultipartFile file) {
        try {
            String uri = archivoService.store(file);
            camion.setImagenUri(uri);
            Camion saved = camionService.saveCamion(camion);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear camion con imagen");
        }
    }

    // Subir/actualizar imagen de un camion existente
    @PostMapping(value = "/{id}/imagen", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImagen(@PathVariable Integer id, @RequestPart("file") MultipartFile file) {
        try {
            if (!camionService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            String uri = archivoService.store(file);
            Camion existente = camionService.findById(id);
            existente.setImagenUri(uri);
            Camion actualizado = camionService.saveCamion(existente);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> putCamion(@PathVariable Integer id, @RequestBody Camion camion) {
        try {
            if (!this.camionService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            Camion existente = this.camionService.findById(id);
            existente.setPatente(camion.getPatente());
            existente.setMarca(camion.getMarca());
            existente.setModelo(camion.getModelo());
            existente.setAnnio(camion.getAnnio());
            existente.setTipo(camion.getTipo());
            existente.setCapacidad(camion.getCapacidad());
            existente.setDisponibilidad(camion.getDisponibilidad());
            existente.setEstado(camion.getEstado());
            existente.setDescripcion(camion.getDescripcion());
            existente.setTraccion(camion.getTraccion());
            existente.setPrecio(camion.getPrecio());
            if (camion.getImagenUri() != null) {
                existente.setImagenUri(camion.getImagenUri());
            }
            Camion actualizado = this.camionService.saveCamion(existente);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCamion(@PathVariable Integer id) {
        try {
            this.camionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
