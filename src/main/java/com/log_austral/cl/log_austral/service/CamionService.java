package com.log_austral.cl.log_austral.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.log_austral.cl.log_austral.model.Camion;
import com.log_austral.cl.log_austral.repository.CamionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CamionService {

    @Autowired
    private CamionRepository camionRepository;

    public Camion findById(Integer id) {
        return this.camionRepository.findById(id).get();
    }

    public List<Camion> findAll() {
        return this.camionRepository.findAll();
    }

    public Camion saveCamion(Camion camion) { // tambien funciona para PUT (actualizar)
        return this.camionRepository.save(camion);
    }

    public void deleteById(Integer id) {
        this.camionRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        // retorna true si existe
        return this.camionRepository.existsById(id);
    }

}
