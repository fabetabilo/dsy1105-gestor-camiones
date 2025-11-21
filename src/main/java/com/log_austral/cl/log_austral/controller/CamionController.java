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
import org.springframework.web.bind.annotation.RestController;

import com.log_austral.cl.log_austral.model.Camion;
import com.log_austral.cl.log_austral.service.CamionService;

@RestController
@RequestMapping("/api/v1/camion")
public class CamionController {

    @Autowired
    private CamionService camionService;

    @GetMapping("/{id}")
    public ResponseEntity<Camion> getCamionById(@PathVariable Integer id) {
        try {
            Camion camionGet = this.camionService.findById(id);
            return ResponseEntity.ok(camionGet);
        
        } catch (Exception e) {
            // para cuando no encuentre nada con ese id, construye la respuesta Not Found
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
            // maneja la excepcion en caso de que elc uerpo body sea invalido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> putCamion(@PathVariable Integer id, @RequestBody Camion camion) {
        try {
            if (this.camionService.existsById(id)) {
                // el camion que actualiza, es el que viene buscado por su id
                Camion camionPut = this.camionService.findById(id);

                // setea los valores a actualizar
                camionPut.setId(id);
                camionPut.setPatente(camionPut.getPatente());
                camionPut.setMarca(camionPut.getMarca());
                camionPut.setModelo(camionPut.getModelo());
                camionPut.setAnnio(camionPut.getAnnio());
                camionPut.setTipo(camionPut.getTipo());
                camionPut.setCapacidad(camionPut.getCapacidad());
                camionPut.setDisponibilidad(camionPut.getDisponibilidad());
                camionPut.setEstado(camionPut.getEstado());
                camionPut.setDescripcion(camionPut.getDescripcion());
                camionPut.setTraccion(camionPut.getTraccion());
                camionPut.setPrecio(camionPut.getPrecio());
                camionPut.setImagenUri(camionPut.getImagenUri());

                this.camionService.saveCamion(camionPut);
                return ResponseEntity.ok(camion);
            
            } else {
                return ResponseEntity.notFound().build();
            }

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
