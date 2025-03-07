package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ RutaEstacionService.class, EstacionService.class })
public class RutaEstacionServiceTest 
{
    @Autowired
	private RutaEstacionService rutaEstacionService;

	@Autowired
	private EstacionService estacionService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private RutaEntity ruta = new RutaEntity();
	private List<EstacionEntity> estacionList = new ArrayList<>();

    @BeforeEach
	void setUp() 
    {
		clearData();
		insertData();
	}

    private void clearData() 
    {
		entityManager.getEntityManager().createQuery("delete from RutaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
	}

    private void insertData() 
    {	
		ruta = factory.manufacturePojo(RutaEntity.class);
		entityManager.persist(ruta);

		for (int i = 0; i < 3; i++) {
			EstacionEntity entity = factory.manufacturePojo(EstacionEntity.class);
			entity.getRutas().add(ruta);
			entityManager.persist(entity);
			estacionList.add(entity);
			ruta.getEstaciones().add(entity);
		}
	}


    @Test
	void testAddEstacionRuta() throws EntityNotFoundException, IllegalOperationException 
    {
		EstacionEntity newEstacion = factory.manufacturePojo(EstacionEntity.class);
		estacionService.createEstacion(newEstacion);

		EstacionEntity estacionEntity = rutaEstacionService.addEstacionRuta(ruta.getId(), newEstacion.getId());
		assertNotNull(estacionEntity);

		assertEquals(estacionEntity.getId(), newEstacion.getId());
		assertEquals(estacionEntity.getName(), newEstacion.getName());
		assertEquals(estacionEntity.getDireccion(), newEstacion.getDireccion());
		assertEquals(estacionEntity.getCapacidad(), newEstacion.getCapacidad());

		EstacionEntity lastEstacion = rutaEstacionService.getEstacion(ruta.getId(), newEstacion.getId());

		
		assertEquals(lastEstacion.getId(), newEstacion.getId());
		assertEquals(lastEstacion.getName(), newEstacion.getName());
		assertEquals(lastEstacion.getDireccion(), newEstacion.getDireccion());
		assertEquals(lastEstacion.getCapacidad(), newEstacion.getCapacidad());

	}

    @Test
	void testRemoveEstacionRuta() throws EntityNotFoundException, IllegalOperationException 
    {
		for (EstacionEntity estacion : estacionList) 
        {
			rutaEstacionService.removeEstacionRuta(ruta.getId(), estacion.getId());
		}
		assertTrue(rutaEstacionService.getEstaciones(ruta.getId()).isEmpty());
	}
}
