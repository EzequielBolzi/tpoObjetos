package utils;

import java.util.HashMap;
import java.util.Map;
import com.tpo.armarPartido.controller.ControllerUsuario;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;

public class GenerarUsuarios {
	
	public static void generar(ControllerUsuario userController) {
	    // Usuarios con sus niveles por deporte
	    Map<Deporte, Nivel> nivelesJuan = new HashMap<>();
	    nivelesJuan.put(Deporte.FUTBOL, Nivel.INTERMEDIO);
	    nivelesJuan.put(Deporte.BASQUET, Nivel.PRINCIPIANTE);

	    Map<Deporte, Nivel> nivelesAna = new HashMap<>();
	    nivelesAna.put(Deporte.VOLEY, Nivel.INTERMEDIO);
	    nivelesAna.put(Deporte.FUTBOL, Nivel.INTERMEDIO);

	    Map<Deporte, Nivel> nivelesLuis = new HashMap<>();
	    nivelesLuis.put(Deporte.FUTBOL, Nivel.AVANZADO);
	    nivelesLuis.put(Deporte.BASQUET, Nivel.AVANZADO);
	    
	    Map<Deporte, Nivel> nivelesSofia = new HashMap<>();
	    nivelesSofia.put(Deporte.FUTBOL, Nivel.PRINCIPIANTE);
	    nivelesSofia.put(Deporte.VOLEY, Nivel.INTERMEDIO);

	    Map<Deporte, Nivel> nivelesMartin = new HashMap<>();
	    nivelesMartin.put(Deporte.BASQUET, Nivel.INTERMEDIO);
	    nivelesMartin.put(Deporte.VOLEY, Nivel.AVANZADO);

	    Map<Deporte, Nivel> nivelesCarla = new HashMap<>();
	    nivelesCarla.put(Deporte.FUTBOL, Nivel.INTERMEDIO);
	    nivelesCarla.put(Deporte.BASQUET, Nivel.PRINCIPIANTE);
	    nivelesCarla.put(Deporte.VOLEY, Nivel.PRINCIPIANTE);

	    userController.crearUsuario("Juan Perez", "juan@example.com", "1234", nivelesJuan,
	            MedioNotificacion.EMAIL, new Ubicacion(1, 3));

	    userController.crearUsuario("Ana Gomez", "ana@example.com", "abcd", nivelesAna,
	            MedioNotificacion.SMS, new Ubicacion(15, 16));

	    userController.crearUsuario("Luis Martinez", "luis@example.com", "xyz789", nivelesLuis,
	            MedioNotificacion.SMS, new Ubicacion(5, 8));

		userController.crearUsuario("Sofia Lopez", "sofia@example.com", "clave1", nivelesSofia,
		        MedioNotificacion.EMAIL, new Ubicacion(4, 2));
		
		userController.crearUsuario("Martin Ruiz", "martin@example.com", "pass456", nivelesMartin,
		        MedioNotificacion.SMS, new Ubicacion(6, 1));
		
		userController.crearUsuario("Carla Fernández", "carla@example.com", "segura321", nivelesCarla,
		        MedioNotificacion.EMAIL, new Ubicacion(3, 5));
	}


}
