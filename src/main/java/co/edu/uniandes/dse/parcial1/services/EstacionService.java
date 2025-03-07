package co.edu.uniandes.dse.parcial1.services;

import java.util.Optional;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstacionService 
{
    @Autowired
    EstacionRepository estacionRepository;

    @Transactional
	public EstacionEntity createEstacion(EstacionEntity estacionEntity) throws EntityNotFoundException, IllegalOperationException 
    {
		log.info("Inicia proceso de creación de la estacion");

		log.info("Termina proceso de creación del libro");
		return estacionRepository.save(estacionEntity);
	}

    @Transactional
    public EstacionEntity getEstacion(Long estacionId) throws EntityNotFoundException 
    {
		log.info("Inicia proceso de consultar la estacion con id = {0}", estacionId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException("Estacion no encontrada");
		log.info("Termina proceso de consultar la estacion con id = {0}", estacionId);
		return estacionEntity.get();
	}

}
