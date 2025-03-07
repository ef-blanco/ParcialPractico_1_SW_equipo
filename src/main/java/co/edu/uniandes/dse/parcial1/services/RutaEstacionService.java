package co.edu.uniandes.dse.parcial1.services;

import java.util.Optional;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RutaEstacionService 
{
    @Autowired
	private RutaRepository rutaRepository;

	@Autowired
	private EstacionRepository estacionRepository;

    @Transactional
	public EstacionEntity addEstacionRuta(Long rutaId, Long estacionId) throws EntityNotFoundException, IllegalOperationException 
    {
		log.info("Inicia proceso de asociarle una estacion a la ruta con id = {0}", rutaId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la estacion");

		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException("No se encontro la ruta");

        if ((estacionEntity.get().getCapacidad()<100)&&(estacionEntity.get().getRutas().size()==2)) 
           throw new IllegalOperationException("No se pueden aniadir mas rutas a la estacion");
		rutaEntity.get().getEstaciones().add(estacionEntity.get());
		log.info("Termina el proceso de asociarle una estacion a la ruta con id = {0}", rutaId);
		return estacionEntity.get();
	}


}
