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

    @Transactional
	public EstacionEntity getEstacion(Long rutaId, Long estacionId)
			throws EntityNotFoundException, IllegalOperationException 
    {
		log.info("Inicia proceso de consultar una estacion de la ruta con id = {0}", rutaId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);

		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException("Estacion no encontrada");

		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException("Ruta no encontrada");
        log.info("Termina proceso de consultar una estacion de la ruta con id = {0}", rutaId);

		if (!rutaEntity.get().getEstaciones().contains(estacionEntity.get()))
			throw new IllegalOperationException("La estacion no esta asociada a la ruta");
		
		return estacionEntity.get();
	}

    @Transactional
    public void removeEstacionRuta(Long rutaId, Long estacionId) throws EntityNotFoundException, IllegalOperationException 
    {
		log.info("Inicia proceso de borrar una estacion de la ruta con id = {0}", rutaId);
		Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
		Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);

		if (estacionEntity.isEmpty())
			throw new EntityNotFoundException("La estacion no se encontro");

		if (rutaEntity.isEmpty())
			throw new EntityNotFoundException("La ruta no se encontro");

        if((rutaEntity.get().getTipo().equals("nocturna"))&&(rutaEntity.get().getEstaciones().size()==1))
          throw new IllegalOperationException("No se puede eliminar la asociación ya que la única ruta asociada a la estacion es nocturna");

		rutaEntity.get().getEstaciones().remove(estacionEntity.get());

		log.info("Termina proceso de borrar una estacion de la ruta con id = {0}", rutaId);
	}

}
