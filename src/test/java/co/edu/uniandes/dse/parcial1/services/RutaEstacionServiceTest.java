package co.edu.uniandes.dse.parcial1.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
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

}
